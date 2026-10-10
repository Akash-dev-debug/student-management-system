
package com.studentmanagement.service;

import com.studentmanagement.dao.CourseDAO;
import com.studentmanagement.model.Course;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.SQLException;

public class CourseCrudTest {

    public static void main(String[] args) {
        CourseDAO courseDAO = new CourseDAO();

        try {
            // Add a temporary course.
            Course course = new Course(
                    0, "CRUD999", "Temporary Test Course", 3
            );

            if (!courseDAO.addCourse(course)) {
                System.out.println("Course creation failed.");
                return;
            }

            // Find the new course ID.
            Course savedCourse = courseDAO.getAllCourses()
                    .stream()
                    .filter(c -> "CRUD999".equals(c.getCourseCode()))
                    .findFirst()
                    .orElseThrow(() ->
                            new SQLException("Test course was not found."));

            // Update the course.
            savedCourse.setCourseName("Updated Test Course");
            savedCourse.setCredits(4);

            boolean updated = courseDAO.updateCourse(savedCourse);
            System.out.println("Update test: "
                    + (updated ? "PASSED" : "FAILED"));

            // Delete the temporary course.
            boolean deleted = courseDAO.deleteCourse(savedCourse.getId());
            System.out.println("Delete test: "
                    + (deleted ? "PASSED" : "FAILED"));

        } catch (SQLException | RuntimeException exception) {
            System.err.println("Course CRUD test failed: "
                    + exception.getMessage());
        } finally {
            try {
                DatabaseConnection.closePool();
            } catch (SQLException exception) {
                System.err.println("Pool cleanup failed: "
                        + exception.getMessage());
            }
        }
    }
}
