package com.lmd.core.Common;


import com.lmd.core.Model.FileType;
import com.lmd.core.Model.Thing;

import java.io.File;


/**
 * 辅助工具类，将File对象转换成Thing对象
 *     不希望实例化此类，也不希望修改继承此类
 */
public final class FileConverThing {
    private FileConverThing(){}

    //转换器：将File--> Thing
    public static Thing convert(File file){
        Thing thing = new Thing();
        thing.setName(file.getName());
        thing.setPath(file.getAbsolutePath());
        thing.setDepth(computeFileDepth(file));
        thing.setFileType(computeFileType(file));

        return thing;
    }

    private static int computeFileDepth(File file){
//        String[] res = file.getAbsolutePath().split(File.separator);
        String[] res = file.getAbsolutePath().split("\\\\");
        return res.length;
    }

    private static FileType computeFileType(File file){
        if(file.isDirectory()){
            return FileType.OTHER;
        }
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if(index!=-1 && index<fileName.length()-1){
            //防止abc.
            String exten = fileName.substring(index+1);
            return FileType.lookup(exten);
        }else {
            return FileType.OTHER;
        }

    }

//    public static void main(String[] args) {
//        System.out.println(computeFileDepth(new File("D:\\a\\b\\c.txt")));
//        System.out.println(computeFileDepth(new File("D:\\a\\c.txt")));
//        System.out.println(computeFileType(new File("D:\\a\\c.")));
//    }
}
