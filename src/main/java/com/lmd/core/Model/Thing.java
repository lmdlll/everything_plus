package com.lmd.core.Model;

import lombok.Data;

/**
 * 文件属性信息索引之后的记录  用Thing表示
 *       给数据库存储的信息
 */

@Data  //getter setter toString 生成完成
public class Thing {
    /**
     * 文件名称（保留名称）
     * eg:File D:/a/b/hello.txt  -> hello.txt
     */
    private String name;


     // 文件路径
    private String path;


     //文件路径深度
    private Integer depth;


     //文件类型
    private FileType fileType;


}
