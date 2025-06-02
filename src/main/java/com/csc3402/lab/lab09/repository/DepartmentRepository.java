package com.csc3402.lab.lab09.repository;

import com.csc3402.lab.lab09.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Add custom query methods if needed
}
