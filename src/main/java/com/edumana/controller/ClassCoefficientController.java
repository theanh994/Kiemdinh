package com.edumana.controller;

import com.edumana.model.ClassCoefficient;
import com.edumana.service.ClassCoefficientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/class-coefficients")
public class ClassCoefficientController {

    @Autowired
    private ClassCoefficientService classCoefficientService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("coefficients", classCoefficientService.getAllCoefficients());
        return "class-coefficient/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("classCoefficient", new ClassCoefficient());
        // Thêm các hệ số mẫu vào model
        model.addAttribute("sampleCoefficients", new BigDecimal[] {
            new BigDecimal("-0.3"),  // < 20 sinh viên
            new BigDecimal("-0.2"),  // 20-29 sinh viên
            new BigDecimal("-0.1"),  // 30-39 sinh viên
            new BigDecimal("0.0"),   // 40-49 sinh viên
            new BigDecimal("0.1"),   // 50-59 sinh viên
            new BigDecimal("0.2"),   // 60-69 sinh viên
            new BigDecimal("0.3")    // 70-79 sinh viên
        });
        return "class-coefficient/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute ClassCoefficient classCoefficient,
                      BindingResult result,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("sampleCoefficients", new BigDecimal[] {
                new BigDecimal("-0.3"), new BigDecimal("-0.2"),
                new BigDecimal("-0.1"), new BigDecimal("0.0"),
                new BigDecimal("0.1"), new BigDecimal("0.2"),
                new BigDecimal("0.3")
            });
            return "class-coefficient/form";
        }

        try {
            classCoefficientService.save(classCoefficient);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Hệ số lớp đã được lưu thành công!");
        } catch (IllegalArgumentException e) {
            result.rejectValue("minStudents", "error.classCoefficient", e.getMessage());
            return "class-coefficient/form";
        }

        return "redirect:/class-coefficients/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        classCoefficientService.findById(id).ifPresent(coefficient -> {
            model.addAttribute("classCoefficient", coefficient);
            model.addAttribute("sampleCoefficients", new BigDecimal[] {
                new BigDecimal("-0.3"), new BigDecimal("-0.2"),
                new BigDecimal("-0.1"), new BigDecimal("0.0"),
                new BigDecimal("0.1"), new BigDecimal("0.2"),
                new BigDecimal("0.3")
            });
        });
        return "class-coefficient/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            classCoefficientService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Hệ số lớp đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Không thể xóa hệ số lớp này!");
        }
        return "redirect:/class-coefficients/list";
    }
}
