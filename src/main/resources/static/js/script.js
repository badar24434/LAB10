document.addEventListener('DOMContentLoaded', function() {
    // Global variables
    const apiBaseUrl = '/api/staffs';
    const departmentsApiUrl = '/api/departments';
    const projectsApiUrl = '/api/projects';
    const staffForm = document.getElementById('staffForm');
    const staffTableBody = document.getElementById('staffTableBody');
    const departmentTableBody = document.getElementById('departmentTableBody');
    const projectTableBody = document.getElementById('projectTableBody');
    const formTitle = document.getElementById('formTitle');
    const submitBtn = document.getElementById('submitBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const clearBtn = document.getElementById('clearBtn');
    const alertSuccess = document.getElementById('alertSuccess');
    const alertError = document.getElementById('alertError');
    const loadingIndicator = document.getElementById('loading');
    
    // Form fields
    const staffIdField = document.getElementById('staffId');
    const nameField = document.getElementById('name');
    const emailField = document.getElementById('email');
    const positionField = document.getElementById('position');
    const departmentField = document.getElementById('department');
    const projectsSelect = document.getElementById('projectsSelect');
    const assignedProjects = document.getElementById('assignedProjects');
    const addProjectBtn = document.getElementById('addProjectBtn');
    const removeProjectBtn = document.getElementById('removeProjectBtn');
    
    // Error message elements
    const nameError = document.getElementById('nameError');
    const emailError = document.getElementById('emailError');
    const positionError = document.getElementById('positionError');
    const departmentError = document.getElementById('departmentError');
    const projectsError = document.getElementById('projectsError');
    
    // Store departments and projects data
    let departmentsData = [];
    let projectsData = [];
    let selectedProjects = new Map(); // Map to store selected project IDs and objects
    
    // Initial load
    loadDepartmentsData();
    loadProjectsData();
    loadStaffData();
    
    // Event Listeners
    staffForm.addEventListener('submit', handleFormSubmit);
    clearBtn.addEventListener('click', clearForm);
    cancelBtn.addEventListener('click', cancelEdit);
    addProjectBtn.addEventListener('click', addSelectedProject);
    removeProjectBtn.addEventListener('click', removeSelectedProject);
    
    // Project handling functions - improved version
    function addSelectedProject() {
        const selectedOptions = Array.from(projectsSelect.selectedOptions);
        
        if (selectedOptions.length === 0) {
            showFieldError(projectsError, "Please select a project to add");
            setTimeout(() => { projectsError.style.display = 'none'; }, 2000);
            return;
        }
        
        selectedOptions.forEach(option => {
            const projectId = option.value;
            const projectName = option.text;
            
            // Don't add if already in the assigned projects
            if (!selectedProjects.has(Number(projectId))) {
                // Add to map
                const projectObj = projectsData.find(p => p.id == projectId);
                if (projectObj) {
                    selectedProjects.set(Number(projectId), projectObj);
                    
                    // Add option to assigned projects
                    const newOption = document.createElement('option');
                    newOption.value = projectId;
                    newOption.textContent = projectName;
                    
                    // Insert in alphabetical order
                    insertOptionAlphabetically(assignedProjects, newOption);
                }
            }
        });
        
        // Remove selected options from available projects
        selectedOptions.forEach(option => {
            projectsSelect.removeChild(option);
        });
        
        // Show brief success message
        showSuccess(`${selectedOptions.length} project(s) added`);
    }
    
    function removeSelectedProject() {
        const selectedOptions = Array.from(assignedProjects.selectedOptions);
        
        if (selectedOptions.length === 0) {
            showFieldError(projectsError, "Please select a project to remove");
            setTimeout(() => { projectsError.style.display = 'none'; }, 2000);
            return;
        }
        
        selectedOptions.forEach(option => {
            const projectId = option.value;
            const projectName = option.text;
            
            // Remove from map
            selectedProjects.delete(Number(projectId));
            
            // Add back to available projects
            const newOption = document.createElement('option');
            newOption.value = projectId;
            newOption.textContent = projectName;
            
            // Insert in alphabetical order
            insertOptionAlphabetically(projectsSelect, newOption);
        });
        
        // Remove from assigned projects
        selectedOptions.forEach(option => {
            assignedProjects.removeChild(option);
        });
        
        // Show brief success message
        showSuccess(`${selectedOptions.length} project(s) removed`);
    }
    
    // Helper function to insert options alphabetically
    function insertOptionAlphabetically(selectElement, newOption) {
        const options = Array.from(selectElement.options);
        
        // If no options or should be first
        if (options.length === 0 || newOption.text < options[0].text) {
            selectElement.add(newOption, 0);
            return;
        }
        
        // Find the right position
        for (let i = 0; i < options.length; i++) {
            if (i === options.length - 1 || newOption.text < options[i + 1].text) {
                selectElement.add(newOption, i + 1);
                return;
            }
        }
        
        // If we get here, add to the end
        selectElement.add(newOption);
    }
    
    // Functions
    function loadDepartmentsData() {
        showLoading(true);
        
        fetch(departmentsApiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch departments data');
                }
                return response.json();
            })
            .then(data => {
                departmentsData = data;
                populateDepartmentDropdown();
                renderDepartmentTable(data);
                showLoading(false);
            })
            .catch(error => {
                showError('Error loading departments data: ' + error.message);
                showLoading(false);
            });
    }
    
    function loadProjectsData() {
        showLoading(true);
        
        fetch(projectsApiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch projects data');
                }
                return response.json();
            })
            .then(data => {
                projectsData = data;
                populateProjectsDropdown();
                renderProjectTable(data);
                showLoading(false);
            })
            .catch(error => {
                showError('Error loading projects data: ' + error.message);
                showLoading(false);
            });
    }
    
    function populateDepartmentDropdown() {
        departmentField.innerHTML = '<option value="">Select Department</option>';
        
        departmentsData.forEach(dept => {
            const option = document.createElement('option');
            option.value = dept.id;
            option.textContent = dept.name;
            departmentField.appendChild(option);
        });
    }
    
    // Enhanced populateProjectsDropdown function
    function populateProjectsDropdown() {
        projectsSelect.innerHTML = '';
        
        // Sort projects by name
        const sortedProjects = [...projectsData].sort((a, b) => a.name.localeCompare(b.name));
        
        // Check if any projects exist
        if (sortedProjects.length === 0) {
            const option = document.createElement('option');
            option.disabled = true;
            option.textContent = '-- No projects available --';
            projectsSelect.appendChild(option);
            return;
        }
        
        // Add projects that aren't already selected
        sortedProjects.forEach(project => {
            if (!selectedProjects.has(Number(project.id))) {
                const option = document.createElement('option');
                option.value = project.id;
                option.textContent = project.name;
                projectsSelect.appendChild(option);
            }
        });
        
        // If all projects are selected
        if (projectsSelect.options.length === 0) {
            const option = document.createElement('option');
            option.disabled = true;
            option.textContent = '-- All projects assigned --';
            projectsSelect.appendChild(option);
        }
    }
    
    function renderDepartmentTable(departments) {
        departmentTableBody.innerHTML = '';
        
        if (departments.length === 0) {
            const noDataRow = document.createElement('tr');
            noDataRow.innerHTML = '<td colspan="4" class="no-data">No departments found</td>';
            departmentTableBody.appendChild(noDataRow);
            return;
        }
        
        departments.forEach(dept => {
            const row = document.createElement('tr');
            
            row.innerHTML = `
                <td>${dept.id}</td>
                <td>${dept.name}</td>
                <td>${dept.description || 'N/A'}</td>
                <td>${dept.staffs ? dept.staffs.length : 0}</td>
            `;
            
            departmentTableBody.appendChild(row);
        });
    }
    
    function renderProjectTable(projects) {
        projectTableBody.innerHTML = '';
        
        if (projects.length === 0) {
            const noDataRow = document.createElement('tr');
            noDataRow.innerHTML = '<td colspan="5" class="no-data">No projects found</td>';
            projectTableBody.appendChild(noDataRow);
            return;
        }
        
        projects.forEach(project => {
            const row = document.createElement('tr');
            
            row.innerHTML = `
                <td>${project.id}</td>
                <td>${project.name}</td>
                <td>${project.description || 'N/A'}</td>
                <td>${project.status || 'Not Set'}</td>
                <td>${project.staffs ? project.staffs.length : 0}</td>
            `;
            
            projectTableBody.appendChild(row);
        });
    }
    
    function loadStaffData() {
        showLoading(true);
        
        fetch(apiBaseUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch staff data');
                }
                return response.json();
            })
            .then(data => {
                renderStaffTable(data);
                showLoading(false);
            })
            .catch(error => {
                showError('Error loading staff data: ' + error.message);
                showLoading(false);
            });
    }
    
    function renderStaffTable(staffs) {
        staffTableBody.innerHTML = '';
        
        if (staffs.length === 0) {
            const noDataRow = document.createElement('tr');
            noDataRow.innerHTML = '<td colspan="7" class="no-data">No staff members found</td>';
            staffTableBody.appendChild(noDataRow);
            return;
        }
        
        staffs.forEach(staff => {
            // Handle department display
            let departmentName = 'Not Assigned';
            let departmentId = null;
            
            // If department is not null, convert it to a Number (works whether it's a primitive or object)
            if (staff.department != null) {
                departmentId = Number(staff.department);
            }
            
            if (departmentId) {
                // Find matching department from our stored departmentsData
                const dept = departmentsData.find(d => Number(d.id) === departmentId);
                if (dept) {
                    departmentName = dept.name;
                }
            }
            
            // Handle projects display
            let projectsDisplay = 'None';
            if (staff.projects && staff.projects.length > 0) {
                const projectNames = [];
                staff.projects.forEach(projectId => {
                    const project = projectsData.find(p => p.id === Number(projectId));
                    if (project) {
                        projectNames.push(project.name);
                    }
                });
                
                if (projectNames.length > 0) {
                    projectsDisplay = projectNames.join(', ');
                }
            }
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${staff.id}</td>
                <td>${staff.name}</td>
                <td>${staff.email}</td>
                <td>${staff.position}</td>
                <td>${departmentName}</td>
                <td>${projectsDisplay}</td>
                <td class="action-buttons">
                    <button class="btn btn-success btn-sm edit-btn" data-id="${staff.id}">Edit</button>
                    <button class="btn btn-danger btn-sm delete-btn" data-id="${staff.id}">Delete</button>
                </td>
            `;
            staffTableBody.appendChild(row);
            
            const editBtn = row.querySelector('.edit-btn');
            const deleteBtn = row.querySelector('.delete-btn');
            editBtn.addEventListener('click', () => editStaff(staff));
            deleteBtn.addEventListener('click', () => deleteStaff(staff.id));
        });
    }
    
    function handleFormSubmit(e) {
        e.preventDefault();
        clearErrors();
        
        // Create an array of project objects from the selected projects map
        const projectsArray = Array.from(selectedProjects.values()).map(project => {
            return { id: project.id };
        });
        
        const staffData = {
            name: nameField.value.trim(),
            email: emailField.value.trim(),
            position: positionField.value.trim(),
            department: departmentField.value ? { id: departmentField.value } : null,
            projects: projectsArray
        };
        
        // Validate form data
        let hasErrors = false;
        
        if (staffData.name.length < 2) {
            showFieldError(nameError, 'Name must be at least 2 characters');
            hasErrors = true;
        }
        
        if (!validateEmail(staffData.email)) {
            showFieldError(emailError, 'Please enter a valid email address');
            hasErrors = true;
        }
        
        if (staffData.position.length < 2) {
            showFieldError(positionError, 'Position must be at least 2 characters');
            hasErrors = true;
        }
        
        if (!departmentField.value) {
            showFieldError(departmentError, 'Please select a department');
            hasErrors = true;
        }
        
        if (hasErrors) return;
        
        const isEditing = !!staffIdField.value;
        const url = isEditing ? `${apiBaseUrl}/${staffIdField.value}` : apiBaseUrl;
        const method = isEditing ? 'PUT' : 'POST';
        
        showLoading(true);
        
        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(staffData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Failed to save staff data');
                    });
                }
                return response.json();
            })
            .then(data => {
                showSuccess(isEditing ? 'Staff updated successfully' : 'Staff added successfully');
                clearForm();
                loadStaffData();
                loadDepartmentsData();
                loadProjectsData();
            })
            .catch(error => {
                showError(error.message);
            })
            .finally(() => {
                showLoading(false);
            });
    }
    
    function editStaff(staff) {
        // Set form to edit mode
        formTitle.textContent = 'Edit Staff Member';
        submitBtn.textContent = 'Update Staff';
        cancelBtn.style.display = 'block';
        
        // Populate form fields
        staffIdField.value = staff.id;
        nameField.value = staff.name;
        emailField.value = staff.email;
        positionField.value = staff.position;
        
        // Set department if exists
        if (staff.department) {
            departmentField.value = staff.department;
        } else {
            departmentField.value = '';
        }
        
        // Clear existing projects selection
        selectedProjects.clear();
        assignedProjects.innerHTML = '';
        
        // Handle projects assignment
        if (staff.projects && staff.projects.length > 0) {
            // Sort projects by name for display
            const projects = [];
            
            staff.projects.forEach(projectId => {
                const projectObj = projectsData.find(p => p.id === Number(projectId));
                if (projectObj) {
                    projects.push(projectObj);
                    selectedProjects.set(Number(projectId), projectObj);
                }
            });
            
            // Sort and add to the assigned projects
            projects.sort((a, b) => a.name.localeCompare(b.name)).forEach(project => {
                const option = document.createElement('option');
                option.value = project.id;
                option.textContent = project.name;
                assignedProjects.add(option);
            });
        }
        
        // Refresh available projects dropdown
        populateProjectsDropdown();
        
        // Scroll to form
        staffForm.scrollIntoView({ behavior: 'smooth' });
    }
    
    function deleteStaff(staffId) {
        if (!confirm('Are you sure you want to delete this staff member?')) {
            return;
        }
        
        showLoading(true);
        
        fetch(`${apiBaseUrl}/${staffId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete staff member');
                }
                showSuccess('Staff deleted successfully');
                loadStaffData();
                loadDepartmentsData();
                loadProjectsData();
            })
            .catch(error => {
                showError('Error deleting staff: ' + error.message);
            })
            .finally(() => {
                showLoading(false);
            });
    }
    
    function clearForm() {
        staffForm.reset();
        staffIdField.value = '';
        formTitle.textContent = 'Add New Staff Member';
        submitBtn.textContent = 'Add Staff';
        cancelBtn.style.display = 'none';
        clearErrors();
        
        // Clear selected projects
        selectedProjects.clear();
        assignedProjects.innerHTML = '';
        populateProjectsDropdown();
    }
    
    function cancelEdit() {
        clearForm();
    }
    
    function showFieldError(element, message) {
        element.textContent = message;
        element.style.display = 'block';
    }
    
    function clearErrors() {
        nameError.style.display = 'none';
        emailError.style.display = 'none';
        positionError.style.display = 'none';
        departmentError.style.display = 'none';
        projectsError.style.display = 'none';
        alertError.style.display = 'none';
    }
    
    function showSuccess(message) {
        alertSuccess.textContent = message;
        alertSuccess.style.display = 'block';
        
        // Hide after 3 seconds
        setTimeout(() => {
            alertSuccess.style.display = 'none';
        }, 3000);
    }
    
    function showError(message) {
        alertError.textContent = message;
        alertError.style.display = 'block';
        
        // Hide after 5 seconds
        setTimeout(() => {
            alertError.style.display = 'none';
        }, 5000);
    }
    
    function showLoading(isLoading) {
        loadingIndicator.style.display = isLoading ? 'block' : 'none';
    }
    
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }
});
