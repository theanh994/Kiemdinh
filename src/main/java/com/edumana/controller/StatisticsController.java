package com.edumana.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edumana.dto.CourseStatistics;
import com.edumana.dto.TeachingPaymentYearlyReport;
import com.edumana.model.Course;
import com.edumana.model.Department;
import com.edumana.model.Teacher;
import com.edumana.service.CourseClassService;
import com.edumana.service.CourseService;
import com.edumana.service.DegreeService;
import com.edumana.service.DepartmentService;
import com.edumana.service.TeacherService;
import com.edumana.service.TeachingPaymentService;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseClassService courseClassService;

    @Autowired
    private TeachingPaymentService teachingPaymentService;

    @GetMapping("/teaching-payments/school")
    public String schoolTeachingPaymentStatistics(
            @RequestParam(required = false) Integer year,
            Model model) {

        // If year is not provided, use current year
        int selectedYear = year != null ? year : LocalDate.now().getYear();

        // Add years for the dropdown (current year and 4 previous years)
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            years.add(currentYear - i);
        }

        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);

        // Get all departments
        List<Department> departments = departmentService.getAllDepartments();
        Map<Department, List<TeachingPaymentYearlyReport>> departmentReports = new LinkedHashMap<>();
        
        BigDecimal schoolTotalPayment = BigDecimal.ZERO;
        int schoolTotalClasses = 0;
        int schoolTotalPeriods = 0;
        int schoolTotalTeachers = 0;

        // Calculate reports for each department
        for (Department dept : departments) {
            List<Teacher> departmentTeachers = teacherService.findByDepartment(dept);
            List<TeachingPaymentYearlyReport> teacherReports = new ArrayList<>();
            
            BigDecimal departmentTotalPayment = BigDecimal.ZERO;
            int departmentTotalClasses = 0;
            int departmentTotalPeriods = 0;
            int departmentActiveTeachers = 0;

            for (Teacher teacher : departmentTeachers) {
                TeachingPaymentYearlyReport report = 
                    teachingPaymentService.calculateYearlyPayment(teacher.getId(), selectedYear);
                
                // Only include teachers who had classes
                if (!report.getSemesterPayments().isEmpty()) {
                    teacherReports.add(report);
                    departmentTotalPayment = departmentTotalPayment.add(report.getTotalYearlyPayment());
                    departmentActiveTeachers++;
                    
                    for (TeachingPaymentYearlyReport.SemesterPayment sp : report.getSemesterPayments()) {
                        departmentTotalClasses += sp.getTotalClasses();
                        departmentTotalPeriods += sp.getTotalPeriods();
                    }
                }
            }

            if (!teacherReports.isEmpty()) {
                departmentReports.put(dept, teacherReports);
                
                // Accumulate school totals
                schoolTotalPayment = schoolTotalPayment.add(departmentTotalPayment);
                schoolTotalClasses += departmentTotalClasses;
                schoolTotalPeriods += departmentTotalPeriods;
                schoolTotalTeachers += departmentActiveTeachers;
            }
        }

        model.addAttribute("departmentReports", departmentReports);
        model.addAttribute("schoolTotalPayment", schoolTotalPayment);
        model.addAttribute("schoolTotalClasses", schoolTotalClasses);
        model.addAttribute("schoolTotalPeriods", schoolTotalPeriods);
        model.addAttribute("schoolTotalTeachers", schoolTotalTeachers);

        return "statistics/school-teaching-payments";
    }

    @GetMapping("/teaching-payments/department")
    public String departmentTeachingPaymentStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long departmentId,
            Model model) {

        // If year is not provided, use current year
        int selectedYear = year != null ? year : LocalDate.now().getYear();

        // Add years for the dropdown (current year and 4 previous years)
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            years.add(currentYear - i);
        }

        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("selectedDepartmentId", departmentId);

        // If a department is selected, calculate the report for all teachers
        if (departmentId != null) {
            Department department = departmentService.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
            
            List<Teacher> departmentTeachers = teacherService.findByDepartment(department);
            List<TeachingPaymentYearlyReport> teacherReports = new ArrayList<>();
            
            BigDecimal departmentTotalPayment = BigDecimal.ZERO;
            int departmentTotalClasses = 0;
            int departmentTotalPeriods = 0;
            
            for (Teacher teacher : departmentTeachers) {
                TeachingPaymentYearlyReport report = 
                    teachingPaymentService.calculateYearlyPayment(teacher.getId(), selectedYear);
                teacherReports.add(report);
                
                // Accumulate department totals
                departmentTotalPayment = departmentTotalPayment.add(report.getTotalYearlyPayment());
                for (TeachingPaymentYearlyReport.SemesterPayment sp : report.getSemesterPayments()) {
                    departmentTotalClasses += sp.getTotalClasses();
                    departmentTotalPeriods += sp.getTotalPeriods();
                }
            }
            
            model.addAttribute("department", department);
            model.addAttribute("teacherReports", teacherReports);
            model.addAttribute("departmentTotalPayment", departmentTotalPayment);
            model.addAttribute("departmentTotalClasses", departmentTotalClasses);
            model.addAttribute("departmentTotalPeriods", departmentTotalPeriods);
        }

        return "statistics/department-teaching-payments";
    }

    @GetMapping("/teaching-payments")
    public String teachingPaymentStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long teacherId,
            Model model) {

        // If year is not provided, use current year
        int selectedYear = year != null ? year : LocalDate.now().getYear();

        // Add years for the dropdown (current year and 4 previous years)
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            years.add(currentYear - i);
        }

        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("selectedTeacherId", teacherId);

        // If a teacher is selected, calculate their yearly report
        if (teacherId != null) {
            TeachingPaymentYearlyReport report =
                teachingPaymentService.calculateYearlyPayment(teacherId, selectedYear);
            model.addAttribute("report", report);
        }

        return "statistics/teaching-payments";
    }

    @GetMapping("/teachers")
    public String teacherStatistics(Model model) {
        // 1. Tổng số giáo viên
        int totalTeachers = teacherService.getAllTeachers().size();
        model.addAttribute("totalTeachers", totalTeachers);

        // 2. Tổng số bộ môn
        int totalDepartments = departmentService.getAllDepartments().size();
        model.addAttribute("totalDepartments", totalDepartments);

        // 3. Thống kê phân bố giáo viên theo bộ môn
        Map<String, Integer> departmentStats = new HashMap<>();
        departmentService.getAllDepartments().forEach(dept -> {
            String deptName = dept.getName() != null ? dept.getName() : "Chưa phân bộ môn";
            int count = teacherService.findByDepartment(dept).size();
            departmentStats.put(deptName, count);
        });
        model.addAttribute("departmentStats", departmentStats);

        // 4. Thống kê phân bố giáo viên theo bằng cấp
        Map<String, Integer> degreeStats = new HashMap<>();
        degreeService.getAllDegrees().forEach(degree -> {
            String degreeName = degree.getFullName() != null ? degree.getFullName() : "Chưa phân bằng cấp";
            int count = teacherService.findByDegree(degree).size();
            degreeStats.put(degreeName, count);
        });
        model.addAttribute("degreeStats", degreeStats);

        // 5. Tổng số giờ giảng dạy
        int totalTeachingHours = courseClassService.calculateTotalTeachingHours();
        model.addAttribute("totalTeachingHours", totalTeachingHours);

        // 6. Số giờ giảng dạy theo bộ môn
        Map<String, Integer> teachingHoursByDepartment = new HashMap<>();
        departmentService.getAllDepartments().forEach(dept -> {
            String deptName = dept.getName() != null ? dept.getName() : "Chưa phân bộ môn";
            int hours = courseClassService.calculateTeachingHoursByDepartment(dept.getId());
            teachingHoursByDepartment.put(deptName, hours);
        });
        model.addAttribute("teachingHoursByDepartment", teachingHoursByDepartment);

        return "statistics/teachers";
    }

    @GetMapping("/courses")
    public String courseStatistics(@RequestParam(required = false) Integer year, Model model) {
        // If year is not provided, use current year
        int selectedYear = year != null ? year : LocalDate.now().getYear();

        List<CourseStatistics> statistics = new ArrayList<>();
        List<Course> allCourses = courseService.getAllCourses();

        for (Course course : allCourses) {
            int totalClasses = courseClassService.countByYearAndCourse(selectedYear, course.getId());
            int totalStudents = courseClassService.sumMaxStudentsByYearAndCourse(selectedYear, course.getId());

            if (totalClasses > 0) { // Only add courses that have classes
                CourseStatistics stat = new CourseStatistics(
                        course.getCourseCode(),
                        course.getName(),
                        course.getCredits(),
                        totalClasses,
                        totalStudents,
                        selectedYear + ""
                );
                statistics.add(stat);
            }
        }

        // Add years for the dropdown (current year and 4 previous years)
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            years.add(currentYear - i);
        }

        model.addAttribute("statistics", statistics);
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);

        return "statistics/courses";
    }
}
