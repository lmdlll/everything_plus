package com.lmd.core.Model;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum  FileType {
    IMG("png","jpeg","jpg","gif"),          //图片
    DOC("ppt","pptx","doc","docx","pdf" ),  //文档
    BIN("exe","jar","sh","msi"),            //二进制
    ARCHIVE("zip","rar"),                   //归档
    OTHER("*");


    // 对应的文件类型的扩展名集合
    private Set<String> extend = new HashSet<>();
    FileType(String... extend){
        this.extend.addAll(Arrays.asList(extend));
    }

    /**
     * 根据文件扩展名获取文件类型   eg:ppt->DOC
     * @param exten
     * @return
     */
    public static FileType lookup(String exten){
        for(FileType fileType:FileType.values()){
            if(fileType.extend.contains(exten)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }

    /**
     * 根据文件类型名（String）获取文件类型对象  eg: DOC->DOC
     * 啥啥啥，不一样吗？
     * @param name
     * @return
     */
    //TODO:不明白意思，问何荣
    public static FileType lookupByName(String name){
        for(FileType fileType:FileType.values()){
            if(fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }



    //测试
//    public static void main(String[] args) {
//        System.out.println(FileType.lookup("ppt"));
//        System.out.println(FileType.lookup("java"));
//        System.out.println(FileType.lookup("jpeg"));
//        System.out.println(FileType.lookup("exe"));
//
//    }


}
