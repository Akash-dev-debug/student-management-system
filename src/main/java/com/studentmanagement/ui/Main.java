
package com.studentmanagement.ui;

import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Student Management System");
        System.out.println("-------------------------");

        try {
            try (Connection connection =
                         DatabaseConnection.getConnection()) {

                if (connection.isValid(5)) {
                    System.out.println("Database connection successful!");
                    System.out.println("Connected to: "
                            + connection.getCatalog());

                    StudentMenu menu = new StudentMenu();
                    menu.start();
                } else {
                    System.out.println("Database connection is invalid.");
                }
            }

        } catch (SQLException | RuntimeException exception) {
            System.err.println("Application error: "
                    + exception.getMessage());

        } finally {
            try {
                DatabaseConnection.closePool();
            } catch (SQLException exception) {
                System.err.println(
                        "Could not close connection pool: "
                                + exception.getMessage()
                );
            }
        }
    }
}
