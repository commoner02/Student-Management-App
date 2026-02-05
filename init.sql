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