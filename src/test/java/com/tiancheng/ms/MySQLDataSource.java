package com.tiancheng.ms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDataSource {

    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL =  "jdbc:mysql://127.0.0.1:3306/tiancheng?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&autoReconnect=true&nullCatalogMeansCurrent=true&&useSSL=false&serverTimezone=Asia/Shanghai";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "admin";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
            // 注册 JDBC 驱动
        Class.forName(JDBC_DRIVER);

        // 打开链接
        System.out.println("连接数据库...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }
}