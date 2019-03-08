package com.lmd.core.interceptor.impl;

import com.lmd.core.Model.Thing;
import com.lmd.core.dao.FileIndexDao;
import com.lmd.core.dao.Impl.FileInedxDaoImpl;
import com.lmd.core.interceptor.ThingTnterceptor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThingClearInterceptor implements ThingTnterceptor,Runnable {
    private Queue<Thing> queue = new ArrayBlockingQueue<>(1024);
    private final FileIndexDao fileIndexDao;

    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(Thing thing) {
        this.queue.add(thing);

    }


    @Override
    public void run() {
        while (true){
            Thing thing = this.queue.poll();
            if(thing!=null){
                fileIndexDao.delete(thing);
            }
            //1.一条条删，可优化 ---> 批量删除，未完成。。。
//            List<Thing> thingList = new ArrayList<>();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
