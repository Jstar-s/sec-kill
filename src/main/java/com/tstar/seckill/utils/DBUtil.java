package com.tstar.seckill.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具
 */
@Component
public class DBUtil {

    @Autowired
    private MysqlMsg mysqlMsg;

    /**
     * 获取数据库连接对象
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public  Connection getConn() throws ClassNotFoundException, SQLException {
        String url = mysqlMsg.getUrl();
        String username = mysqlMsg.getUsername();
        String password = mysqlMsg.getPassword();
        String driver = mysqlMsg.getDrive();
        Class.forName(driver);// 加载驱动
//        System.out.println(url +  driver);
//        System.out.println("password" + password);
        return DriverManager.getConnection(url, username, password);
    }

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        Connection conn = getConn();
//        System.out.println(conn);
//    }
}
