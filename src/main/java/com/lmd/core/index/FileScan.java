package com.lmd.core.index;

import com.lmd.core.interceptor.FileIntercrptor;


public interface FileScan {

    /**
     * 遍历Path
     * @param path
     */
    void index(String path);

    /**
     * 遍历的拦截器
     * @param intercrptor
     */
    void interceptor(FileIntercrptor intercrptor);



//    public static void main(String[] args) {
//        DataSourceFactory.initDatabase();
//
//        FileScan fileScan = new FileScanImpl();
//        FileIntercrptor intercrptor = new FilePrintInterceptor();
//         fileScan.interceptor(intercrptor);
//        FileIndexInterceptor indexInterceptor = new FileIndexInterceptor(new FileInedxDaoImpl(DataSourceFactory.dataSource()));;
//        fileScan.interceptor(indexInterceptor);
//        fileScan.index("F:\\QQ下载");
//
//    }

}
