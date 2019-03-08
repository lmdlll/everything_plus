package com.lmd.core.search.impl;

import com.lmd.core.Model.Condition;
import com.lmd.core.Model.Thing;
import com.lmd.core.dao.DataSourceFactory;
import com.lmd.core.dao.FileIndexDao;
import com.lmd.core.dao.Impl.FileInedxDaoImpl;
import com.lmd.core.search.FileSerch;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


//业务层
public class FileSerchImpl implements FileSerch {
    //想要将检索的信息以Thing输出
//    //1.首先要有数据库连接， DataSourceFactory,为什么不用此，为了避免耦合
//    //如何传？
//    //定义一个属性，属性叫DataSourc，初始化通过构造方法来完成
//    private final DataSource dataSource;
//    public FileSerchImpl(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }


    private final FileIndexDao fileIndexDao;
    public FileSerchImpl( FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public List<Thing> search(Condition condition) {
        //数据库的处理逻辑
        if(condition==null){
            return new ArrayList<>();
        }
        return this.fileIndexDao.search(condition);
    }

//    public static void main(String[] args) {
////        FileSerch fileSerch = new FileSerchImpl(DataSourceFactory.dataSource());
//        FileSerch fileSerch  = new FileSerchImpl(new FileInedxDaoImpl(DataSourceFactory.dataSource()));
//        List<Thing> things = fileSerch.search(new Condition());
//        System.out.println(things);
//    }
}
