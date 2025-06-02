package com.csc3402.lab.lab09.config;

import com.csc3402.lab.lab09.model.Department;
import com.csc3402.lab.lab09.model.Project;
import com.csc3402.lab.lab09.model.Staff;
import com.csc3402.lab.lab09.service.DepartmentService;
import com.csc3402.lab.lab09.service.ProjectService;
import com.csc3402.lab.lab09.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private StaffService staffService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private ProjectService projectService;

    @Override
    public void run(String... args) throws Exception {
        // Load initial sample data
        loadSampleData();
    }

    private void loadSampleData() {
        // Check if we already have data
        if (departmentService.findAll().isEmpty()) {
            // Add sample departments
            Department itDept = departmentService.save(new Department("Information Technology", "IT department responsible for technical infrastructure"));
            Department hrDept = departmentService.save(new Department("Human Resources", "HR department for employee management"));
            Department salesDept = departmentService.save(new Department("Sales", "Sales and marketing department"));
            
            // Add sample projects
            Project webProject = projectService.save(new Project("Website Redesign", "Modernize the company website", "In Progress"));
            Project crmProject = projectService.save(new Project("CRM Implementation", "Implement a new customer relationship management system", "Planning"));
            Project mobileProject = projectService.save(new Project("Mobile App", "Develop a mobile application for customers", "In Progress"));
            Project analyticsProject = projectService.save(new Project("Data Analytics Platform", "Build a data analytics platform", "Not Started"));
            
            // Add sample staff members with departments
            Staff john = new Staff("John Doe", "john.doe@example.com", "Software Engineer", itDept);
            Staff jane = new Staff("Jane Smith", "jane.smith@example.com", "Project Manager", itDept);
            Staff michael = new Staff("Michael Brown", "michael.brown@example.com", "UX Designer", itDept);
            Staff emily = new Staff("Emily Wilson", "emily.wilson@example.com", "HR Specialist", hrDept);
            Staff robert = new Staff("Robert Taylor", "robert.taylor@example.com", "Sales Executive", salesDept);
            
            // Assign projects to staff
            john.addProject(webProject);
            john.addProject(mobileProject);
            
            jane.addProject(webProject);
            jane.addProject(crmProject);
            jane.addProject(analyticsProject);
            
            michael.addProject(webProject);
            michael.addProject(mobileProject);
            
            emily.addProject(crmProject);
            
            robert.addProject(crmProject);
            robert.addProject(analyticsProject);
            
            // Save staff with their project associations
            staffService.save(john);
            staffService.save(jane);
            staffService.save(michael);
            staffService.save(emily);
            staffService.save(robert);
            
            System.out.println("Sample data loaded successfully!");
        }
    }
}
