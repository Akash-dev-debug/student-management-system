
package com.studentmanagement.service;

import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionTest {

    public static void main(String[] args) throws SQLException {
        TransactionService transactionService = new TransactionService();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "DELETE FROM transaction_test WHERE id = 2"
            );
        }

        try {
            transactionService.executeTransaction(
                    "INSERT INTO transaction_test (id, test_value) VALUES (?, ?)",
                    new Object[]{2, "First insert"},
                    "INSERT INTO transaction_test (id, test_value) VALUES (?, ?)",
                    new Object[]{1, "Duplicate ID"}
            );

            System.out.println("Unexpected: transaction succeeded.");

        } catch (SQLException exception) {
            System.out.println("Expected transaction failure: "
                    + exception.getMessage());
        }

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT COUNT(*) FROM transaction_test WHERE id = 2")) {

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                System.out.println("Rollback test passed.");
            } else {
                System.out.println("Rollback test failed.");
            }
        } finally {
            DatabaseConnection.closePool();
        }
    }
}
