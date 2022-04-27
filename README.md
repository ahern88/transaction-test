# 事务学习

## 创建数据库
```sql
CREATE DATABASE `transaction_test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */

-- 默认表为 innodb 存储引擎
-- auto-generated definition
create table userinfo
(
    id       int auto_increment   primary key,
    username varchar(32)          not null,
    nickname varchar(32)          not null,
    password varchar(32)          not null,
    status   tinyint(1) default 1 not null
) comment '用户信息表';

create table userinfo_myisam
(
    id       int auto_increment   primary key,
    username varchar(32)          not null,
    nickname varchar(32)          not null,
    password varchar(32)          not null,
    status   tinyint(1) default 1 not null
) engine=MyISAM comment '用户信息表-MYISAM引擎';
```
查看数据库的存储引擎
```sql
show variables like '%storage_engine%';
-- default_storage_engine,InnoDB
-- default_tmp_storage_engine,InnoDB
-- disabled_storage_engines,""
-- internal_tmp_mem_storage_engine,TempTable

-- 查询innodb引擎状态
show engine innodb status;
```
设置命令行的隔离级别(SERIALIZABLE | REPEATABLE READ | READ COMMITTED | READ UNCOMMITTED)
```sql
set transaction isolation level READ UNCOMMITTED; 
```

查看默认事务隔离级别
```sql
show variables like 'transaction_isolation';
-- transaction_isolation,REPEATABLE-READ
```