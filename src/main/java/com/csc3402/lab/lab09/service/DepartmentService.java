package com.csc3402.lab.lab09.service;

import com.csc3402.lab.lab09.model.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> findAll();
    Optional<Department> findById(Long id);
    Department save(Department department);
    void deleteById(Long id);
}
