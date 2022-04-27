package io.github.ahern88.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class TranscationMyisamTest {

    public static void main(String[] args) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/transaction_test";
        String user = "root";
        String password = "ahern88";

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

            // 开启事务
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate("insert into userinfo_myisam values(null, 'ahern88', '小艾', '123456', 1);");

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
