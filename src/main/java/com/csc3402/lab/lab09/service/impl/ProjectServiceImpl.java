package com.csc3402.lab.lab09.service.impl;

import com.csc3402.lab.lab09.model.Project;
import com.csc3402.lab.lab09.repository.ProjectRepository;
import com.csc3402.lab.lab09.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }
    
    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
    
    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }
    
    @Override
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }
}
