
package com.studentmanagement.service;

import com.studentmanagement.dao.EnrollmentDAO;
import com.studentmanagement.model.Enrollment;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EnrollmentBatchTest {

    public static void main(String[] args) {
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        List<Enrollment> enrollments = List.of(
                new Enrollment(1, 1, LocalDate.now(), null),
                new Enrollment(1, 2, LocalDate.now(), null)
        );

        try {
            int[] results =
                    enrollmentDAO.enrollStudentsBatch(enrollments);

            System.out.println("Batch enrollment completed.");
            System.out.println(
                    "Number of batch results: " + results.length);

        } catch (SQLException exception) {
            System.err.println(
                    "Batch enrollment failed: " + exception.getMessage());

        } finally {
            try {
                DatabaseConnection.closePool();
            } catch (SQLException exception) {
                System.err.println(
                        "Pool cleanup failed: " + exception.getMessage());
            }
        }
    }
}
