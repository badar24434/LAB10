package com.csc3402.lab.lab09.controller;

import com.csc3402.lab.lab09.model.Project;
import com.csc3402.lab.lab09.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectApiController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(project -> ResponseEntity.ok().body(project))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project) {
        // Check if project name already exists
        if (projectRepository.existsByName(project.getName())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Project name already exists");
            return ResponseEntity.badRequest().body(error);
        }

        Project savedProject = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Valid @RequestBody Project projectDetails) {
        return projectRepository.findById(id)
                .map(project -> {
                    // Check if name already exists (excluding current project)
                    if (!project.getName().equals(projectDetails.getName()) && 
                        projectRepository.existsByName(projectDetails.getName())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("message", "Project name already exists");
                        return ResponseEntity.badRequest().body(error);
                    }

                    project.setName(projectDetails.getName());
                    project.setDescription(projectDetails.getDescription());
                    project.setStatus(projectDetails.getStatus());

                    Project updatedProject = projectRepository.save(project);
                    return ResponseEntity.ok(updatedProject);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(project -> {
                    projectRepository.delete(project);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
