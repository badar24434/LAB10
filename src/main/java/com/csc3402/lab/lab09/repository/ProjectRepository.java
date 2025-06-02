package com.csc3402.lab.lab09.repository;

import com.csc3402.lab.lab09.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom query methods can be added here if needed
    boolean existsByName(String name);
}
