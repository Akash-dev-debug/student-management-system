Student Management System — Testing Report

1. Database Connection

Status: Passed

Java successfully connected to MySQL using JDBC and the configured database connection pool.

2. Student CRUD Operations

Add Student: Passed

View All Students: Passed

Search Student by ID: Passed

Update Student: Passed

Delete Student: Passed

3. Stored Procedure

Status: Passed

Created and tested the GetAllStudents stored procedure in MySQL Workbench.

Integrated the stored procedure into the Java application.

4. Database Backup

Status: Passed

Executed backup.bat.

Verified that the generated SQL backup contained table definitions and student data.

5. Database Recovery

Status: Passed

Restored the backup into the separate student_management_test database.

Verified the restored records using a SQL query.

The test database contained one student record.

6. Build Verification

The project compiled successfully using Maven during development.

7. Further Testing Required

Automated unit and integration tests.

Java transaction commit and rollback behavior.

Invalid input and duplicate-record handling.

Backup and recovery testing with additional records


## Course CRUD Testing

The Course CRUD test verifies the following operations:

1. **Create:** Inserts a temporary course into the database.
2. **Read:** Retrieves the newly created course.
3. **Update:** Changes the course name and credit value.
4. **Delete:** Removes the temporary test course.

### Run the Test

```bash
mvn exec:java -Dexec.mainClass="com.studentmanagement.service.CourseCrudTest"
```

### Expected Result

```text
Update test: PASSED
Delete test: PASSED
```

The test uses the temporary course code `CRUD999`.


## Enrollment Batch Testing

**Test class:** `EnrollmentBatchTest`

**Purpose:** Verify that multiple student-course enrollments can be inserted using JDBC batch operations and transaction management.

**Test data:**
- Student ID: 1
- Course IDs: 1 and 2

**Run command:**

```bash
mvn exec:java -Dexec.mainClass="com.studentmanagement.service.EnrollmentBatchTest"
```

**Expected result:**
- Two enrollment records are inserted.
- The batch result contains two entries.
- The transaction is committed when the operation succeeds.
- The transaction is rolled back if a SQL error occurs.

