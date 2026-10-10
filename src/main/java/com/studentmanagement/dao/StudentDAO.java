
package com.studentmanagement.dao;

import com.studentmanagement.model.Student;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean addStudent(Student student) throws SQLException {
        String sql = """
                INSERT INTO students
                (student_code, first_name, last_name, date_of_birth,
                 email, phone, department, enrollment_date)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getStudentCode());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            setDateOrNull(statement, 4, student.getDateOfBirth());
            statement.setString(5, student.getEmail());
            statement.setString(6, student.getPhone());
            statement.setString(7, student.getDepartment());
            statement.setDate(8, Date.valueOf(student.getEnrollmentDate()));

            if (statement.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    student.setId(keys.getInt(1));
                }
            }

            return true;
        }
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapStudent(resultSet);
                }
            }
        }

        return null;
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT * FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapStudent(resultSet));
            }
        }

        return students;
    }

    public List<Student> getAllStudentsUsingProcedure() throws SQLException {
        String sql = "{CALL GetAllStudents()}";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapStudent(resultSet));
            }
        }

        return students;
    }

    public boolean updateStudent(Student student) throws SQLException {
        String sql = """
                UPDATE students
                SET student_code = ?, first_name = ?, last_name = ?,
                    date_of_birth = ?, email = ?, phone = ?,
                    department = ?, enrollment_date = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getStudentCode());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            setDateOrNull(statement, 4, student.getDateOfBirth());
            statement.setString(5, student.getEmail());
            statement.setString(6, student.getPhone());
            statement.setString(7, student.getDepartment());
            statement.setDate(8, Date.valueOf(student.getEnrollmentDate()));
            statement.setInt(9, student.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private void setDateOrNull(
            PreparedStatement statement,
            int parameterIndex,
            java.time.LocalDate date) throws SQLException {

        if (date == null) {
            statement.setNull(parameterIndex, Types.DATE);
        } else {
            statement.setDate(parameterIndex, Date.valueOf(date));
        }
    }

    private Student mapStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student();

        student.setId(resultSet.getInt("id"));
        student.setStudentCode(resultSet.getString("student_code"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));

        Date dateOfBirth = resultSet.getDate("date_of_birth");
        if (dateOfBirth != null) {
            student.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        student.setEmail(resultSet.getString("email"));
        student.setPhone(resultSet.getString("phone"));
        student.setDepartment(resultSet.getString("department"));

        Date enrollmentDate = resultSet.getDate("enrollment_date");
        if (enrollmentDate != null) {
            student.setEnrollmentDate(enrollmentDate.toLocalDate());
        }

        return student;
    }
}
