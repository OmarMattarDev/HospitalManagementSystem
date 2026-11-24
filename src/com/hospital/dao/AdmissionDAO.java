
package com.hospital.dao;

import com.hospital.model.Admission;
import com.hospital.model.enums.AdmissionStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdmissionDAO {

    // Fields
    private final Connection connection;

    // Constructor
    public AdmissionDAO(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        this.connection = connection;
    }

    // Add Admission
    public Long addAdmission(Admission admission) throws SQLException {

        if (admission == null) {
            throw new IllegalArgumentException(
                    "Admission cannot be null.");
        }

        String sql = """
                INSERT INTO admissions
                (patient_id, admission_date, discharge_date,
                 status, daily_cost, room_number, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, admission.getPatientId());

            ps.setTimestamp(2, Timestamp.valueOf(
                    admission.getAdmissionDate()));

            if (admission.getDischargeDate() == null) {
                ps.setNull(3, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(3, Timestamp.valueOf(
                        admission.getDischargeDate()));
            }

            ps.setString(4, admission.getStatus().name());
            ps.setBigDecimal(5, admission.getDailyCost());
            ps.setString(6, admission.getRoomNumber());
            ps.setString(7, admission.getNotes());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException(
                "Failed to retrieve generated admission ID.");
    }

    // Get Admission By ID
    public Admission getAdmissionById(Long admissionId)
            throws SQLException {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid admission ID.");
        }

        String sql = """
                SELECT *
                FROM admissions
                WHERE admission_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, admissionId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapAdmission(rs);
                }
            }
        }

        return null;
    }

    // Get All Admissions
    public List<Admission> getAllAdmissions() throws SQLException {

        List<Admission> admissions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM admissions
                ORDER BY admission_date DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                admissions.add(mapAdmission(rs));
            }
        }

        return admissions;
    }

    // Get Admissions By Patient
    public List<Admission> getAdmissionsByPatient(Long patientId)
            throws SQLException {

        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("Invalid patient ID.");
        }

        List<Admission> admissions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM admissions
                WHERE patient_id = ?
                ORDER BY admission_date DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    admissions.add(mapAdmission(rs));
                }
            }
        }

        return admissions;
    }

    // Get Admissions By Status
    public List<Admission> getAdmissionsByStatus(
            AdmissionStatus status) throws SQLException {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Admission status cannot be null.");
        }

        List<Admission> admissions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM admissions
                WHERE status = ?
                ORDER BY admission_date DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    admissions.add(mapAdmission(rs));
                }
            }
        }

        return admissions;
    }

    // Get Active Admissions
    public List<Admission> getActiveAdmissions() throws SQLException {

        List<Admission> admissions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM admissions
                WHERE status IN ('ADMITTED', 'ICU')
                ORDER BY admission_date DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                admissions.add(mapAdmission(rs));
            }
        }

        return admissions;
    }

    // Update Admission
    public boolean updateAdmission(Admission admission)
            throws SQLException {

        if (admission == null || admission.getAdmissionId() == null) {
            throw new IllegalArgumentException(
                    "Invalid admission.");
        }

        String sql = """
                UPDATE admissions
                SET patient_id = ?,
                    admission_date = ?,
                    discharge_date = ?,
                    status = ?,
                    daily_cost = ?,
                    room_number = ?,
                    notes = ?
                WHERE admission_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, admission.getPatientId());

            ps.setTimestamp(2, Timestamp.valueOf(
                    admission.getAdmissionDate()));

            if (admission.getDischargeDate() == null) {
                ps.setNull(3, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(3, Timestamp.valueOf(
                        admission.getDischargeDate()));
            }

            ps.setString(4, admission.getStatus().name());
            ps.setBigDecimal(5, admission.getDailyCost());
            ps.setString(6, admission.getRoomNumber());
            ps.setString(7, admission.getNotes());
            ps.setLong(8, admission.getAdmissionId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete Admission
    public boolean deleteAdmission(Long admissionId)
            throws SQLException {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid admission ID.");
        }

        String sql = """
                DELETE FROM admissions
                WHERE admission_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, admissionId);

            return ps.executeUpdate() > 0;
        }
    }

    // ResultSet Mapping
    private Admission mapAdmission(ResultSet rs) throws SQLException {

        Timestamp dischargeTimestamp = rs.getTimestamp("discharge_date");

        return new Admission(
                rs.getLong("admission_id"),
                rs.getLong("patient_id"),
                rs.getTimestamp("admission_date").toLocalDateTime(),
                dischargeTimestamp == null
                        ? null
                        : dischargeTimestamp.toLocalDateTime(),
                AdmissionStatus.valueOf(rs.getString("status")),
                rs.getBigDecimal("daily_cost"),
                rs.getString("room_number"),
                rs.getString("notes")
        );
    }
}
