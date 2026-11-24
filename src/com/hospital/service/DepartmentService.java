
package com.hospital.service;

import com.hospital.dao.DepartmentDAO;
import com.hospital.model.Department;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class DepartmentService {

    private final DepartmentDAO departmentDAO;

    public DepartmentService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null.");
        }

        this.departmentDAO = new DepartmentDAO(connection);
    }

    public Long addDepartment(Department department) throws SQLException {
        validateDepartment(department);
        return departmentDAO.addDepartment(department);
    }

    public Department getDepartmentById(Long departmentId)
            throws SQLException {

        validateId(departmentId, "Department ID");
        return departmentDAO.getDepartmentById(departmentId);
    }

    public Department getDepartmentByName(String departmentName)
            throws SQLException {

        if (departmentName == null || departmentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Department name cannot be empty."
            );
        }

        return departmentDAO.getDepartmentByName(departmentName.trim());
    }

    public List<Department> getAllDepartments() throws SQLException {
        return departmentDAO.getAllDepartments();
    }

    public boolean updateDepartment(Department department)
            throws SQLException {

        validateDepartment(department);
        validateId(department.getDepartmentId(), "Department ID");

        return departmentDAO.updateDepartment(department);
    }

    public boolean deleteDepartment(Long departmentId)
            throws SQLException {

        validateId(departmentId, "Department ID");
        return departmentDAO.deleteDepartment(departmentId);
    }

    public List<Department> getDepartmentsSortedByName()
            throws SQLException {

        return getAllDepartments().stream()
                .sorted(Comparator.comparing(Department::getDepartmentName))
                .toList();
    }

    private void validateDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException(
                    "Department cannot be null."
            );
        }
    }

    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero."
            );
        }
    }
}
