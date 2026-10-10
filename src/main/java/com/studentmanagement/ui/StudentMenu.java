
package com.studentmanagement.ui;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {

    private final StudentService studentService = new StudentService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> viewAllStudents();
                    case "3" -> searchStudent();
                    case "4" -> updateStudent();
                    case "5" -> deleteStudent();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (SQLException exception) {
                System.out.println("Database error: "
                        + exception.getMessage());
            } catch (IllegalArgumentException
                     | DateTimeParseException exception) {
                System.out.println("Invalid input: "
                        + exception.getMessage());
            }
        }

        System.out.println("Application closed.");
    }

    private void printMenu() {
        System.out.println("\n=== Student Management System ===");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students (Stored Procedure)");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void addStudent() throws SQLException {
        System.out.println("\n--- Add Student ---");

        Student student = readStudentDetails();

        if (studentService.addStudent(student)) {
            System.out.println("Student added successfully. ID: "
                    + student.getId());
        } else {
            System.out.println("Student could not be added.");
        }
    }

    private void viewAllStudents() throws SQLException {
        List<Student> students =
                studentService.getAllStudentsUsingProcedure();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n--- Student List ---");

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void searchStudent() throws SQLException {
        int id = readPositiveId("Enter student ID: ");
        Student student = studentService.getStudentById(id);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    private void updateStudent() throws SQLException {
        int id = readPositiveId("Enter student ID to update: ");
        Student existingStudent = studentService.getStudentById(id);

        if (existingStudent == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Enter the updated student details:");
        Student updatedStudent = readStudentDetails();
        updatedStudent.setId(id);

        if (studentService.updateStudent(updatedStudent)) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Student was not updated.");
        }
    }

    private void deleteStudent() throws SQLException {
        int id = readPositiveId("Enter student ID to delete: ");

        System.out.print("Confirm deletion? (yes/no): ");
        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        if (studentService.deleteStudent(id)) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private Student readStudentDetails() {
        System.out.print("Student code: ");
        String studentCode = scanner.nextLine().trim();

        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Date of birth (YYYY-MM-DD, optional): ");
        String dateInput = scanner.nextLine().trim();
        LocalDate dateOfBirth = dateInput.isEmpty()
                ? null : LocalDate.parse(dateInput);

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine().trim();

        System.out.print("Department: ");
        String department = scanner.nextLine().trim();

        System.out.print("Enrollment date (YYYY-MM-DD): ");
        LocalDate enrollmentDate =
                LocalDate.parse(scanner.nextLine().trim());

        return new Student(
                studentCode,
                firstName,
                lastName,
                dateOfBirth,
                email,
                phone,
                department,
                enrollmentDate
        );
    }

    private int readPositiveId(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        try {
            int id = Integer.parseInt(input);

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "ID must be greater than zero.");
            }

            return id;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Please enter a valid numeric ID.");
        }
    }
}
