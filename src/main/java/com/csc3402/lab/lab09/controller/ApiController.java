package com.csc3402.lab.lab09.controller;

import com.csc3402.lab.lab09.model.Staff;
import com.csc3402.lab.lab09.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/staffs")
public class ApiController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public List<Staff> getAllStaffs() {
        return staffService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Long id) {
        return staffService.findById(id)
                .map(staff -> ResponseEntity.ok().body(staff))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createStaff(@Valid @RequestBody Map<String, Object> staffData) {
        try {
            // Validate required fields
            String name = (String) staffData.get("name");
            String email = (String) staffData.get("email");
            String position = (String) staffData.get("position");
            
            if (name == null || email == null || position == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Name, email, and position are required");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Check if email already exists
            if (staffService.existsByEmail(email)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Delegate to service layer
            Staff savedStaff = staffService.createStaffFromData(staffData);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStaff);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error creating staff: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @Valid @RequestBody Map<String, Object> staffData) {
        try {
            // Validate required fields
            String name = (String) staffData.get("name");
            String email = (String) staffData.get("email");
            String position = (String) staffData.get("position");
            
            if (name == null || email == null || position == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Name, email, and position are required");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Check if staff exists
            if (!staffService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Check if email already exists (excluding current staff)
            Staff existingStaff = staffService.findByEmail(email).orElse(null);
            if (existingStaff != null && !existingStaff.getId().equals(id)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Delegate to service layer
            Staff updatedStaff = staffService.updateStaffFromData(id, staffData);
            return ResponseEntity.ok(updatedStaff);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error updating staff: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        return staffService.findById(id)
                .map(staff -> {
                    staffService.delete(staff);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{staffId}/projects/{projectId}")
    public ResponseEntity<?> addProjectToStaff(@PathVariable Long staffId, @PathVariable Long projectId) {
        try {
            staffService.addProjectToStaff(staffId, projectId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{staffId}/projects/{projectId}")
    public ResponseEntity<?> removeProjectFromStaff(@PathVariable Long staffId, @PathVariable Long projectId) {
        try {
            staffService.removeProjectFromStaff(staffId, projectId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
