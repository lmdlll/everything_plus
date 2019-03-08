package com.lmd.core.interceptor.impl;

import com.lmd.core.interceptor.FileIntercrptor;

import java.io.File;

//打印路径，让FileScanImpl调用此类
public class FilePrintInterceptor implements FileIntercrptor {
    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
