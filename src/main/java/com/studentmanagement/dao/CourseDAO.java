
package com.studentmanagement.dao;

import com.studentmanagement.model.Course;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CourseDAO {

    public boolean addCourse(Course course) throws SQLException {
        String sql = """
                INSERT INTO courses (course_code, course_name, credits)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, course.getCourseCode());
            statement.setString(2, course.getCourseName());
            statement.setInt(3, course.getCredits());

            return statement.executeUpdate() == 1;
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        String sql = """
                SELECT id, course_code, course_name, credits
                FROM courses
                ORDER BY id
                """;

        List<Course> courses = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                courses.add(new Course(
                        resultSet.getInt("id"),
                        resultSet.getString("course_code"),
                        resultSet.getString("course_name"),
                        resultSet.getInt("credits")
                ));
            }
        }

        return courses;
    }

    public boolean updateCourse(Course course) throws SQLException {
        String sql = """
                UPDATE courses
                SET course_code = ?, course_name = ?, credits = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, course.getCourseCode());
            statement.setString(2, course.getCourseName());
            statement.setInt(3, course.getCredits());
            statement.setInt(4, course.getId());

            return statement.executeUpdate() == 1;
        }
    }

    public boolean deleteCourse(int courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);
            return statement.executeUpdate() == 1;
        }
    }

    public int[] addCoursesBatch(Collection<Course> courses)
            throws SQLException {

        if (courses == null || courses.isEmpty()) {
            return new int[0];
        }

        String sql = """
                INSERT INTO courses (course_code, course_name, credits)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    for (Course course : courses) {
                        statement.setString(1, course.getCourseCode());
                        statement.setString(2, course.getCourseName());
                        statement.setInt(3, course.getCredits());
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
}
