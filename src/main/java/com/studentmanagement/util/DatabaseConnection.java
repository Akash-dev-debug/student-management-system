
package com.studentmanagement.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final BasicDataSource DATA_SOURCE = createDataSource();

    private DatabaseConnection() {
    }

    private static BasicDataSource createDataSource() {
        Properties properties = new Properties();

        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "db.properties was not found in src/main/resources.");
            }

            properties.load(input);

        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password", ""));

        dataSource.setInitialSize(2);
        dataSource.setMaxTotal(10);
        dataSource.setMaxIdle(5);
        dataSource.setMinIdle(2);
        dataSource.setMaxWaitMillis(5000);

        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static void closePool() throws SQLException {
        DATA_SOURCE.close();
    }
}
