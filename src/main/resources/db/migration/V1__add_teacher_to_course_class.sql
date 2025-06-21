-- Add teacher_id column to course_classes table
ALTER TABLE course_classes
ADD COLUMN teacher_id BIGINT;

-- Add foreign key constraint
ALTER TABLE course_classes
ADD CONSTRAINT fk_course_class_teacher
FOREIGN KEY (teacher_id)
REFERENCES teachers (id);
