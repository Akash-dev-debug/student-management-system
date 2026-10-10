
package com.studentmanagement.service;

import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionService {

    public void executeTransaction(
            String firstSql,
            Object[] firstParameters,
            String secondSql,
            Object[] secondParameters) throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                try (PreparedStatement firstStatement =
                             connection.prepareStatement(firstSql);
                     PreparedStatement secondStatement =
                             connection.prepareStatement(secondSql)) {

                    setParameters(firstStatement, firstParameters);
                    firstStatement.executeUpdate();

                    setParameters(secondStatement, secondParameters);
                    secondStatement.executeUpdate();

                    connection.commit();

                } catch (SQLException exception) {
                    try {
                        connection.rollback();
                    } catch (SQLException rollbackException) {
                        exception.addSuppressed(rollbackException);
                    }

                    throw exception;
                }

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        }
    }

    private void setParameters(
            PreparedStatement statement,
            Object[] parameters) throws SQLException {

        if (parameters == null) {
            return;
        }

        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
