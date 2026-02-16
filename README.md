# Student Management System

A comprehensive role-based web application for educational institutions to manage students, courses, and enrollments. Built with Spring Boot, this system provides separate interfaces for teachers and students with appropriate access controls.

## Features

### For Teachers
- **Student Management**: Create, view, edit, and delete student accounts
- **Course Management**: Add and manage courses
- **Enrollment Control**: Enroll students in courses and manage their course selections
- **Dashboard**: View statistics on total students and courses

### For Students
- **Profile Management**: View and update personal information
- **Course Overview**: View enrolled courses in real-time
- **Dashboard**: Personalized interface showing profile details and course list

## Technology Stack

| Component | Technology |
|-----------|------------|
| Backend | Spring Boot 3.2.3, Spring Security, Spring Data JPA |
| Frontend | Thymeleaf, Bootstrap 5 |
| Database | PostgreSQL |
| Build Tool | Maven |
| Containerization | Docker |
| CI/CD | GitHub Actions |

## Project Structure

```
src/
├── main/
│   ├── java/com/shuvocse21/StudentManagementApp/
│   │   ├── config/         # Security and application configuration
│   │   ├── controller/      # MVC controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data access layer
│   │   └── service/        # Business logic
│   └── resources/
│       ├── templates/       # Thymeleaf HTML templates
│       └── application.properties
└── test/                    # Unit and integration tests
```

## Quick Start

### Prerequisites
- Java 21
- Docker and Docker Compose (recommended)
- PostgreSQL (for local development)

### Using Docker (Recommended)
```bash
# Clone the repository
git clone https://github.com/yourusername/StudentManagementApp.git
cd StudentManagementApp

# Start the application with PostgreSQL
docker-compose up -d

# Access the application
open http://localhost:8080
```

### Local Development Setup
```bash
# Configure PostgreSQL database
# Update src/main/resources/application.properties with your DB credentials

# Build and run
./mvnw clean install
./mvnw spring-boot:run
```

## API Security

The application implements role-based access control:
- `/student/**` - Accessible only by users with STUDENT role
- `/teacher/**` - Accessible only by users with TEACHER role
- `/register/teacher` - Public endpoint for teacher registration
- All other endpoints require authentication

## Database Schema

The application uses the following main entities:
- **User**: Base authentication entity
- **Student**: Extends User with student-specific fields
- **Teacher**: Extends User with teacher-specific fields
- **Course**: Course information with teacher assignment
- **Student_Courses**: Join table for student enrollments

## Testing

```bash
# Run tests
./mvnw test

# Run tests with coverage
./mvnw verify
```

## CI/CD Pipeline

The project includes GitHub Actions workflow that:
- Runs tests on push to `dev` and feature branches
- Executes full test suite on pull requests to `main`
- Automatically deploys to production on merge to `main`


## License

This project is licensed under the MIT License - see the LICENSE file for details.
