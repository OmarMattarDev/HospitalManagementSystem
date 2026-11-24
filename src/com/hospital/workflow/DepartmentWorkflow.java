
package com.hospital.workflow;

import com.hospital.dao.DepartmentDAO;
import com.hospital.model.Department;

import java.sql.Connection;
import java.sql.SQLException;

public class DepartmentWorkflow {

    private final DepartmentDAO departmentDAO;

    public DepartmentWorkflow(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.departmentDAO = new DepartmentDAO(connection);
    }

    public Long createDepartment(Department department)
            throws SQLException {

        if (department == null) {
            throw new IllegalArgumentException(
                    "Department cannot be null."
            );
        }

        String departmentName = department.getDepartmentName();

        if (departmentName == null || departmentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Department name cannot be empty."
            );
        }

        Department existingDepartment =
                departmentDAO.getDepartmentByName(departmentName.trim());

        if (existingDepartment != null) {
            throw new IllegalStateException(
                    "A department with this name already exists."
            );
        }

        return departmentDAO.addDepartment(department);
    }
}
