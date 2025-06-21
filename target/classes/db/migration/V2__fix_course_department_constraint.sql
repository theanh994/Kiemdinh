-- First, ensure we have at least one department
INSERT IGNORE INTO departments (id, name, abbreviation) 
VALUES (1, 'Khoa mặc định', 'KMD')
ON DUPLICATE KEY UPDATE name = name;

-- Update any courses with null department_id to use the default department
UPDATE courses SET department_id = 1 WHERE department_id IS NULL;

-- Now it's safe to add the foreign key constraint
ALTER TABLE courses 
MODIFY COLUMN department_id bigint NOT NULL,
ADD CONSTRAINT fk_course_department 
FOREIGN KEY (department_id) REFERENCES departments(id);
