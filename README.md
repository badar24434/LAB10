# Staff Management System - Spring Boot CRUD Application

This is a simple yet comprehensive Staff Management System built using Spring Boot. It provides a complete CRUD (Create, Read, Update, Delete) interface for managing staff members with a clean, responsive UI.

## Features

- Complete CRUD operations for staff management
- Modern, responsive UI with gradient design
- Client-side and server-side validation
- RESTful API endpoints
- In-memory H2 database with console access
- Form validation with user feedback
- Loading indicators and success/error messages

## Prerequisites

- Java 17 or higher
- Maven (or use the included Maven wrapper)
- An IDE like IntelliJ IDEA, Eclipse, or VS Code (optional)

## How to Run the Application

### Using Maven Wrapper

1. Open a terminal/command prompt
2. Navigate to the project root directory (`LAB09`)
3. Run the application using the Maven wrapper:

   ```
   # On Windows
   mvnw.cmd spring-boot:run

   # On Linux/Mac
   ./mvnw spring-boot:run
   ```

### Using Maven (if installed)

1. Open a terminal/command prompt
2. Navigate to the project root directory (`LAB09`)
3. Run the application using Maven:

   ```
   mvn spring-boot:run
   ```

### Using an IDE

1. Import the project into your IDE
2. Run `com.csc3402.lab.lab09.Lab09Application` as a Java application

## Accessing the Application

- Web Interface: [http://localhost:8080](http://localhost:8080)
- H2 Database Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:staffdb`
  - Username: `sa`
  - Password: (leave empty)

## Project Structure and File Descriptions

### Java Source Files

- **Lab09Application.java**: Main Spring Boot application class that serves as the entry point
- **model/Staff.java**: Entity class representing a staff member with validation annotations
- **repository/StaffRepository.java**: JPA repository interface for database operations
- **controller/WebController.java**: MVC controller for handling web page requests
- **controller/ApiController.java**: REST controller exposing API endpoints
- **controller/StaffController.java**: Traditional MVC controller for staff operations
- **config/DataLoader.java**: Component that loads sample data on application startup

### Templates and Static Resources

- **templates/crud.html**: Main HTML page for the CRUD application
- **static/js/script.js**: JavaScript file containing all client-side logic
- **static/css/styles.css**: CSS file containing all styling for the application

### Configuration Files

- **application.properties**: Configuration file for Spring Boot properties
- **pom.xml**: Maven project configuration file

### API Endpoints

| Method | URL                   | Description                               |
|--------|------------------------|-------------------------------------------|
| GET    | /api/staffs           | Get all staff members                      |
| GET    | /api/staffs/{id}      | Get a specific staff member by ID          |
| POST   | /api/staffs           | Create a new staff member                  |
| PUT    | /api/staffs/{id}      | Update an existing staff member            |
| DELETE | /api/staffs/{id}      | Delete a staff member                      |

## Application Flow

1. The application starts with preloaded sample data
2. Users can view all staff members in a table
3. Users can add new staff members via the form at the top
4. Each staff record in the table has edit and delete buttons
5. Clicking edit loads the staff data into the form for updating
6. Delete button removes the staff member after confirmation
7. Validation is performed both client-side and server-side

## Implementation Details

### Frontend

- Pure HTML, CSS, and JavaScript (no frameworks)
- Responsive design works on desktop and mobile devices
- Modern UI with gradient backgrounds and subtle animations
- Client-side form validation with visual feedback

### Backend

- Spring Boot 3.5.0 for application framework
- Spring Data JPA for database operations
- H2 in-memory database for data storage
- Bean validation for entity validation
- RESTful API design principles

## Error Handling

- Client-side validation prevents invalid submissions
- Server-side validation ensures data integrity
- Error messages are displayed to the user
- Success messages confirm operations

## Notes

- This is a development/demonstration application - use a persistent database for production
- The application is designed to be simple but functional
- The H2 database is reset on application restart

## Troubleshooting

- If the application fails to start, ensure no other applications are using port 8080
- Check the console output for detailed error messages
- Ensure Java 17 or higher is installed and configured

---

*Created for CSC3402 Lab 09*
