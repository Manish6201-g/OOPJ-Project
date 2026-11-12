package com.campusconnect.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() { }

    public static Connection getConnection() throws SQLException {
        String url = env("CAMPUS_DB_URL", "jdbc:mysql://localhost:3306/campus_lost_found?serverTimezone=UTC");
        String user = env("CAMPUS_DB_USER", "campus_app");
        String password = System.getenv("CAMPUS_DB_PASSWORD");
        if (password == null || password.isBlank()) {
            throw new SQLException("CAMPUS_DB_PASSWORD is not set. Set your MySQL password in the terminal before running the app.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
