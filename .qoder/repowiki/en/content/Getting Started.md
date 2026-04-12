# Getting Started

<cite>
**Referenced Files in This Document**
- [pom.xml](file://pom.xml)
- [application.properties](file://src/main/resources/application.properties)
- [InterviewRecordApplication.java](file://src/main/java/com/wu/InterviewRecordApplication.java)
- [SecurityConfig.java](file://src/main/java/com/wu/config/SecurityConfig.java)
- [UserServiceImpl.java](file://src/main/java/com/wu/service/impl/UserServiceImpl.java)
- [UserController.java](file://src/main/java/com/wu/web/UserController.java)
- [HomeController.java](file://src/main/java/com/wu/web/HomeController.java)
- [User.java](file://src/main/java/com/wu/model/User.java)
- [SystemLog.java](file://src/main/java/com/wu/model/SystemLog.java)
- [LoggingAspect.java](file://src/main/java/com/wu/config/LoggingAspect.java)
</cite>

## Table of Contents
1. [Introduction](#introduction)
2. [Prerequisites](#prerequisites)
3. [Installation Steps](#installation-steps)
4. [Database Setup and Configuration](#database-setup-and-configuration)
5. [Environment Variables](#environment-variables)
6. [Initial Application Launch](#initial-application-launch)
7. [Configuration Options](#configuration-options)
8. [Verification](#verification)
9. [Default Admin Credentials and Initial Setup](#default-admin-credentials-and-initial-setup)
10. [Troubleshooting](#troubleshooting)
11. [Next Steps](#next-steps)

## Introduction
This guide helps you install and run the Interview Record Management System locally. It covers prerequisites, database setup, configuration, launching the application, verifying the installation, and understanding default credentials and initial setup.

## Prerequisites
- Java 8 or higher
- Apache Maven
- MySQL server

These are confirmed by the project’s build configuration and dependencies.

**Section sources**
- [pom.xml:20-23](file://pom.xml#L20-L23)
- [pom.xml:25-70](file://pom.xml#L25-L70)

## Installation Steps
1. Clone or download the repository to your machine.
2. Open a terminal in the project root directory.
3. Build the project using Maven:
   - Command: mvn clean install
4. After a successful build, the application JAR is produced under the target directory.

Notes:
- The Spring Boot Maven plugin is configured for packaging and running the application.
- The project uses Thymeleaf for templating and Spring MVC for web routing.

**Section sources**
- [pom.xml:72-79](file://pom.xml#L72-L79)
- [pom.xml:36-47](file://pom.xml#L36-L47)

## Database Setup and Configuration
- The application connects to MySQL via JDBC.
- Default connection settings are embedded in application.properties:
  - Driver class name
  - JDBC URL (host, port, database name)
  - Username and password
- Hibernate DDL behavior is set to update, which will create or modify tables on startup.

Important:
- Ensure MySQL is installed and running.
- Create the target database referenced by the JDBC URL before starting the app.
- Adjust the JDBC URL, username, and password in application.properties to match your local MySQL setup.

**Section sources**
- [application.properties:3-6](file://src/main/resources/application.properties#L3-L6)
- [application.properties:9](file://src/main/resources/application.properties#L9)

## Environment Variables
- No explicit environment variables are required by the current configuration.
- If you deploy outside the default environment, you can override properties using standard Spring Boot mechanisms (e.g., system properties, environment variables, or external application.properties).

[No sources needed since this section provides general guidance]

## Initial Application Launch
- From the project root, run the Spring Boot application using Maven:
  - Command: mvn spring-boot:run
- Alternatively, package and run the JAR:
  - Package: mvn package
  - Run: java -jar target/interview-record-system-1.0.0.jar
- The application listens on port 8080 by default.

What happens at startup:
- Spring Boot initializes the web stack, JPA/Hibernate, security, and Thymeleaf.
- The admin user is created automatically if not present.

**Section sources**
- [InterviewRecordApplication.java:8-10](file://src/main/java/com/wu/InterviewRecordApplication.java#L8-L10)
- [application.properties:1](file://src/main/resources/application.properties#L1)
- [UserServiceImpl.java:28-37](file://src/main/java/com/wu/service/impl/UserServiceImpl.java#L28-L37)

## Configuration Options
Key application.properties settings:
- Server port: controls the HTTP port the app binds to.
- Database connection:
  - Driver class name
  - JDBC URL (includes host, port, database name, and timezone)
  - Username and password
- JPA/Hibernate:
  - ddl-auto: controls schema generation/update behavior
  - show-sql: toggles SQL logging
  - format_sql: enables formatted SQL output
- Thymeleaf:
  - cache disabled for development to reflect template changes immediately

Security-related behavior:
- Spring Security is enabled and configured to protect endpoints.
- Login page, logout, and CSRF settings are defined in the security configuration.

Logging:
- An aspect logs system operations for specific save/delete actions.
- Logging preferences can be adjusted via Spring Boot logging configuration.

**Section sources**
- [application.properties:1-14](file://src/main/resources/application.properties#L1-L14)
- [SecurityConfig.java:37-58](file://src/main/java/com/wu/config/SecurityConfig.java#L37-L58)
- [LoggingAspect.java:28-62](file://src/main/java/com/wu/config/LoggingAspect.java#L28-L62)

## Verification
After starting the application:
- Open a browser and navigate to http://localhost:8080
- You should be redirected to the records list after login.
- Try accessing:
  - Login page: http://localhost:8080/login
  - Registration page: http://localhost:8080/register
  - Records list: http://localhost:8080/records
  - Study list: http://localhost:8080/study
  - Logs list (admin-only): http://localhost:8080/logs

If you see template pages rendered and can navigate without immediate errors, the installation is successful.

**Section sources**
- [HomeController.java:9-12](file://src/main/java/com/wu/web/HomeController.java#L9-L12)
- [SecurityConfig.java:40-44](file://src/main/java/com/wu/config/SecurityConfig.java#L40-L44)

## Default Admin Credentials and Initial Setup
- On first startup, the system creates an admin user automatically if one does not exist.
- Default admin account:
  - Username: admin
  - Password: admin
- To log in:
  - Go to http://localhost:8080/login
  - Enter the admin credentials
  - You will be redirected to the records list

Notes:
- The admin user is granted ROLE_ADMIN, which allows access to admin-protected endpoints (such as logs).
- Non-admin users can register via the registration page and will have ROLE_USER.

**Section sources**
- [UserServiceImpl.java:72-85](file://src/main/java/com/wu/service/impl/UserServiceImpl.java#L72-L85)
- [SecurityConfig.java:41](file://src/main/java/com/wu/config/SecurityConfig.java#L41)
- [UserController.java:31-34](file://src/main/java/com/wu/web/UserController.java#L31-L34)

## Troubleshooting
Common issues and resolutions:
- Database connectivity errors:
  - Verify MySQL is running and the database exists.
  - Confirm the JDBC URL, username, and password in application.properties match your setup.
- Port conflicts:
  - Change server.port in application.properties if 8080 is in use.
- Template rendering issues:
  - Ensure Thymeleaf cache is disabled during development (as configured) so changes take effect.
- Security exceptions:
  - Ensure you are logged in when accessing protected endpoints.
  - Admin-protected endpoints require ROLE_ADMIN.

Operational checks:
- Confirm the admin user exists and can log in.
- Review logs for initialization messages related to admin creation.

**Section sources**
- [application.properties:1-14](file://src/main/resources/application.properties#L1-L14)
- [UserServiceImpl.java:72-85](file://src/main/java/com/wu/service/impl/UserServiceImpl.java#L72-L85)
- [SecurityConfig.java:40-44](file://src/main/java/com/wu/config/SecurityConfig.java#L40-L44)

## Next Steps
- Explore the application UI:
  - Manage interview records
  - Manage study records
  - View system logs (admin)
- Customize configuration:
  - Adjust database credentials and JPA settings
  - Tune logging and security policies
- Extend functionality:
  - Add new endpoints and controllers
  - Integrate additional persistence or messaging layers

[No sources needed since this section provides general guidance]