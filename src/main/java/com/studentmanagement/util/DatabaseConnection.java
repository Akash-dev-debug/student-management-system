
package com.studentmanagement.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

public final class DatabaseConnection {

    private static final BasicDataSource DATA_SOURCE = createDataSource();

    private DatabaseConnection() {
        // Prevent object creation.
    }

    private static BasicDataSource createDataSource() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "db.properties file was not found."
                );
            }

            properties.load(input);

        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || url.isBlank()
                || username == null || username.isBlank()
                || password == null) {
            throw new IllegalStateException(
                    "Please check the database settings in db.properties."
            );
        }

        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Connection pool configuration
        dataSource.setInitialSize(2);
        dataSource.setMaxTotal(10);
        dataSource.setMaxIdle(5);
        dataSource.setMinIdle(2);
        dataSource.setMaxWait(Duration.ofMillis(5000));

        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static void closePool() throws SQLException {
        DATA_SOURCE.close();
    }
}
