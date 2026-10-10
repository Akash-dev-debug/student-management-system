Student Management System

A Java-based Student Management System built using JDBC, MySQL, and Maven.

Features

Add new student records

View all students

Search students by ID

Update student details

Delete student records

Validate student input

Use JDBC PreparedStatement for database operations

Reuse database connections through a connection pool

Execute stored procedures

Create and restore database backups

Technologies Used

Java 17 language target

MySQL 8.0

JDBC

Maven

Apache Commons DBCP2

Visual Studio Code

Project Structure

StudentManagementSystem/
├── database/
│   ├── schema.sql
│   ├── procedures.sql
│   ├── backup.bat
│   └── restore.bat
├── docs/
├── src/
│   └── main/
│       ├── java/com/studentmanagement/
│       │   ├── config/
│       │   ├── model/
│       │   ├── dao/
│       │   ├── service/
│       │   ├── util/
│       │   └── ui/
│       └── resources/
│           └── db.properties
├── .gitignore
├── pom.xml
└── README.md

Prerequisites

JDK 17 or later

Maven

MySQL Server 8.0 or compatible

Database Setup

Start MySQL Server.

Open MySQL Workbench.

Execute database/schema.sql.

Create a dedicated database user with access to student_management.

Configure the local src/main/resources/db.properties file.

Example configuration:

db.url=jdbc:mysql://localhost:3306/student_management
db.username=your_database_user
db.password=your_database_password

Do not commit database credentials to version control.

Build and Run

Compile the project:

mvn clean compile

Run the application:

mvn exec:java -Dexec.mainClass="com.studentmanagement.ui.Main"

Stored Procedures

Execute database/procedures.sql in MySQL Workbench to create the GetAllStudents stored procedure.

Backup and Recovery

Run database/backup.bat to create a database backup.

Run database/restore.bat with a trusted backup file path to restore data.

Review the scripts and test recovery using a separate test database before restoring important data.

Security

Database credentials are stored in a local configuration file excluded by .gitignore.

Student queries use parameterized statements.

Database connections and query resources are closed using try-with-resources.

Future Improvements

Add automated unit and integration tests.

Improve input error handling.

Add course and enrollment management.

Add a graphical user interface.

License

This project was developed for educational purposes.