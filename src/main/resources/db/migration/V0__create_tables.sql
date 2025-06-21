-- Create departments table
CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(10)
);

-- Create teachers table
CREATE TABLE teachers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE
);

-- Create courses table
CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    department_id BIGINT
);

-- Create course_classes table
CREATE TABLE course_classes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    course_id BIGINT,
    teacher_id BIGINT
);

-- Insert default department
INSERT INTO departments (id, name, abbreviation) VALUES (1, 'Khoa mặc định', 'KMD');