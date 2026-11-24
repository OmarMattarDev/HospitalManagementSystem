
package com.hospital.model;

import java.time.LocalDateTime;

public class Department {

    // Fields
    private Long departmentId;
    private String departmentName;
    private String description;
    private String location;
    private LocalDateTime createdAt;

    // Constructor for new departments
    public Department(String departmentName,
                      String description, String location) {

        setDepartmentName(departmentName);
        setDescription(description);
        setLocation(location);

        this.createdAt = LocalDateTime.now();
    }

    // Constructor for database mapping
    public Department(Long departmentId, String departmentName,
                      String description, String location,
                      LocalDateTime createdAt) {

        if (departmentId == null || departmentId <= 0) {
            throw new IllegalArgumentException("Invalid department ID.");
        }

        this.departmentId = departmentId;

        setDepartmentName(departmentName);
        setDescription(description);
        setLocation(location);

        if (createdAt == null || createdAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid creation date.");
        }

        this.createdAt = createdAt;
    }

    // Getters
    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setDepartmentName(String departmentName) {

        if (departmentName == null || departmentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Department name cannot be empty.");
        }

        if (departmentName.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Department name cannot exceed 100 characters.");
        }

        this.departmentName = departmentName.trim();
    }

    public void setDescription(String description) {

        if (description != null && description.trim().length() > 255) {
            throw new IllegalArgumentException(
                    "Description cannot exceed 255 characters.");
        }

        this.description = description == null ? null : description.trim();
    }

    public void setLocation(String location) {

        if (location != null && location.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Location cannot exceed 100 characters.");
        }

        this.location = location == null ? null : location.trim();
    }

    // Object Methods
    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Department)) {
            return false;
        }

        Department department = (Department) obj;

        return departmentId != null
                && departmentId.equals(department.departmentId);
    }

    @Override
    public int hashCode() {
        return departmentId == null
                ? System.identityHashCode(this)
                : departmentId.hashCode();
    }
}
