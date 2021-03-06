package com.lmd.core.dao.Impl;

import com.lmd.core.Model.Condition;
import com.lmd.core.Model.FileType;
import com.lmd.core.Model.Thing;
import com.lmd.core.dao.DataSourceFactory;
import com.lmd.core.dao.FileIndexDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileInedxDaoImpl implements FileIndexDao {
    private final DataSource dataSource;

    public FileInedxDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1. 获取数据库连接
            connection = dataSource.getConnection();
            //2. 准备sql语句
                //四个 ? 表示参数占用
            String sql = "insert into FILE_INDEX(name, path, depth, file_type) values (?, ?, ?, ?) ";
            //3. 准备命令
            statement = connection.prepareStatement(sql);
            //4. 设置参数 1,2，3,4
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4,thing.getFileType().name());
            //5. 执行命令
            statement.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }

    @Override
    public void delete(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1. 获取数据库连接
            connection = dataSource.getConnection();
            //2. 准备sql语句
            String sql = "delete from file_index where path like '"+thing.getPath()+"%'";
            //3. 准备命令
            statement = connection.prepareStatement(sql);
            //5. 执行命令
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }

    }

    @Override
    public List<Thing> search(Condition condition) {
        List<Thing> things = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //1. 获取数据库连接
            connection = dataSource.getConnection();
            //2. 准备sql语句
            //name       :     like
            //fileType   :       =
            //limit      :    limit offset
            //orderByAsc :    order by
            //为什么不用StringBuffer?   ---StringBuffer不会被多线程共享，不会产生竞争
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(" select name, path, depth, file_type from file_index ");
            //name匹配原则：前模糊/后模糊/前后模糊(这里选此)
            sqlBuilder.append(" where ")
                    .append(" name  like '%")
                    .append(condition.getName())
                    .append("%' ");
            if(condition.getFileType()!=null){
                sqlBuilder.append(" and file_type = '")
                        .append(condition.getFileType().toUpperCase())
                        .append("' ");
            }
            //limit , order by 必选
            //先order by 再limit
            sqlBuilder.append(" order by depth ")
                    .append(condition.getOrderByAsc() ? "asc" : "desc");
            sqlBuilder.append(" limit ")
                    .append(condition.getLimit())
                    .append(" offset 0 ");

            System.out.println(sqlBuilder);
            //3. 准备命令
            statement = connection.prepareStatement(sqlBuilder.toString());
            //5. 执行命令
           resultSet = statement.executeQuery();
            //6. 处理结果
            while (resultSet.next()){
               // 数据库中的行记录 ---> java中的对象
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                 String fileType = resultSet.getString("file_type");
                thing.setFileType(FileType.lookupByName(fileType));
                things.add(thing);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            releaseResource(resultSet,statement,connection);
        }
        return things;
    }

    //解决内部代码大量重复问题：重构
    private void releaseResource(ResultSet resultSet,PreparedStatement statement,Connection connection){
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        FileIndexDao fileIndexDao = new FileInedxDaoImpl(DataSourceFactory.dataSource());
        Thing thing = new Thing();
        thing.setName("简历.pdf");
        thing.setPath("D:\\a\\test\\简历.pdf");
        thing.setDepth(3);
        thing.setFileType(FileType.DOC);
//        fileIndexDao.insert(thing);
        Thing thing2 = new Thing();
        thing2.setName("简历2.pdf");
        thing2.setPath("D:\\a\\简历2.pdf");
        thing2.setDepth(2);
        thing2.setFileType(FileType.DOC);
//        fileIndexDao.insert(thing2);
//        List<Thing> things = fileIndexDao.search(new Condition());


        Condition condition=  new Condition();
        condition.setName("简历");
        condition.setLimit(12);
        condition.setOrderByAsc(true);
        condition.setFileType("DOC");
        List<Thing> things = fileIndexDao.search(condition);
        for (Thing i:things) {
            System.out.println(i);
        }
    }
}


