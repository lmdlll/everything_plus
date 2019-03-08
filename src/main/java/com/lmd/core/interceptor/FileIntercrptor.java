package com.lmd.core.interceptor;


import java.io.File;

//函数接口：只有一个接口
@FunctionalInterface
public interface FileIntercrptor {
    void apply(File file);

}
