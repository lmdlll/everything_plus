package com.lmd.core.search;

import com.lmd.core.Model.Condition;
import com.lmd.core.Model.Thing;
import com.lmd.core.dao.DataSourceFactory;
import com.lmd.core.dao.Impl.FileInedxDaoImpl;
import com.lmd.core.search.impl.FileSerchImpl;

import java.util.List;

public interface FileSerch {
    /**
     * 根据condition条件进行数据库的检索
     * @param condition
     * @return
     */
   List<Thing> search(Condition condition);

//    public static void main(String[] args) {
//        Condition condition = new Condition();
//        condition.setLimit(10);
//        condition.setName("test");
//        condition.setOrderByAsc(true);
//        FileSerch fileSerch = new FileSerchImpl(new FileInedxDaoImpl(DataSourceFactory.dataSource()));
//        List<Thing> list = fileSerch.search(new Condition());
//        for (Thing things: list) {
//            System.out.println(things);
//        }
//    }

}
