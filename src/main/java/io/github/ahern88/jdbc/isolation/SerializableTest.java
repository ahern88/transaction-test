package io.github.ahern88.jdbc.isolation;

import java.sql.*;


public class SerializableTest {

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
            //  Transaction isolation level NONE not supported by MySQL，MySQL不支持NONE的隔离机制
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            System.out.println("隔离级别: " + connection.getTransactionIsolation());
            // 默认隔离级别：TRANSACTION_REPEATABLE_READ 可重复读

            // 开启事务
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            System.out.println("1. ========================");
            ResultSet rs = statement.executeQuery("select * from userinfo");

            System.out.println("2. ========================");

            statement.executeUpdate("insert into userinfo values(null, 'ahern88', '小艾', '123456', 1);");

            System.out.println("3. ========================");

            rs = statement.executeQuery("select * from userinfo");

            System.out.println("4. ========================");

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
