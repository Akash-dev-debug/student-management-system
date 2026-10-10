
package com.studentmanagement.service;

import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionService {

    public void executeTransaction(String firstSql, String secondSql)
            throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(firstSql);
                    statement.executeUpdate(secondSql);
                }

                connection.commit();

            } catch (SQLException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }

                throw exception;

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }
}
