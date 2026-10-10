Student Management System — Setup Guide

1. Requirements

JDK 17 or later

Apache Maven

MySQL Server 8.0 or compatible

MySQL Workbench (recommended)

2. Create the Database

Start MySQL Server.

Open MySQL Workbench and connect to your MySQL instance.

Execute database/schema.sql.

Execute database/procedures.sql to create the GetAllStudents stored procedure.

3. Configure Database Access

Create a dedicated MySQL user with access to the student_management database.

Configure the local file src/main/resources/db.properties:

db.url=jdbc:mysql://localhost:3306/student_management
db.username=your_database_user
db.password=your_database_password

Replace the example values with your own credentials. Do not commit this file to GitHub.

4. Build the Project

Open a terminal in the project root and execute:

mvn clean compile

5. Run the Application

Execute:

mvn exec:java -Dexec.mainClass="com.studentmanagement.ui.Main"

The application should connect to MySQL and display the student management menu.

6. Database Backup

Run database/backup.bat from a Windows terminal. Enter the database user's password when prompted.

The backup file is saved in database/backups/.

7. Database Recovery

Use database/restore.bat with a trusted SQL backup file. Test recovery against a separate test database before restoring important data.

8. Troubleshooting

Access denied: Verify the database username, password, and privileges.

Connection refused: Check that MySQL Server is running.

Database not found: Execute database/schema.sql.

Stored procedure not found: Execute database/procedures.sql.

Build failure: Check the Maven output and verify the JDK configuration.