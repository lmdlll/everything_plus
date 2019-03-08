package com.lmd.core.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.lmd.config.everythingConfig;


import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



//数据源操作不需要反复操作，所以选择：工厂设计模式，
//初始化好的数据源不能被用户随便实例化，所以选择：单例模式
//此类应该完成的工作： 数据源的初始化 + 初始化脚本的执行

//数据源操作
public final class DataSourceFactory {
    /**
     * 数据源（单例）
     */
//druid：java最好的数据库连接池，druid能够提供强大的监控和扩展功能
    private static volatile DruidDataSource dataSource;
    private DataSourceFactory(){}


    public static DataSource dataSource() {
        if (dataSource == null) {
            synchronized(DataSourceFactory.class) {
                if (dataSource == null) {
                    //实例化
                    dataSource = new DruidDataSource();
                    //JDBC  driver class
                    dataSource.setDriverClassName("org.h2.Driver");
                    //url, username, password
                    //采用的是H2的嵌入式数据库，数据库以本地文件的方式存储，只需要提供url接口
                    //JDBC规范中关于MySQL jdbc:mysql://ip:port/databaseName
                    //JDBC规范中关于H2 jdbc:h2:filepath ->存储到本地文件
                    //JDBC规范中关于H2 jdbc:h2:~/filepath ->存储到当前用户的home目录
                    //JDBC规范中关于H2 jdbc:h2://ip:port/databaseName ->存储到服务器
                    dataSource.setUrl("jdbc:h2:" + everythingConfig.getInstance().getH2IndexPath());

                    //Druid数据库连接池的可配置参数
                    //https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
                    //第一种
                    dataSource.setValidationQuery("select now()");
//                    第二种
//                    dataSource.setTestWhileIdle(false);
                }
            }
        }
        return dataSource;
    }


    //初始化数据库
    public static void initDatabase(){
        //1.获取数据源
        DataSource dataSource = DataSourceFactory.dataSource();
        //2.获取SQL语句
        //不采取读取绝对路径文件
//E:\idealU\EveryThing\src\main\resources\everything_plus.sql ：这是一个绝对路径，如果程序发送给别人，路径会发生变化。不采用
        //采取读取classpath路径下的文件
        //try-with-resources
        try(InputStream in = DataSourceFactory.class.getClassLoader().getResourceAsStream("everything_plus.sql")){
            if(in==null){
                throw new RuntimeException("Not read init database script please check it");
            }
            StringBuilder sqlBulider = new StringBuilder();
            try( BufferedReader reader = new BufferedReader(new InputStreamReader(in));){
                String line = null;
                while( (line=reader.readLine()) != null){
                    if(!line.startsWith("--")){
                        sqlBulider.append(line);
                    }
                }
            }
            //3，获取数据库连接和名称执行SQL
            //把string变为执行的sql语句
            String sql = sqlBulider.toString();
            //JDBC
            //3.1 获取数据库的连接
            Connection connection = dataSource.getConnection();
            //3.2 创建命令
            PreparedStatement statement = connection.prepareStatement(sql);
            //3.3 执行SQL语句
            statement.execute();
            statement.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//        DataSource dataSource = DataSourceFactory.dataSource();
//        System.out.println(dataSource);
//
////        String workDir =  System.getProperty("user.dir");
////        System.out.println(workDir);
//        DataSourceFactory.initDatabase();
//    }
}
