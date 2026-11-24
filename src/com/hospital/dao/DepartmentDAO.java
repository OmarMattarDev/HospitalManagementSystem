
package com.hospital.dao;

import com.hospital.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // Fields
    private final Connection connection;

    // Constructor
    public DepartmentDAO(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        this.connection = connection;
    }

    // Add Department
    public Long addDepartment(Department department) throws SQLException {

        if (department == null) {
            throw new IllegalArgumentException(
                    "Department cannot be null.");
        }

        String sql = """
                INSERT INTO departments
                (department_name, description, location)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, department.getDepartmentName());
            ps.setString(2, department.getDescription());
            ps.setString(3, department.getLocation());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException(
                "Failed to retrieve generated department ID.");
    }

    // Get Department By ID
    public Department getDepartmentById(Long departmentId)
            throws SQLException {

        if (departmentId == null || departmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid department ID.");
        }

        String sql = """
                SELECT *
                FROM departments
                WHERE department_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, departmentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapDepartment(rs);
                }
            }
        }

        return null;
    }

    // Get Department By Name
    public Department getDepartmentByName(String departmentName)
            throws SQLException {

        if (departmentName == null || departmentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Department name cannot be empty.");
        }

        String sql = """
                SELECT *
                FROM departments
                WHERE department_name = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, departmentName.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapDepartment(rs);
                }
            }
        }

        return null;
    }

    // Get All Departments
    public List<Department> getAllDepartments() throws SQLException {

        List<Department> departments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM departments
                ORDER BY department_name
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(mapDepartment(rs));
            }
        }

        return departments;
    }

    // Update Department
    public boolean updateDepartment(Department department)
            throws SQLException {

        if (department == null || department.getDepartmentId() == null) {
            throw new IllegalArgumentException(
                    "Invalid department.");
        }

        String sql = """
                UPDATE departments
                SET department_name = ?,
                    description = ?,
                    location = ?
                WHERE department_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, department.getDepartmentName());
            ps.setString(2, department.getDescription());
            ps.setString(3, department.getLocation());
            ps.setLong(4, department.getDepartmentId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete Department
    public boolean deleteDepartment(Long departmentId)
            throws SQLException {

        if (departmentId == null || departmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid department ID.");
        }

        String sql = """
                DELETE FROM departments
                WHERE department_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, departmentId);

            return ps.executeUpdate() > 0;
        }
    }

    // ResultSet Mapping
    private Department mapDepartment(ResultSet rs) throws SQLException {

        return new Department(
                rs.getLong("department_id"),
                rs.getString("department_name"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
