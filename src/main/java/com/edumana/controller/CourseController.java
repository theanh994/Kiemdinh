package com.edumana.controller;

import com.edumana.model.Course;
import com.edumana.model.CourseClass;
import com.edumana.model.Semester;
import com.edumana.service.CourseService;
import com.edumana.service.CourseClassService;
import com.edumana.service.DepartmentService;
import com.edumana.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseClassService courseClassService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SemesterService semesterService;

    // Hiển thị danh sách học phần
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "course/list";
    }

    // Hiển thị form thêm mới học phần
    @GetMapping("/new")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/form";
    }

    // Xử lý thêm mới học phần
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        try {
            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("message", "Thêm mới học phần thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/courses";
    }

    // Hiển thị form cập nhật học phần
    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("course", courseService.getCourseById(id).orElseThrow(() -> 
                new RuntimeException("Không tìm thấy học phần với ID: " + id)));
            return "course/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/courses";
        }
    }

    // Xử lý cập nhật học phần
    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course course, 
                             RedirectAttributes redirectAttributes) {
        try {
            courseService.updateCourse(id, course);
            redirectAttributes.addFlashAttribute("message", "Cập nhật học phần thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/courses";
    }

    // Xử lý xóa học phần
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("message", "Xóa học phần thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/courses";
    }

    // Hiển thị danh sách học phần theo khoa
    @GetMapping("/department")
    public String departmentCourses(
            @RequestParam(required = false) String departmentId,
            Model model) {
        
        // Lấy danh sách tất cả các khoa để lọc
        model.addAttribute("departments", departmentService.getAllDepartments());
        
        // Parse department ID
        Long deptId = null;
        if (departmentId != null && !departmentId.equals("all")) {
            try {
                deptId = Long.parseLong(departmentId);
            } catch (NumberFormatException e) {
                // Invalid ID, treat as null (all departments)
            }
        }
        model.addAttribute("selectedDepartmentId", deptId);

        // Lấy danh sách học phần gần đây (thêm trong 30 ngày qua)
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<Course> recentCourses;
        List<Course> otherCourses;

        if (deptId != null) {
            // Lọc theo khoa
            recentCourses = courseService.findRecentCoursesByDepartment(deptId, thirtyDaysAgo);
            otherCourses = courseService.findOlderCoursesByDepartment(deptId, thirtyDaysAgo);
        } else {
            // Tất cả các học phần
            recentCourses = courseService.findRecentCourses(thirtyDaysAgo);
            otherCourses = courseService.findOlderCourses(thirtyDaysAgo);
        }

        // Lấy thống kê học phần (số lượng lớp học cho mỗi học phần)
        Map<Long, Map<String, Integer>> courseStats = new HashMap<>();
        Stream.concat(recentCourses.stream(), otherCourses.stream())
            .forEach(course -> {
                Map<String, Integer> stats = courseService.getStatistics(course.getId());
                courseStats.put(course.getId(), stats);
            });

        model.addAttribute("recentCourses", recentCourses);
        model.addAttribute("courses", otherCourses);
        model.addAttribute("courseStats", courseStats);

        return "course/department-courses";
    }

    // Hiển thị danh sách lớp học của một học phần
    @GetMapping("/classes/{courseId}")
    public String courseClasses(
            @PathVariable Long courseId,
            @RequestParam(required = false) Long semesterId,
            Model model) {
        
        Course course = courseService.getCourseById(courseId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));

        // Lấy danh sách tất cả các kỳ học để lọc
        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("selectedSemesterId", semesterId);
        
        // Lấy danh sách lớp học của học phần
        List<CourseClass> classes;
        if (semesterId != null) {
            // Lọc theo kỳ học
            Semester semester = semesterService.getSemesterById(semesterId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ học"));
            classes = courseClassService.findBySemesterAndCourse(semester, course);
        } else {
            // Tất cả các lớp học
            classes = courseClassService.findByCourse(course);
        }

        model.addAttribute("course", course);
        model.addAttribute("classes", classes);

        return "course/course-classes";
    }
}
