package com.lmd.core.Model;


import lombok.Data;

/**
 * 条件信息
 */

@Data
public class Condition {

    private String name;

    private String fileType;

    //限制有多少个数量显示
    private Integer limit;

    /**
     * 检索结果的文件信息depth 排序规则
     * 1.默认是true -> asc
     * 2.false -> desc
     */
    private Boolean orderByAsc;

}
