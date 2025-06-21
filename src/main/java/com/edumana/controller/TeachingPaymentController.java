package com.edumana.controller;

import com.edumana.dto.TeachingPaymentDTO;
import com.edumana.model.Semester;
import com.edumana.model.Teacher;
import com.edumana.service.SemesterService;
import com.edumana.service.TeacherService;
import com.edumana.service.TeachingPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teaching-payment")
public class TeachingPaymentController {

    @Autowired
    private TeachingPaymentService teachingPaymentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SemesterService semesterService;

    @GetMapping("/calculate")
    public String showCalculationForm(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("semesters", semesterService.getAllSemesters());
        return "teaching-payment/form";
    }

    @GetMapping("/result")
    public String calculatePayment(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long semesterId,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (teacherId == null || semesterId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Vui lòng chọn giáo viên và kỳ học!");
            return "redirect:/teaching-payment/calculate";
        }

        try {
            Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên"));
            
            Semester semester = semesterService.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ học"));

            TeachingPaymentDTO paymentDetails = teachingPaymentService.calculatePayment(teacher, semester);
            model.addAttribute("paymentDetails", paymentDetails);
            
            // Add these for the form at the top of the result page
            model.addAttribute("teachers", teacherService.getAllTeachers());
            model.addAttribute("semesters", semesterService.getAllSemesters());
            model.addAttribute("selectedTeacherId", teacherId);
            model.addAttribute("selectedSemesterId", semesterId);
            
            return "teaching-payment/result";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/teaching-payment/calculate";
        }
    }
}
