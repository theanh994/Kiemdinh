package com.edumana.repository;

import com.edumana.model.CourseClass;
import com.edumana.model.Course;
import com.edumana.model.Semester;
import com.edumana.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
    List<CourseClass> findBySemester(Semester semester);
    List<CourseClass> findByCourse(Course course);
    List<CourseClass> findByTeacher(Teacher teacher);
    List<CourseClass> findBySemesterAndTeacher(Semester semester, Teacher teacher);
    List<CourseClass> findBySemesterAndTeacherIsNull(Semester semester);
    Optional<CourseClass> findByClassCode(String classCode);
    List<CourseClass> findBySemesterOrderByClassCodeAsc(Semester semester);

    @Query("SELECT cc FROM CourseClass cc WHERE YEAR(cc.semester.startDate) = :year")
    List<CourseClass> findByYear(@Param("year") int year);

    @Query("SELECT cc FROM CourseClass cc " +
           "WHERE YEAR(cc.semester.startDate) = :year " +
           "AND cc.course.id = :courseId")
    List<CourseClass> findByYearAndCourse(@Param("year") int year, @Param("courseId") Long courseId);

    @Query("SELECT COUNT(cc) FROM CourseClass cc " +
           "WHERE YEAR(cc.semester.startDate) = :year " +
           "AND cc.course.id = :courseId")
    int countByYearAndCourse(@Param("year") int year, @Param("courseId") Long courseId);

    @Query("SELECT SUM(cc.maxStudents) FROM CourseClass cc " +
           "WHERE YEAR(cc.semester.startDate) = :year " +
           "AND cc.course.id = :courseId")
    Integer sumMaxStudentsByYearAndCourse(@Param("year") int year, @Param("courseId") Long courseId);

    @Query("SELECT cc FROM CourseClass cc WHERE cc.classCode LIKE :prefix%")
    List<CourseClass> findByClassCodeStartingWith(@Param("prefix") String prefix);

    List<CourseClass> findBySemesterAndCourse(Semester semester, Course course);
}
