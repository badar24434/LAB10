package com.csc3402.lab.lab09.service.impl;

import com.csc3402.lab.lab09.model.Department;
import com.csc3402.lab.lab09.model.Project;
import com.csc3402.lab.lab09.model.Staff;
import com.csc3402.lab.lab09.repository.DepartmentRepository;
import com.csc3402.lab.lab09.repository.ProjectRepository;
import com.csc3402.lab.lab09.repository.StaffRepository;
import com.csc3402.lab.lab09.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaffServiceImpl implements StaffService {
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }
    
    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }
    
    @Override
    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return staffRepository.existsByEmail(email);
    }
    
    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }
    
    @Override
    public void delete(Staff staff) {
        staffRepository.delete(staff);
    }
    
    @Override
    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }
    
    @Override
    public void addProjectToStaff(Long staffId, Long projectId) {
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        
        if (staffOpt.isPresent() && projectOpt.isPresent()) {
            Staff staff = staffOpt.get();
            Project project = projectOpt.get();
            
            staff.addProject(project);
            staffRepository.save(staff);
        }
    }
    
    @Override
    public void removeProjectFromStaff(Long staffId, Long projectId) {
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        
        if (staffOpt.isPresent() && projectOpt.isPresent()) {
            Staff staff = staffOpt.get();
            Project project = projectOpt.get();
            
            staff.removeProject(project);
            staffRepository.save(staff);
        }
    }
    
    @Override
    public Staff createStaffFromData(Map<String, Object> staffData) {
        // Extract basic staff data
        String name = (String) staffData.get("name");
        String email = (String) staffData.get("email");
        String position = (String) staffData.get("position");
        
        // Create new staff
        Staff staff = new Staff(name, email, position);
        
        // Handle department
        Object deptObj = staffData.get("department");
        if (deptObj != null) {
            Long deptId = extractId(deptObj);
            if (deptId != null) {
                Department department = departmentRepository.findById(deptId).orElse(null);
                staff.setDepartment(department);
            }
        }
        
        // Handle projects
        processProjectsData(staff, staffData.get("projects"));
        
        // Save and return
        return staffRepository.save(staff);
    }
    
    @Override
    public Staff updateStaffFromData(Long id, Map<String, Object> staffData) {
        Optional<Staff> staffOpt = staffRepository.findById(id);
        if (!staffOpt.isPresent()) {
            return null;
        }
        
        Staff staff = staffOpt.get();
        
        // Update basic fields
        staff.setName((String) staffData.get("name"));
        staff.setEmail((String) staffData.get("email"));
        staff.setPosition((String) staffData.get("position"));
        
        // Handle department
        Object deptObj = staffData.get("department");
        if (deptObj != null) {
            Long deptId = extractId(deptObj);
            if (deptId != null) {
                Department department = departmentRepository.findById(deptId).orElse(null);
                staff.setDepartment(department);
            } else {
                staff.setDepartment(null);
            }
        } else {
            staff.setDepartment(null);
        }
        
        // Handle projects
        staff.getProjects().clear();
        processProjectsData(staff, staffData.get("projects"));
        
        // Save and return
        return staffRepository.save(staff);
    }
    
    // Helper method to extract ID from various object types
    private Long extractId(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            Object idObj = map.get("id");
            return extractId(idObj);
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    // Helper method to process projects data
    private void processProjectsData(Staff staff, Object projectsObj) {
        if (projectsObj instanceof List) {
            List<?> projectsList = (List<?>) projectsObj;
            for (Object projObj : projectsList) {
                Long projectId = extractId(projObj);
                if (projectId != null) {
                    projectRepository.findById(projectId).ifPresent(staff::addProject);
                }
            }
        }
    }
}
