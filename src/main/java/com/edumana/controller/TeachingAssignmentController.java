package com.edumana.controller;

import com.edumana.model.CourseClass;
import com.edumana.model.Semester;
import com.edumana.model.Teacher;
import com.edumana.service.CourseClassService;
import com.edumana.service.SemesterService;
import com.edumana.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teaching")
public class TeachingAssignmentController {

    @Autowired
    private CourseClassService courseClassService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SemesterService semesterService;

    @GetMapping("/assignments")
    public String viewAssignments(
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long teacherId,
            Model model) {
        
        model.addAttribute("semesters", semesterService.getAllSemesters());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        
        if (semesterId != null) {
            Semester semester = semesterService.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ học"));
            model.addAttribute("selectedSemester", semester);
            
            if (teacherId != null) {
                Teacher teacher = teacherService.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
                model.addAttribute("selectedTeacher", teacher);
                model.addAttribute("assignedClasses", 
                    courseClassService.findBySemesterAndTeacher(semester, teacher));
            } else {
                model.addAttribute("unassignedClasses", 
                    courseClassService.findUnassignedClassesBySemester(semester));
                model.addAttribute("assignedClasses", 
                    courseClassService.findBySemester(semester));
            }
        }
        
        return "teaching/assignments";
    }

    @PostMapping("/assign")
    public String assignTeacher(
            @RequestParam Long classId,
            @RequestParam Long teacherId,
            @RequestParam Long semesterId,
            RedirectAttributes redirectAttributes) {
        
        try {
            Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));
            
            courseClassService.assignTeacher(classId, teacher);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đã phân công giảng viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi phân công giảng viên: " + e.getMessage());
        }
        
        return "redirect:/teaching/assignments?semesterId=" + semesterId;
    }

    @PostMapping("/unassign")
    public String unassignTeacher(
            @RequestParam Long classId,
            @RequestParam Long semesterId,
            RedirectAttributes redirectAttributes) {
        
        try {
            courseClassService.unassignTeacher(classId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đã hủy phân công giảng viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra khi hủy phân công giảng viên: " + e.getMessage());
        }
        
        return "redirect:/teaching/assignments?semesterId=" + semesterId;
    }
}
