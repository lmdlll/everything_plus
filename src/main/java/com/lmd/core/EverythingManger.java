package com.lmd.core;

import com.lmd.config.everythingConfig;
import com.lmd.core.Common.HandlePath;
import com.lmd.core.Model.Condition;
import com.lmd.core.Model.Thing;
import com.lmd.core.Monitor.FileWatch;
import com.lmd.core.dao.DataSourceFactory;
import com.lmd.core.dao.FileIndexDao;
import com.lmd.core.dao.Impl.FileInedxDaoImpl;
import com.lmd.core.dao.Impl.FileScanImpl;
import com.lmd.core.index.FileScan;
import com.lmd.core.interceptor.impl.FileIndexInterceptor;
import com.lmd.core.interceptor.impl.ThingClearInterceptor;
import com.lmd.core.search.FileSerch;
import com.lmd.core.search.impl.FileSerchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//cmd底下的命令行主程序会依赖此类
//此类会依赖core包下的类
public final class EverythingManger {

    private static volatile EverythingManger manger;
    private FileScan fileScan;
    private FileSerch fileSerch;

    //清理删除的文件
    private ThingClearInterceptor thingClearInterceptor;
    private Thread bgClearThread;
    private AtomicBoolean bgClearThreadStatus = new AtomicBoolean(false);


    public EverythingManger() {
        this.initComponent();
    }

    private void initComponent() {
        //数据源对象
        DataSource dataSource = DataSourceFactory.dataSource();
        //检查数据库
        checkDatabase();
        //业务层的对象
        FileIndexDao fileIndexDao = new FileInedxDaoImpl(dataSource);
        this.fileSerch = new FileSerchImpl(fileIndexDao);
        this.fileScan = new FileScanImpl();
//        this.fileScan.interceptor(new FilePrintInterceptor());   真正发布代码时不需要
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
        this.thingClearInterceptor = new ThingClearInterceptor(fileIndexDao);
        this.bgClearThread = new Thread(this.thingClearInterceptor);
        this.bgClearThread.setName("Thread-Thing-Clearn");
        //将其变为守护线程
        this.bgClearThread.setDaemon(true);
    }

    private void checkDatabase() {
        String fileName = everythingConfig.getInstance().getH2IndexPath()+".mv.db";
        File dbfile = new File(fileName);
        if(dbfile.isFile() && !dbfile.exists()){
            DataSourceFactory.initDatabase();
        }
    }

    public static EverythingManger getInstance(){
        if(manger==null){
            synchronized (EverythingManger.class){
                if(manger==null){
                    manger = new EverythingManger();
                }
            }
        }
        return manger;
    }



    private ExecutorService executorService ;


    /**
     * 检索
     */
    public List<Thing> search(Condition condition){
        //NOTICE 扩展
        //Stream 流式处理
       return this.fileSerch.search(condition).stream().filter(thing -> {
            String path = thing.getPath();
            File f = new File(path);
            boolean flag = f.exists();
            if(!flag){
                //做删除操作
                thingClearInterceptor.apply(thing);
            }
            return flag;
        }).collect(Collectors.toList());
    }

    /**
     * 索引，使用多线程，同时遍历
     */
    public void buildIndex(){
        Set<String> directories = everythingConfig.getInstance().getIncludePath();
        if(this.executorService==null){
            this.executorService = Executors.newFixedThreadPool(directories.size(), new ThreadFactory() {
                private final AtomicInteger threadId = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Thread-Scan-"+threadId.getAndIncrement());
                    return thread;
                }
            });
        }

        final CountDownLatch countDownLatch = new CountDownLatch(directories.size());
        System.out.println("Build index start ……");
        for(String path: directories){
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    EverythingManger.this.fileScan.index(path);
                    //当前任务完成，值-1
                    countDownLatch.countDown();
                }
            });
        }
        try {
            //阻塞，直到任务完成，值0
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Build index complete ……");

    }

    /**
     * 启动清理线程
     */
    public void startbgClearThread() {
        if (this.bgClearThreadStatus.compareAndSet(false,true)) {
            this.bgClearThread.start();

        } else {
            System.out.println("不能重复启动bgClearThread");
        }

    }
    /**
     * 文件监控
     */
    private FileWatch fileWatch;

    /**
     * 启动文件系统监听
     */
    public void startFileSystemMonitor() {
        everythingConfig config = everythingConfig.getInstance();
        HandlePath handlePath = new HandlePath();
        handlePath.setIncludePath(config.getIncludePath());
        handlePath.setExcludePath(config.getExcludePath());
        this.fileWatch.monitor(handlePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("文件系统监控启动");
                fileWatch.start();
            }
        }).start();
    }
}


