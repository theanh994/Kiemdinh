package com.edumana.controller;

import com.edumana.model.Degree;
import com.edumana.service.DegreeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/degrees")
public class DegreeController {

    @Autowired
    private DegreeService degreeService;

    // Hiển thị danh sách bằng cấp
    @GetMapping
    public String listDegrees(Model model) {
        model.addAttribute("degrees", degreeService.getAllDegrees());
        return "degree/list";
    }

    // Hiển thị form thêm mới bằng cấp
    @GetMapping("/new")
    public String showNewDegreeForm(Model model) {
        model.addAttribute("degree", new Degree());
        // Đề xuất hệ số giảng dạy mặc định
        model.addAttribute("suggestedCoefficients", new BigDecimal[] {
            new BigDecimal("1.3"),  // Đại học
            new BigDecimal("1.5"),  // Thạc sĩ
            new BigDecimal("1.7"),  // Tiến sĩ
            new BigDecimal("2.0"),  // Phó Giáo sư
            new BigDecimal("2.5")   // Giáo sư
        });
        return "degree/form";
    }

    // Hiển thị form cập nhật bằng cấp
    @GetMapping("/edit/{id}")
    public String showEditDegreeForm(@PathVariable Long id, Model model) {
        Degree degree = degreeService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Mã bằng cấp không hợp lệ: " + id));
        model.addAttribute("degree", degree);
        // Đề xuất hệ số giảng dạy mặc định
        model.addAttribute("suggestedCoefficients", new BigDecimal[] {
            new BigDecimal("1.3"),  // Đại học
            new BigDecimal("1.5"),  // Thạc sĩ
            new BigDecimal("1.7"),  // Tiến sĩ
            new BigDecimal("2.0"),  // Phó Giáo sư
            new BigDecimal("2.5")   // Giáo sư
        });
        return "degree/form";
    }

    // Xử lý thêm mới bằng cấp
    @PostMapping("/save")
    public String saveDegree(@Valid @ModelAttribute Degree degree,
                      BindingResult result,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        
        if (result.hasErrors()) {
            // Thêm lại đề xuất nếu xác thực không thành công
            model.addAttribute("suggestedCoefficients", new BigDecimal[] {
                new BigDecimal("1.3"),
                new BigDecimal("1.5"),
                new BigDecimal("1.7"),
                new BigDecimal("2.0"),
                new BigDecimal("2.5")
            });
            return "degree/form";
        }

        degreeService.save(degree);
        redirectAttributes.addFlashAttribute("successMessage", 
            "Bằng cấp " + degree.getFullName() + " đã được lưu thành công!");
        return "redirect:/degrees";
    }

    // Xử lý xóa bằng cấp
    @GetMapping("/delete/{id}")
    public String deleteDegree(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Degree degree = degreeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mã bằng cấp không hợp lệ: " + id));
            
            degreeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Đã xóa bằng cấp " + degree.getFullName() + " thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Không thể xóa bằng cấp này vì đang được sử dụng!");
        }
        return "redirect:/degrees";
    }
}
