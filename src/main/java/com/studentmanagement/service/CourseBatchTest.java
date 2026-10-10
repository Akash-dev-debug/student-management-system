
package com.studentmanagement.service;

import com.studentmanagement.dao.CourseDAO;
import com.studentmanagement.model.Course;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.SQLException;
import java.util.List;

public class CourseBatchTest {

    public static void main(String[] args) {
        CourseDAO courseDAO = new CourseDAO();

        List<Course> courses = List.of(
                new Course(0, "TEST101", "Test Course One", 3),
                new Course(0, "TEST102", "Test Course Two", 4)
        );

        try {
            int[] results = courseDAO.addCoursesBatch(courses);

            System.out.println("Batch operation completed.");
            System.out.println("Number of batch results: " + results.length);

        } catch (SQLException exception) {
            System.err.println("Batch test failed: "
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
