package com.csc3402.lab.lab09.controller;

import com.csc3402.lab.lab09.model.Staff;
import com.csc3402.lab.lab09.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // Redirect root to list page
    @GetMapping("/")
    public String home() {
        return "redirect:/staffs/list";
    }

    @GetMapping("/list")
    public String listStaffs(Model model) {
        model.addAttribute("staffs", staffService.findAll());
        return "list-staff";
    }

    @GetMapping("/add")
    public String showAddForm(Staff staff) {
        return "add-staff";
    }

    @PostMapping("/add")
    public String addStaff(@Valid Staff staff, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-staff";
        }

        // Check if email already exists
        if (staffService.existsByEmail(staff.getEmail())) {
            result.rejectValue("email", "error.staff", "Email already exists");
            return "add-staff";
        }

        staffService.save(staff);
        return "redirect:/staffs/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Staff staff = staffService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff Id:" + id));
        model.addAttribute("staff", staff);
        return "update-staff";
    }

    @PostMapping("/update/{id}")
    public String updateStaff(@PathVariable("id") Long id, @Valid Staff staff,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            staff.setId(id);
            return "update-staff";
        }

        // Check if email already exists (excluding current staff)
        Staff existingStaff = staffService.findByEmail(staff.getEmail()).orElse(null);
        if (existingStaff != null && !existingStaff.getId().equals(id)) {
            result.rejectValue("email", "error.staff", "Email already exists");
            staff.setId(id);
            return "update-staff";
        }

        staff.setId(id);
        staffService.save(staff);
        return "redirect:/staffs/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable("id") Long id) {
        Staff staff = staffService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid staff Id:" + id));
        staffService.delete(staff);
        return "redirect:/staffs/list";
    }
}