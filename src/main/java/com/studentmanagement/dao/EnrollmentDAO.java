
package com.studentmanagement.dao;

import com.studentmanagement.model.Enrollment;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnrollmentDAO {

    public boolean enrollStudent(Enrollment enrollment)
            throws SQLException {

        String sql = """
                INSERT INTO student_courses
                    (student_id, course_id, enrollment_date, grade)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            setEnrollmentParameters(statement, enrollment);

            return statement.executeUpdate() == 1;
        }
    }

    public int[] enrollStudentsBatch(Collection<Enrollment> enrollments)
            throws SQLException {

        if (enrollments == null || enrollments.isEmpty()) {
            return new int[0];
        }

        String sql = """
                INSERT INTO student_courses
                    (student_id, course_id, enrollment_date, grade)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    for (Enrollment enrollment : enrollments) {
                        setEnrollmentParameters(statement, enrollment);
                        statement.addBatch();
                    }

                    int[] results = statement.executeBatch();
                    connection.commit();

                    return results;

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

    public List<Enrollment> getAllEnrollments()
            throws SQLException {

        String sql = """
                SELECT student_id, course_id, enrollment_date, grade
                FROM student_courses
                ORDER BY enrollment_date DESC
                """;

        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Date sqlDate = resultSet.getDate("enrollment_date");

                Enrollment enrollment = new Enrollment(
                        resultSet.getInt("student_id"),
                        resultSet.getInt("course_id"),
                        sqlDate.toLocalDate(),
                        resultSet.getString("grade")
                );

                enrollments.add(enrollment);
            }
        }

        return enrollments;
    }

    private void setEnrollmentParameters(
            PreparedStatement statement,
            Enrollment enrollment) throws SQLException {

        statement.setInt(1, enrollment.getStudentId());
        statement.setInt(2, enrollment.getCourseId());
        statement.setDate(
                3, Date.valueOf(enrollment.getEnrollmentDate()));
        statement.setString(4, enrollment.getGrade());
    }
}
