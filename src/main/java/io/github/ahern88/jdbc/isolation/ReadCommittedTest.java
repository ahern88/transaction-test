package io.github.ahern88.jdbc.isolation;

import java.sql.*;


public class ReadCommittedTest {

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
            /**
             * A constant indicating that
             * dirty reads are prevented; non-repeatable reads and phantom
             * reads can occur.  This level only prohibits a transaction
             * from reading a row with uncommitted changes in it.
             */
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println("隔离级别: " + connection.getTransactionIsolation());
            // 默认隔离级别：TRANSACTION_REPEATABLE_READ 可重复读

            // 开启事务
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into userinfo values(null, 'ahern88', '小艾', '123456', 1);");

            // 执行到这里的时候debug，使用sql客户端连接查看是否能看到未提交数据
            // set transaction isolation level READ COMMITTED; select * from userinfo;
            // 读已提交的隔离机制只能查到已commit后的数据

            ResultSet rs = statement.executeQuery("select * from userinfo");
            if (rs.next()) {
                System.out.println("id: " + rs.getString("id"));
            }

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
