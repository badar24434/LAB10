package com.csc3402.lab.lab09.service;

import com.csc3402.lab.lab09.model.Staff;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface StaffService {
    List<Staff> findAll();
    Optional<Staff> findById(Long id);
    Optional<Staff> findByEmail(String email);
    boolean existsByEmail(String email);
    Staff save(Staff staff);
    void delete(Staff staff);
    void deleteById(Long id);
    
    // Methods for managing staff-project relationships
    void addProjectToStaff(Long staffId, Long projectId);
    void removeProjectFromStaff(Long staffId, Long projectId);
    
    // Methods for handling complex create/update operations
    Staff createStaffFromData(Map<String, Object> staffData);
    Staff updateStaffFromData(Long id, Map<String, Object> staffData);
}
