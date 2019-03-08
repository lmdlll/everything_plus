package com.lmd.core.dao.Impl;


import com.lmd.config.everythingConfig;
import com.lmd.core.Model.Thing;
import com.lmd.core.dao.DataSourceFactory;
import com.lmd.core.dao.FileIndexDao;
import com.lmd.core.index.FileScan;
import com.lmd.core.interceptor.FileIntercrptor;
import com.lmd.core.interceptor.impl.FileIndexInterceptor;
import com.lmd.core.interceptor.impl.FilePrintInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileScanImpl implements FileScan {
    //DAO
    private everythingConfig config = everythingConfig.getInstance();
    private LinkedList<FileIntercrptor> intercrptors = new LinkedList<>();

//递归调用   管道机制
    @Override
    public void index(String path) {
        File file  = new File(path);
        if(file.isFile()){
            if(config.getExcludePath().contains(file.getParent())) {
                return;
            }
        }else {
            if(config.getExcludePath().contains(path)){
                return;
            }else {
                // file -> Thing ->dao
                File[] files = file.listFiles();
                if (file != null) {
                    for (File f : files) {
                        index(f.getAbsolutePath());
                    }
                }
            }
        }

        for(FileIntercrptor intercrptor:this.intercrptors){
            intercrptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileIntercrptor intercrptor) {
            this.intercrptors.add(intercrptor);
    }

//    public void addFileInterceptor(FileIntercrptor fileIntercrptor){
//        this.intercrptors.add(fileIntercrptor);
//    }


//    public static void main(String[] args) {
//        FileScan scan = new FileScanImpl();
//        FileIntercrptor intercrptor = new FilePrintInterceptor();
//        ((FileScanImpl) scan).addFileInterceptor(intercrptor);
//        FileIntercrptor fileindexIntercrptor = new FileIndexInterceptor(new FileInedxDaoImpl(DataSourceFactory.dataSource()));
//        ((FileScanImpl) scan).addFileInterceptor(fileindexIntercrptor);
//        scan.index("F:\\javaSE");
//
//    }


}
