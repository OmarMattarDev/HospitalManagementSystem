
package com.hospital.report;

import com.hospital.dao.DepartmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.model.Department;
import com.hospital.model.Doctor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepartmentReport {

    private final DepartmentDAO departmentDAO;
    private final DoctorDAO doctorDAO;

    public DepartmentReport(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.departmentDAO = new DepartmentDAO(connection);
        this.doctorDAO = new DoctorDAO(connection);
    }

    public long getTotalDepartments() throws SQLException {
        return departmentDAO.getAllDepartments().size();
    }

    public Map<String, Long> getDoctorsCountByDepartment()
            throws SQLException {

        List<Department> departments =
                departmentDAO.getAllDepartments();

        List<Doctor> doctors = doctorDAO.getAllDoctors();

        Map<String, Long> result = new LinkedHashMap<>();

        for (Department department : departments) {

            long doctorsCount = doctors.stream()
                    .filter(doctor ->
                            department.getDepartmentId()
                                    .equals(doctor.getDepartmentId())
                    )
                    .count();

            result.put(
                    department.getDepartmentName(),
                    doctorsCount
            );
        }

        return result;
    }

    public Map<String, Long> getActiveDoctorsCountByDepartment()
            throws SQLException {

        List<Department> departments =
                departmentDAO.getAllDepartments();

        List<Doctor> doctors = doctorDAO.getAllDoctors();

        Map<String, Long> result = new LinkedHashMap<>();

        for (Department department : departments) {

            long activeDoctorsCount = doctors.stream()
                    .filter(doctor ->
                            department.getDepartmentId()
                                    .equals(doctor.getDepartmentId())
                    )
                    .filter(Doctor::isActive)
                    .count();

            result.put(
                    department.getDepartmentName(),
                    activeDoctorsCount
            );
        }

        return result;
    }
}
