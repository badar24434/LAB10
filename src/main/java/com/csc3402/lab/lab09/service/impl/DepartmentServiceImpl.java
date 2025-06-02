package com.csc3402.lab.lab09.service.impl;

import com.csc3402.lab.lab09.model.Department;
import com.csc3402.lab.lab09.repository.DepartmentRepository;
import com.csc3402.lab.lab09.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
    
    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }
    
    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }
    
    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
