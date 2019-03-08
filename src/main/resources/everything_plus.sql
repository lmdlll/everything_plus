-- 创建数据库
-- 如果是普通的数据库建表，需要在创建一个database，但是在这里使用的是h2嵌入式数据库，在连接数据源时，就已经创建一个目录了，不需要再嵌入了
-- create database if not exists everthing_plus;
-- 创建数据库表
drop table if exists file_index;
create table if not exists file_index(
  name varchar(256) not null comment '文件名称',
  path varchar (1024) not null comment '文件路径',
  depth int not null comment '文件路径深度',
  file_type varchar(32) not null comment '文件类型'
);

