package com.csc3402.lab.lab09.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    @Column(name = "position", nullable = false)
    private String position;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    
    @ManyToMany
    @JoinTable(
        name = "staff_project",
        joinColumns = @JoinColumn(name = "staff_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnoreProperties("staffs")
    private Set<Project> projects = new HashSet<>();
    
    // Add a method to get department ID for serialization
    @JsonProperty("department")
    public Long getDepartmentId() {
        return department != null ? department.getId() : null;
    }
    
    // Get project IDs for serialization
    @JsonProperty("projects")
    public Set<Long> getProjectIds() {
        Set<Long> projectIds = new HashSet<>();
        if (projects != null) {
            for (Project project : projects) {
                projectIds.add(project.getId());
            }
        }
        return projectIds;
    }
    
    // This setter is for JSON deserialization only
    @JsonProperty("department")
    public void setDepartmentId(Long departmentId) {
        if (departmentId != null) {
            Department dept = new Department();
            dept.setId(departmentId);
            this.department = dept;
        } else {
            this.department = null;
        }
    }
    
    // This is to ignore the projects field during deserialization
    @JsonSetter("projects")
    public void setProjectIds(Set<Long> projectIds) {
        // This will be handled by the controller
    }
    
    // Default constructor
    public Staff() {
    }

    // Constructor with parameters
    public Staff(String name, String email, String position) {
        this.name = name;
        this.email = email;
        this.position = position;
    }
    
    // Constructor with department
    public Staff(String name, String email, String position, Department department) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.department = department;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    @JsonIgnore
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    @JsonIgnore
    public Set<Project> getProjects() {
        return projects;
    }
    
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
    
    public void addProject(Project project) {
        this.projects.add(project);
        project.getStaffs().add(this);
    }
    
    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getStaffs().remove(this);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", department=" + (department != null ? department.getName() : "null") +
                '}';
    }
}