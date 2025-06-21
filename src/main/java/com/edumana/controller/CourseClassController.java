package com.edumana.controller;

import com.edumana.model.CourseClass;
import com.edumana.service.CourseClassService;
import com.edumana.service.CourseService;
import com.edumana.service.SemesterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/courseclass")
public class CourseClassController {

    @Autowired
    private CourseClassService courseClassService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("courseClasses", courseClassService.findAll());
        return "courseclass/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("courseClass", new CourseClass());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("semesters", semesterService.getAllSemesters());
        return "courseclass/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute CourseClass courseClass,
                      @RequestParam(required = false, defaultValue = "1") Integer numberOfClasses, 
                      BindingResult result,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        try {
            // Set Semester từ semesterId
            if (courseClass.getSemesterId() != null) {
                semesterService.getSemesterById(courseClass.getSemesterId())
                    .ifPresent(courseClass::setSemester);
            }

            // Set Course từ courseId
            if (courseClass.getCourseId() != null) {
                courseService.getCourseById(courseClass.getCourseId())
                    .ifPresent(courseClass::setCourse);
            }

            // Validate required fields
            if (courseClass.getSemester() == null) {
                result.rejectValue("semesterId", "error.courseClass", "Vui lòng chọn kỳ học");
            }
            if (courseClass.getCourse() == null) {
                result.rejectValue("courseId", "error.courseClass", "Vui lòng chọn học phần");
            }
            if (courseClass.getMaxStudents() == null || courseClass.getMaxStudents() < 1) {
                result.rejectValue("maxStudents", "error.courseClass", "Số sinh viên tối đa phải lớn hơn 0");
            }
            if (numberOfClasses < 1) {
                result.rejectValue("numberOfClasses", "error.courseClass", "Số lớp phải lớn hơn 0");
            }

            if (result.hasErrors()) {
                model.addAttribute("courses", courseService.getAllCourses());
                model.addAttribute("semesters", semesterService.getAllSemesters());
                return "courseclass/form";
            }

            // Tạo nhiều lớp cùng lúc
            List<CourseClass> createdClasses = courseClassService.createMultipleClasses(courseClass, numberOfClasses);
            
            redirectAttributes.addFlashAttribute("message", 
                String.format("Đã tạo thành công %d lớp học phần!", createdClasses.size()));
            return "redirect:/courseclass/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("semesters", semesterService.getAllSemesters());
            return "courseclass/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        courseClassService.findById(id).ifPresent(courseClass -> {
            // Set the IDs for form binding
            if (courseClass.getCourse() != null) {
                courseClass.setCourseId(courseClass.getCourse().getId());
            }
            if (courseClass.getSemester() != null) {
                courseClass.setSemesterId(courseClass.getSemester().getId());
            }
            
            model.addAttribute("courseClass", courseClass);
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("semesters", semesterService.getAllSemesters());
        });
        return "courseclass/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, 
                        RedirectAttributes redirectAttributes) {
        courseClassService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", 
            "Lớp học phần đã được xóa thành công!");
        return "redirect:/courseclass/list";
    }
}
