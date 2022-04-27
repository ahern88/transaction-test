package io.github.ahern88.jdbc.isolation;

import java.sql.*;


/**
 * 幻读，脏读的理解
 * 1. 幻读 一般是针对范围查询，同一个事务中，同一个范围查询两次获得了不同的结果
 * 2. 脏读 一般是读到了未提交的数据
 */
public class RepeatableReadTest {

    public static void main(String[] args) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/transaction_test";
        String user = "root";
        String password = "ahern88";

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

            // 查看事务隔离级别
            // int TRANSACTION_NONE             = 0;
            // int TRANSACTION_READ_UNCOMMITTED = 1;
            // int TRANSACTION_READ_COMMITTED   = 2;
            // int TRANSACTION_REPEATABLE_READ  = 4;
            // int TRANSACTION_SERIALIZABLE     = 8;
            System.out.println("隔离级别: " + connection.getTransactionIsolation());
            // 默认隔离级别：TRANSACTION_REPEATABLE_READ 可重复读

            // 开启事务
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into userinfo values(null, 'ahern88', '小艾', '123456', 1);");

            ResultSet rs = statement.executeQuery("select * from userinfo");
            rs.next();
            System.out.println("id: " + rs.getString("id"));
            // 可重复读的事务隔离机制有个问题是在数据未commit之前，当前事务能查到未提交的数据
            // 测试了下，另启一个连接在未提交前查不到未提交的数据
            // 总结：可重复读的隔离机制，就是未提交前的数据只对当前连接可见

            if (true) {
                throw new RuntimeException("rollback data");
            }

            connection.commit();

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
