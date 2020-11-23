package com.cst8334_group_one_solitaire.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String USERNAME = "CST8334";
    private static final String PASSWORD = "@JavaProject2020";
    private static final String CONN_STRING = "jdbc:mysql://104.248.231.186/cst8334";


    public static Connection getConnection () {
        try {
            return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
}