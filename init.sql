-- Create users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('STUDENT', 'TEACHER')),
                       enabled BOOLEAN DEFAULT TRUE
);

-- Create departments table
CREATE TABLE departments (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             code VARCHAR(10) NOT NULL UNIQUE
);

-- Create teachers table
CREATE TABLE teachers (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                          department_id INTEGER REFERENCES departments(id),
                          employee_id VARCHAR(20) UNIQUE NOT NULL
);

-- Create students table
CREATE TABLE students (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                          department_id INTEGER REFERENCES departments(id),
                          student_id VARCHAR(20) UNIQUE NOT NULL,
                          phone VARCHAR(20),
                          address TEXT
);

-- Create courses table
CREATE TABLE courses (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         code VARCHAR(20) UNIQUE NOT NULL,
                         teacher_id INTEGER REFERENCES teachers(id),
                         department_id INTEGER REFERENCES departments(id)
);

-- Create student_courses table (Many-to-Many)
CREATE TABLE student_courses (
                                 student_id INTEGER REFERENCES students(id) ON DELETE CASCADE,
                                 course_id INTEGER REFERENCES courses(id) ON DELETE CASCADE,
                                 PRIMARY KEY (student_id, course_id)
);

-- Insert sample departments
INSERT INTO departments (name, code) VALUES
                                         ('Computer Science', 'CS'),
                                         ('Mathematics', 'MATH'),
                                         ('Physics', 'PHY');

-- Insert a teacher user (password: teacher123)
INSERT INTO users (username, password, email, role) VALUES
    ('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIiW', 'teacher1@school.edu', 'TEACHER');

-- Insert the teacher record
INSERT INTO teachers (user_id, department_id, employee_id) VALUES
    (1, 1, 'T001');

-- Insert a student user (password: student123)
INSERT INTO users (username, password, email, role) VALUES
    ('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIiW', 'student1@school.edu', 'STUDENT');

-- Insert the student record
INSERT INTO students (user_id, department_id, student_id, phone, address) VALUES
    (2, 1, 'S001', '1234567890', '123 Main St');