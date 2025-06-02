# Staff Management System - LAB10

A complete CRUD application built with Spring Boot for managing staff members, departments, and projects with relationships between them.

## Prerequisites

- Java 17 or higher
- Maven 3.6+ or Gradle 7+
- Git (optional - for cloning the repository)

## How to Run the Application

### Step 1: Clone or Download the Project

If using Git:

```bash
git clone <repository-url>
cd LAB10
```

Alternatively, download and extract the ZIP file.

### Step 2: Build the Project

Using Maven:

```bash
mvn clean install
```

Using Gradle:

```bash
gradle build
```

### Step 3: Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Using Gradle:

```bash
gradle bootRun
```

Alternatively, you can run the JAR file directly: (not recommended)

```bash
java -jar target/lab09-0.0.1-SNAPSHOT.jar
```

### Step 4: Access the Application

Open your browser and navigate to:

```
http://localhost:8080
```

The application should load with the staff management interface.

## Using the Application

### Staff Management

- **View Staff**: All staff members are displayed in a table
- **Add Staff**: Fill out the form at the top of the page and click "Add Staff"
- **Edit Staff**: Click the "Edit" button next to a staff member
- **Delete Staff**: Click the "Delete" button next to a staff member

### Department and Project Views

- View all departments and their staff counts
- View all projects and their status

### Project Assignment

- When adding or editing a staff member, you can assign multiple projects
- Use the dual-list selection interface to add/remove project assignments

## Database Console

The application uses H2 in-memory database. You can access the database console at:

```
http://localhost:8080/h2-console
```

Connection details:

- JDBC URL: `jdbc:h2:mem:staffdb`
- Username: `sa`
- Password: (leave empty)

## Technical Details

- **Backend**: Spring Boot 3.x
- **Database**: H2 in-memory database
- **Frontend**: HTML, CSS, JavaScript
- **Dependencies**: Spring Web, Spring Data JPA, H2 Database

## Sample Data

The application automatically loads sample data on startup, including:

- Departments (IT, HR, Sales)
- Projects (Website Redesign, CRM Implementation, Mobile App, etc.)
- Staff members with assignments to departments and projects

You can start using the application right away without adding any data.

---

*Created for CSC3402 Lab 10*
