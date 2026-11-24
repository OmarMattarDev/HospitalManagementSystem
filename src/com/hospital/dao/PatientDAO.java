
package com.hospital.dao;

import com.hospital.model.Patient;
import com.hospital.model.enums.BloodType;
import com.hospital.model.enums.Gender;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Fields
    private final Connection connection;

    // Constructor
    public PatientDAO(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        this.connection = connection;
    }

    // Add Patient
    public Long addPatient(Patient patient) throws SQLException {

        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        String personSQL = """
                INSERT INTO persons
                (first_name, last_name, gender, date_of_birth,
                 phone, email, address)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String patientSQL = """
                INSERT INTO patients
                (patient_id, blood_type, emergency_contact_name,
                 emergency_contact_phone)
                VALUES (?, ?, ?, ?)
                """;

        Long patientId;

        try (PreparedStatement ps = connection.prepareStatement(
                personSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getGender().name());
            ps.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            ps.setString(7, patient.getAddress());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (!keys.next()) {
                    throw new SQLException(
                            "Failed to retrieve generated patient ID.");
                }

                patientId = keys.getLong(1);
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(patientSQL)) {

            ps.setLong(1, patientId);

            if (patient.getBloodType() == null) {
                ps.setNull(2, Types.VARCHAR);
            } else {
                ps.setString(2, patient.getBloodType().name());
            }

            ps.setString(3, patient.getEmergencyContactName());
            ps.setString(4, patient.getEmergencyContactPhone());

            ps.executeUpdate();
        }

        return patientId;
    }

    // Get Patient By ID
    public Patient getPatientById(Long patientId) throws SQLException {

        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("Invalid patient ID.");
        }

        String sql = """
                SELECT p.person_id, p.first_name, p.last_name,
                       p.gender, p.date_of_birth, p.phone,
                       p.email, p.address, p.created_at,
                       pt.blood_type, pt.emergency_contact_name,
                       pt.emergency_contact_phone
                FROM persons p
                JOIN patients pt
                    ON p.person_id = pt.patient_id
                WHERE p.person_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapPatient(rs);
                }
            }
        }

        return null;
    }

    // Get All Patients
    public List<Patient> getAllPatients() throws SQLException {

        List<Patient> patients = new ArrayList<>();

        String sql = """
                SELECT p.person_id, p.first_name, p.last_name,
                       p.gender, p.date_of_birth, p.phone,
                       p.email, p.address, p.created_at,
                       pt.blood_type, pt.emergency_contact_name,
                       pt.emergency_contact_phone
                FROM persons p
                JOIN patients pt
                    ON p.person_id = pt.patient_id
                ORDER BY p.person_id
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }

        return patients;
    }

    // Update Patient
    public boolean updatePatient(Patient patient) throws SQLException {

        if (patient == null || patient.getPersonId() == null) {
            throw new IllegalArgumentException("Invalid patient.");
        }

        String personSQL = """
                UPDATE persons
                SET first_name = ?, last_name = ?, gender = ?,
                    date_of_birth = ?, phone = ?, email = ?, address = ?
                WHERE person_id = ?
                """;

        String patientSQL = """
                UPDATE patients
                SET blood_type = ?, emergency_contact_name = ?,
                    emergency_contact_phone = ?
                WHERE patient_id = ?
                """;

        int personRows;

        try (PreparedStatement ps = connection.prepareStatement(personSQL)) {

            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getGender().name());
            ps.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            ps.setString(5, patient.getPhone());
            ps.setString(6, patient.getEmail());
            ps.setString(7, patient.getAddress());
            ps.setLong(8, patient.getPersonId());

            personRows = ps.executeUpdate();
        }

        if (personRows == 0) {
            return false;
        }

        try (PreparedStatement ps = connection.prepareStatement(patientSQL)) {

            if (patient.getBloodType() == null) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, patient.getBloodType().name());
            }

            ps.setString(2, patient.getEmergencyContactName());
            ps.setString(3, patient.getEmergencyContactPhone());
            ps.setLong(4, patient.getPersonId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete Patient
    public boolean deletePatient(Long patientId) throws SQLException {

        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("Invalid patient ID.");
        }

        String patientSQL = """
                DELETE FROM patients
                WHERE patient_id = ?
                """;

        String personSQL = """
                DELETE FROM persons
                WHERE person_id = ?
                """;

        int deletedRows;

        try (PreparedStatement ps = connection.prepareStatement(patientSQL)) {

            ps.setLong(1, patientId);
            deletedRows = ps.executeUpdate();
        }

        if (deletedRows == 0) {
            return false;
        }

        try (PreparedStatement ps = connection.prepareStatement(personSQL)) {

            ps.setLong(1, patientId);
            ps.executeUpdate();
        }

        return true;
    }

    // ResultSet Mapping
    private Patient mapPatient(ResultSet rs) throws SQLException {

        String bloodTypeValue = rs.getString("blood_type");

        BloodType bloodType = bloodTypeValue == null
                ? null
                : BloodType.valueOf(bloodTypeValue);

        return new Patient(
                rs.getLong("person_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                Gender.valueOf(rs.getString("gender")),
                rs.getDate("date_of_birth").toLocalDate(),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                bloodType,
                rs.getString("emergency_contact_name"),
                rs.getString("emergency_contact_phone")
        );
    }
}
