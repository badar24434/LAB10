package com.csc3402.lab.lab09.service;

import com.csc3402.lab.lab09.model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> findAll();
    Optional<Project> findById(Long id);
    Project save(Project project);
    void deleteById(Long id);
}
