package com.edumana.controller;

import com.edumana.model.TeachingRate;
import com.edumana.service.TeachingRateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teaching-rates")
public class TeachingRateController {

    @Autowired
    private TeachingRateService teachingRateService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("rates", teachingRateService.getAllRates());
        return "teaching-rate/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("teachingRate", new TeachingRate());
        return "teaching-rate/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute TeachingRate teachingRate,
                      BindingResult result,
                      RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "teaching-rate/form";
        }

        teachingRateService.save(teachingRate);
        redirectAttributes.addFlashAttribute("successMessage", 
            "Định mức tiền dạy đã được lưu thành công!");
        return "redirect:/teaching-rates/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        teachingRateService.findById(id).ifPresent(rate -> 
            model.addAttribute("teachingRate", rate));
        return "teaching-rate/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teachingRateService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Định mức tiền dạy đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Không thể xóa định mức tiền dạy này!");
        }
        return "redirect:/teaching-rates/list";
    }
}
