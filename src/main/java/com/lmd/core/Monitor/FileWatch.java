package com.lmd.core.Monitor;

import com.lmd.core.Common.HandlePath;

public interface FileWatch {
    /**
     * 监听启动
     */
    void start();

    /**
     * 监听的目录
     */
    void monitor(HandlePath handlePath);

    /**
     * 监听停止
     */
    void stop();
}
