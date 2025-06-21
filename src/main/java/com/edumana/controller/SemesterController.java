package com.edumana.controller;

import com.edumana.model.Semester;
import com.edumana.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Controller
@RequestMapping("/semesters")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    // Hiển thị danh sách kỳ học
    @GetMapping
    public String listSemesters(Model model) {
        model.addAttribute("semesters", semesterService.getAllSemesters());
        return "semester/list";
    }

    // Hiển thị form thêm mới kỳ học
    @GetMapping("/new")
    public String showNewSemesterForm(Model model) {
        model.addAttribute("semester", new Semester());
        return "semester/form";
    }    // Xử lý thêm mới kỳ học
    @PostMapping("/save")
    public String saveSemester(@ModelAttribute Semester semester,
                             RedirectAttributes redirectAttributes) {
        try {
            // Validate dates
            if (semester.getStartDate() != null && semester.getEndDate() != null 
                && semester.getEndDate().isBefore(semester.getStartDate())) {
                redirectAttributes.addFlashAttribute("message", "Ngày kết thúc không thể trước ngày bắt đầu!");
                redirectAttributes.addFlashAttribute("messageType", "danger");
                return "redirect:/semesters/new";
            }

            semesterService.createSemester(semester);
            redirectAttributes.addFlashAttribute("message", "Thêm mới kỳ học thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/semesters";
    }

    // Hiển thị form cập nhật kỳ học
    @GetMapping("/edit/{id}")
    public String showEditSemesterForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("semester", semesterService.getSemesterById(id).orElseThrow(() -> 
                new RuntimeException("Không tìm thấy kỳ học với ID: " + id)));
            return "semester/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
            return "redirect:/semesters";
        }
    }

    // Xử lý cập nhật kỳ học
    @PostMapping("/update/{id}")
    public String updateSemester(@PathVariable Long id, @ModelAttribute Semester semester,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               RedirectAttributes redirectAttributes) {
        try {
            semester.setStartDate(startDate);
            semester.setEndDate(endDate);
            semesterService.updateSemester(id, semester);
            redirectAttributes.addFlashAttribute("message", "Cập nhật kỳ học thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/semesters";
    }

    // Xử lý xóa kỳ học
    @GetMapping("/delete/{id}")
    public String deleteSemester(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            semesterService.deleteSemester(id);
            redirectAttributes.addFlashAttribute("message", "Xóa kỳ học thành công!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "danger");
        }
        return "redirect:/semesters";
    }
}
