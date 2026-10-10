
package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public boolean addStudent(Student student) throws SQLException {
        validateStudent(student);
        return studentDAO.addStudent(student);
    }

    public Student getStudentById(int id) throws SQLException {
        validateId(id);
        return studentDAO.getStudentById(id);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public List<Student> getAllStudentsUsingProcedure()
            throws SQLException {
        return studentDAO.getAllStudentsUsingProcedure();
    }

    public List<Student> getStudentsByDepartment(String department)
            throws SQLException {

        requireText(department, "Department");

        return studentDAO.getStudentsByDepartment(department.trim());
    }

    public boolean updateStudent(Student student) throws SQLException {
        if (student == null) {
            throw new IllegalArgumentException(
                    "Student details cannot be null.");
        }

        validateId(student.getId());
        validateStudent(student);

        return studentDAO.updateStudent(student);
    }

    public boolean deleteStudent(int id) throws SQLException {
        validateId(id);
        return studentDAO.deleteStudent(id);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }
    }

    private void validateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException(
                    "Student details cannot be null.");
        }

        requireText(student.getStudentCode(), "Student code");
        requireText(student.getFirstName(), "First name");
        requireText(student.getLastName(), "Last name");
        requireText(student.getEmail(), "Email");
        requireText(student.getDepartment(), "Department");

        if (!student.getEmail().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException(
                    "Please enter a valid email address.");
        }

        if (student.getEnrollmentDate() == null) {
            throw new IllegalArgumentException(
                    "Enrollment date is required.");
        }
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty.");
        }
    }
}
