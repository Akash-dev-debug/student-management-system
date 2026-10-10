
USE student_management;

DROP PROCEDURE IF EXISTS GetAllStudents;

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

DELIMITER ;



DELIMITER //

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
END //

DELIMITER ;
