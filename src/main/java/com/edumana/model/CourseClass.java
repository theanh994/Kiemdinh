package com.edumana.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "course_classes")
public class CourseClass {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester; // Thuộc kỳ học

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Thuộc học phần

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // Giảng viên phụ trách

    @Column(name = "class_code", unique = true, nullable = false)
    private String classCode; // Mã lớp

    @Column(name = "class_name", nullable = false)
    private String className; // Tên lớp

    @Column(name = "max_students", nullable = false)
    @Min(value = 1, message = "Số sinh viên phải lớn hơn 0")
    private Integer maxStudents; // Số sinh viên tối đa

    @Transient
    private Long semesterId;

    @Transient
    private Long courseId;

    @Transient
    private Long teacherId;

    // Constructors
    public CourseClass() {
    }

    public CourseClass(Semester semester, Course course, Teacher teacher, String classCode, 
                      String className, Integer maxStudents) {
        this.semester = semester;
        this.course = course;
        this.teacher = teacher;
        this.classCode = classCode;
        this.className = className;
        this.maxStudents = maxStudents;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Long getSemesterId() {
        return this.semester != null ? this.semester.getId() : this.semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getCourseId() {
        return this.course != null ? this.course.getId() : this.courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getTeacherId() {
        return this.teacher != null ? this.teacher.getId() : this.teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "CourseClass{" +
                "id=" + id +
                ", semester=" + semester +
                ", course=" + course +
                ", teacher=" + teacher +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", maxStudents=" + maxStudents +
                '}';
    }
}
