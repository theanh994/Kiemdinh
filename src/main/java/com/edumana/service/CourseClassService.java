package com.edumana.service;

import com.edumana.model.CourseClass;
import com.edumana.model.Course;
import com.edumana.model.Semester;
import com.edumana.model.Teacher;
import com.edumana.repository.CourseClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseClassService {

    @Autowired
    private CourseClassRepository courseClassRepository;

    public List<CourseClass> findAll() {
        return courseClassRepository.findAll();
    }

    public Optional<CourseClass> findById(Long id) {
        return courseClassRepository.findById(id);
    }

    public List<CourseClass> findBySemester(Semester semester) {
        return courseClassRepository.findBySemesterOrderByClassCodeAsc(semester);
    }

    public List<CourseClass> findByCourse(Course course) {
        return courseClassRepository.findByCourse(course);
    }

    public List<CourseClass> findByTeacher(Teacher teacher) {
        return courseClassRepository.findByTeacher(teacher);
    }

    public List<CourseClass> findBySemesterAndTeacher(Semester semester, Teacher teacher) {
        return courseClassRepository.findBySemesterAndTeacher(semester, teacher);
    }

    public List<CourseClass> findUnassignedClassesBySemester(Semester semester) {
        return courseClassRepository.findBySemesterAndTeacherIsNull(semester);
    }

    @Transactional
    public CourseClass assignTeacher(Long classId, Teacher teacher) {
        CourseClass courseClass = courseClassRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần"));
        courseClass.setTeacher(teacher);
        return courseClassRepository.save(courseClass);
    }

    @Transactional
    public CourseClass unassignTeacher(Long classId) {
        CourseClass courseClass = courseClassRepository.findById(classId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần"));
        courseClass.setTeacher(null);
        return courseClassRepository.save(courseClass);
    }

    public CourseClass save(CourseClass courseClass) {
        return courseClassRepository.save(courseClass);
    }

    public void deleteById(Long id) {
        courseClassRepository.deleteById(id);
    }

    public boolean isClassCodeUnique(String classCode) {
        return courseClassRepository.findByClassCode(classCode).isEmpty();
    }

    public boolean isClassCodeUnique(String classCode, Long currentId) {
        if (currentId == null) {
            return isClassCodeUnique(classCode);
        }
        Optional<CourseClass> existingClass = courseClassRepository.findByClassCode(classCode);
        return existingClass.isEmpty() || existingClass.get().getId().equals(currentId);
    }

    public List<CourseClass> findByYear(int year) {
        return courseClassRepository.findByYear(year);
    }

    public int countByYearAndCourse(int year, Long courseId) {
        return courseClassRepository.countByYearAndCourse(year, courseId);
    }

    public int sumMaxStudentsByYearAndCourse(int year, Long courseId) {
        Integer sum = courseClassRepository.sumMaxStudentsByYearAndCourse(year, courseId);
        return sum != null ? sum : 0;
    }

    public int calculateTotalTeachingHours() {
        return courseClassRepository.findAll().stream()
            .filter(courseClass -> courseClass.getCourse() != null)
            .mapToInt(courseClass -> courseClass.getCourse().getPeriods())
            .sum();
    }

    public int calculateTeachingHoursByDepartment(Long departmentId) {
        if (departmentId == null) {
            // Calculate hours for teachers without department
            return courseClassRepository.findAll().stream()
                .filter(courseClass -> 
                    courseClass.getTeacher() != null && 
                    courseClass.getCourse() != null &&
                    courseClass.getTeacher().getDepartment() == null)
                .mapToInt(courseClass -> courseClass.getCourse().getPeriods())
                .sum();
        } else {
            return courseClassRepository.findAll().stream()
                .filter(courseClass -> 
                    courseClass.getTeacher() != null && 
                    courseClass.getCourse() != null &&
                    courseClass.getTeacher().getDepartment() != null && 
                    courseClass.getTeacher().getDepartment().getId().equals(departmentId))
                .mapToInt(courseClass -> courseClass.getCourse().getPeriods())
                .sum();
        }
    }

    private String generateClassCode(Semester semester, int sequence) {
        String term = semester.getTerm(); // Sử dụng phương thức getTerm() mới
        String year = semester.getStartYear().substring(2); // Lấy 2 số cuối của năm đầu kỳ
        return String.format("ML%s%s%03d", term, year, sequence);
    }

    private String generateClassName(Semester semester, int sequence) {
        String term = semester.getTerm(); // Sử dụng phương thức getTerm() mới
        String year = semester.getStartYear().substring(2); // Lấy 2 số cuối của năm đầu kỳ
        return String.format("TL%s%s%03d", term, year, sequence);
    }

    @Transactional
    public List<CourseClass> createMultipleClasses(CourseClass template, int numberOfClasses) {
        List<CourseClass> createdClasses = new ArrayList<>();

        // Get prefix for the current semester
        String prefixML = String.format("ML%s%s",
            template.getSemester().getTerm(),
            template.getSemester().getStartYear().substring(2));
        
        // Find the current max sequence number
        int startSequence = 1;
        List<CourseClass> existingClasses = courseClassRepository.findByClassCodeStartingWith(prefixML);
        
        if (!existingClasses.isEmpty()) {
            Optional<Integer> maxSequence = existingClasses.stream()
                .map(cc -> {
                    String seqStr = cc.getClassCode().substring(cc.getClassCode().length() - 3);
                    try {
                        return Integer.parseInt(seqStr);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo);
            
            startSequence = maxSequence.map(seq -> seq + 1).orElse(1);
        }

        // Create new classes
        for (int i = 0; i < numberOfClasses; i++) {
            CourseClass newClass = new CourseClass();
            newClass.setSemester(template.getSemester());
            newClass.setCourse(template.getCourse());
            newClass.setMaxStudents(template.getMaxStudents());
            
            int sequence = startSequence + i;
            newClass.setClassCode(generateClassCode(template.getSemester(), sequence));
            newClass.setClassName(generateClassName(template.getSemester(), sequence));
            
            createdClasses.add(courseClassRepository.save(newClass));
        }
        
        return createdClasses;
    }

    public List<CourseClass> findBySemesterAndCourse(Semester semester, Course course) {
        return courseClassRepository.findAll().stream()
            .filter(cc -> cc.getSemester().equals(semester) && cc.getCourse().equals(course))
            .collect(Collectors.toList());
    }
}
