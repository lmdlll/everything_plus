package com.lmd.core.interceptor.impl;

import com.lmd.core.Common.FileConverThing;
import com.lmd.core.Model.Thing;
import com.lmd.core.dao.FileIndexDao;
import com.lmd.core.interceptor.FileIntercrptor;

import java.io.File;

public class FileIndexInterceptor implements FileIntercrptor {
    private final FileIndexDao fileIndexDao;
    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        Thing thing = FileConverThing.convert(file);
//       System.out.println("Thing--->"+thing);
        fileIndexDao.insert(thing);
    }
}
