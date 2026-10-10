
USE student_management;

DELIMITER $$

CREATE PROCEDURE GetAllStudents()
BEGIN
    SELECT
        id,
        student_code,
        first_name,
        last_name,
        email,
        department,
        enrollment_date
    FROM students
    ORDER BY id;
END $$

CREATE PROCEDURE GetStudentsByDepartment(
    IN p_department VARCHAR(100)
)
BEGIN
    SELECT
        id,
        student_code,
        first_name,
        last_name,
        email,
        department
    FROM students
    WHERE department = p_department
    ORDER BY last_name, first_name;
END $$

DELIMITER ;
