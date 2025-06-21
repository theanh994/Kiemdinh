# EduMana Application

This is a Spring Boot application for education management.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## How to Run

1. Clone the repository
2. Navigate to the project directory
3. Run the following command:
   ```
   mvn spring-boot:run
   ```
4. The application will start on http://localhost:8080

## Features

- Spring Boot 3.1.0
- Spring Data JPA
- H2 Database (in-memory)
- RESTful API support

## Database

The application uses H2 in-memory database. You can access the H2 console at:
http://localhost:8080/h2-console

Default credentials:

- JDBC URL: jdbc:h2:mem:edudb
- Username: sa
- Password: [empty]
