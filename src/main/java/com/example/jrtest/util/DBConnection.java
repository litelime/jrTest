package com.example.jrtest.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance = null;
    private Connection connection;

    private DBConnection (String url, String user, String pass) throws SQLException{

        this.connection = DriverManager.getConnection(url, user, pass);

    }

    public static DBConnection getInstance(String url, String user, String pass) throws SQLException{

        if (instance == null){
            synchronized (DBConnection.class){
                if(instance==null){
                    instance = new DBConnection(url, user, pass);
                }
            }
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
