package com.lmd.core.index.Impl;

import com.lmd.config.everythingConfig;
import com.lmd.core.index.FileScan;
import com.lmd.core.interceptor.FileIntercrptor;
import java.io.File;
import java.util.LinkedList;


public class FileScanImpl implements FileScan {
    private everythingConfig config = everythingConfig.getInstance();
    private LinkedList<FileIntercrptor> interceptors = new LinkedList<>();

    @Override
    public void index(String path) {
        File file = new File(path);
        if (file.isFile()) {
            //D:\a\b\abc.pdf  ->  D:\a\b
            if (config.getExcludePath().contains(file.getParent())) {
                return;
            }
        } else {
            if (config.getExcludePath().contains(path)) {
                return;
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        index(f.getAbsolutePath());
                    }
                }
            }
        }

        //File Directory
        for (FileIntercrptor interceptor : this.interceptors) {
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileIntercrptor intercrptor) {
        this.interceptors.add(intercrptor);
    }


}
