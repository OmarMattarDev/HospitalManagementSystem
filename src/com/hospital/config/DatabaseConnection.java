package com.hospital.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String CONFIG_FILE = "config/database.properties.example";
    private static final Properties PROPERTIES = loadProperties();

    private DatabaseConnection() {}

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException(
                        "Database configuration file not found: " + CONFIG_FILE);
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load database configuration from " + CONFIG_FILE, e);
        }

        return properties;
    }

    public static Connection getConnection() throws SQLException {
        String url = PROPERTIES.getProperty("db.url");
        String username = PROPERTIES.getProperty("db.username");
        String password = PROPERTIES.getProperty("db.password");

        if (url == null || url.isBlank() || username == null || username.isBlank() || password == null) {
            throw new SQLException(
                    "Missing or empty database credentials in " + CONFIG_FILE);
        }

        return DriverManager.getConnection(url.trim(), username.trim(), password);
    }
}