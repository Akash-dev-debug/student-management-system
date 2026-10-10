
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
