package com.edumana.controller;

import com.edumana.model.Teacher;
import com.edumana.service.TeacherService;
import com.edumana.service.DepartmentService;
import com.edumana.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DegreeService degreeService;

    // Hiển thị danh sách giáo viên
    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "teacher/list";
    }

    // Hiển thị form thêm mới giáo viên
    @GetMapping("/new")
    public String showNewTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("degrees", degreeService.getAllDegrees());
        return "teacher/form";
    }    // Xử lý thêm mới giáo viên
    @PostMapping("/save")
    public String saveTeacher(@ModelAttribute Teacher teacher, 
                            RedirectAttributes redirectAttributes) {
        try {
            // Tìm Department và Degree từ id
            if (teacher.getDepartmentId() != null) {
                departmentService.getDepartmentById(teacher.getDepartmentId())
                    .ifPresent(teacher::setDepartment);
            }
            if (teacher.getDegreeId() != null) {
                degreeService.getDegreeById(teacher.getDegreeId())
                    .ifPresent(teacher::setDegree);
            }

            teacherService.createTeacher(teacher);
            redirectAttributes.addFlashAttribute("message", "Thêm mới giáo viên thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/teachers";
    }

    // Hiển thị form cập nhật giáo viên
    @GetMapping("/edit/{id}")
    public String showEditTeacherForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("teacher", teacherService.getTeacherById(id).orElseThrow(() -> 
                new RuntimeException("Không tìm thấy giáo viên với ID: " + id)));
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("degrees", degreeService.getAllDegrees());
            return "teacher/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/teachers";
        }
    }

    // Xử lý cập nhật giáo viên
    @PostMapping("/update/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute Teacher teacher,
                              RedirectAttributes redirectAttributes) {
        try {
            teacherService.updateTeacher(id, teacher);
            redirectAttributes.addFlashAttribute("message", "Cập nhật giáo viên thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/teachers";
    }

    // Xử lý xóa giáo viên
    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teacherService.deleteTeacher(id);
            redirectAttributes.addFlashAttribute("message", "Xóa giáo viên thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/teachers";
    }
}
