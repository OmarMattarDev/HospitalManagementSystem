
package com.hospital.dao;

import com.hospital.model.Doctor;
import com.hospital.model.enums.Gender;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // Fields
    private final Connection connection;

    // Constructor
    public DoctorDAO(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        this.connection = connection;
    }

    // Add Doctor
    public Long addDoctor(Doctor doctor) throws SQLException {

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
        }

        String personSQL = """
                INSERT INTO persons
                (first_name, last_name, gender, date_of_birth,
                 phone, email, address)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String doctorSQL = """
                INSERT INTO doctors
                (doctor_id, department_id, specialization,
                 license_number, consultation_fee, hire_date, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Long doctorId;

        try (PreparedStatement ps = connection.prepareStatement(
                personSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getGender().name());
            ps.setDate(4, Date.valueOf(doctor.getDateOfBirth()));
            ps.setString(5, doctor.getPhone());
            ps.setString(6, doctor.getEmail());
            ps.setString(7, doctor.getAddress());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (!keys.next()) {
                    throw new SQLException(
                            "Failed to retrieve generated doctor ID.");
                }

                doctorId = keys.getLong(1);
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(doctorSQL)) {

            ps.setLong(1, doctorId);

            if (doctor.getDepartmentId() == null) {
                ps.setNull(2, Types.BIGINT);
            } else {
                ps.setLong(2, doctor.getDepartmentId());
            }

            ps.setString(3, doctor.getSpecialization());
            ps.setString(4, doctor.getLicenseNumber());
            ps.setBigDecimal(5, doctor.getConsultationFee());
            ps.setDate(6, Date.valueOf(doctor.getHireDate()));
            ps.setBoolean(7, doctor.isActive());

            ps.executeUpdate();
        }

        return doctorId;
    }

    // Get Doctor By ID
    public Doctor getDoctorById(Long doctorId) throws SQLException {

        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("Invalid doctor ID.");
        }

        String sql = """
                SELECT p.person_id, p.first_name, p.last_name,
                       p.gender, p.date_of_birth, p.phone,
                       p.email, p.address, p.created_at,
                       d.department_id, d.specialization,
                       d.license_number, d.consultation_fee,
                       d.hire_date, d.is_active
                FROM persons p
                JOIN doctors d
                    ON p.person_id = d.doctor_id
                WHERE p.person_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapDoctor(rs);
                }
            }
        }

        return null;
    }

    // Get All Doctors
    public List<Doctor> getAllDoctors() throws SQLException {

        List<Doctor> doctors = new ArrayList<>();

        String sql = """
                SELECT p.person_id, p.first_name, p.last_name,
                       p.gender, p.date_of_birth, p.phone,
                       p.email, p.address, p.created_at,
                       d.department_id, d.specialization,
                       d.license_number, d.consultation_fee,
                       d.hire_date, d.is_active
                FROM persons p
                JOIN doctors d
                    ON p.person_id = d.doctor_id
                ORDER BY p.person_id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }

        return doctors;
    }

    // Get Doctors By Department
    public List<Doctor> getDoctorsByDepartment(Long departmentId)
            throws SQLException {

        if (departmentId == null || departmentId <= 0) {
            throw new IllegalArgumentException("Invalid department ID.");
        }

        List<Doctor> doctors = new ArrayList<>();

        String sql = """
                SELECT p.person_id, p.first_name, p.last_name,
                       p.gender, p.date_of_birth, p.phone,
                       p.email, p.address, p.created_at,
                       d.department_id, d.specialization,
                       d.license_number, d.consultation_fee,
                       d.hire_date, d.is_active
                FROM persons p
                JOIN doctors d
                    ON p.person_id = d.doctor_id
                WHERE d.department_id = ?
                ORDER BY p.first_name, p.last_name
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, departmentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }

        return doctors;
    }

    // Update Doctor
    public boolean updateDoctor(Doctor doctor) throws SQLException {

        if (doctor == null || doctor.getPersonId() == null) {
            throw new IllegalArgumentException("Invalid doctor.");
        }

        String personSQL = """
                UPDATE persons
                SET first_name = ?, last_name = ?, gender = ?,
                    date_of_birth = ?, phone = ?, email = ?, address = ?
                WHERE person_id = ?
                """;

        String doctorSQL = """
                UPDATE doctors
                SET department_id = ?, specialization = ?,
                    license_number = ?, consultation_fee = ?,
                    hire_date = ?, is_active = ?
                WHERE doctor_id = ?
                """;

        int personRows;

        try (PreparedStatement ps = connection.prepareStatement(personSQL)) {

            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getGender().name());
            ps.setDate(4, Date.valueOf(doctor.getDateOfBirth()));
            ps.setString(5, doctor.getPhone());
            ps.setString(6, doctor.getEmail());
            ps.setString(7, doctor.getAddress());
            ps.setLong(8, doctor.getPersonId());

            personRows = ps.executeUpdate();
        }

        if (personRows == 0) {
            return false;
        }

        try (PreparedStatement ps = connection.prepareStatement(doctorSQL)) {

            if (doctor.getDepartmentId() == null) {
                ps.setNull(1, Types.BIGINT);
            } else {
                ps.setLong(1, doctor.getDepartmentId());
            }

            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getLicenseNumber());
            ps.setBigDecimal(4, doctor.getConsultationFee());
            ps.setDate(5, Date.valueOf(doctor.getHireDate()));
            ps.setBoolean(6, doctor.isActive());
            ps.setLong(7, doctor.getPersonId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete Doctor
    public boolean deleteDoctor(Long doctorId) throws SQLException {

        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("Invalid doctor ID.");
        }

        String doctorSQL = """
                DELETE FROM doctors
                WHERE doctor_id = ?
                """;

        String personSQL = """
                DELETE FROM persons
                WHERE person_id = ?
                """;

        int deletedRows;

        try (PreparedStatement ps = connection.prepareStatement(doctorSQL)) {

            ps.setLong(1, doctorId);
            deletedRows = ps.executeUpdate();
        }

        if (deletedRows == 0) {
            return false;
        }

        try (PreparedStatement ps = connection.prepareStatement(personSQL)) {

            ps.setLong(1, doctorId);
            ps.executeUpdate();
        }

        return true;
    }

    // ResultSet Mapping
    private Doctor mapDoctor(ResultSet rs) throws SQLException {

        long departmentValue = rs.getLong("department_id");

        Long departmentId = rs.wasNull()
                ? null
                : departmentValue;

        return new Doctor(
                rs.getLong("person_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                Gender.valueOf(rs.getString("gender")),
                rs.getDate("date_of_birth").toLocalDate(),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                departmentId,
                rs.getString("specialization"),
                rs.getString("license_number"),
                rs.getBigDecimal("consultation_fee"),
                rs.getDate("hire_date").toLocalDate(),
                rs.getBoolean("is_active")
        );
    }
}
