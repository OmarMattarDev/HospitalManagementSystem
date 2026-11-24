
package com.hospital.dao;

import com.hospital.model.Diagnosis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisDAO {

    // Fields
    private final Connection connection;

    // Constructor
    public DiagnosisDAO(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        this.connection = connection;
    }

    // Add Diagnosis
    public Long addDiagnosis(Diagnosis diagnosis) throws SQLException {

        if (diagnosis == null) {
            throw new IllegalArgumentException(
                    "Diagnosis cannot be null.");
        }

        String sql = """
                INSERT INTO diagnoses
                (admission_id, diagnosis_name, description, diagnosed_at)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, diagnosis.getAdmissionId());
            ps.setString(2, diagnosis.getDiagnosisName());
            ps.setString(3, diagnosis.getDescription());

            ps.setTimestamp(4, Timestamp.valueOf(
                    diagnosis.getDiagnosedAt()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException(
                "Failed to retrieve generated diagnosis ID.");
    }

    // Get Diagnosis By ID
    public Diagnosis getDiagnosisById(Long diagnosisId)
            throws SQLException {

        if (diagnosisId == null || diagnosisId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid diagnosis ID.");
        }

        String sql = """
                SELECT *
                FROM diagnoses
                WHERE diagnosis_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, diagnosisId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapDiagnosis(rs);
                }
            }
        }

        return null;
    }

    // Get All Diagnoses
    public List<Diagnosis> getAllDiagnoses() throws SQLException {

        List<Diagnosis> diagnoses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM diagnoses
                ORDER BY diagnosed_at DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                diagnoses.add(mapDiagnosis(rs));
            }
        }

        return diagnoses;
    }

    // Get Diagnoses By Admission
    public List<Diagnosis> getDiagnosesByAdmission(Long admissionId)
            throws SQLException {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid admission ID.");
        }

        List<Diagnosis> diagnoses = new ArrayList<>();

        String sql = """
                SELECT *
                FROM diagnoses
                WHERE admission_id = ?
                ORDER BY diagnosed_at DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, admissionId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    diagnoses.add(mapDiagnosis(rs));
                }
            }
        }

        return diagnoses;
    }

    // Update Diagnosis
    public boolean updateDiagnosis(Diagnosis diagnosis)
            throws SQLException {

        if (diagnosis == null || diagnosis.getDiagnosisId() == null) {
            throw new IllegalArgumentException(
                    "Invalid diagnosis.");
        }

        String sql = """
                UPDATE diagnoses
                SET admission_id = ?,
                    diagnosis_name = ?,
                    description = ?,
                    diagnosed_at = ?
                WHERE diagnosis_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, diagnosis.getAdmissionId());
            ps.setString(2, diagnosis.getDiagnosisName());
            ps.setString(3, diagnosis.getDescription());

            ps.setTimestamp(4, Timestamp.valueOf(
                    diagnosis.getDiagnosedAt()));

            ps.setLong(5, diagnosis.getDiagnosisId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete Diagnosis
    public boolean deleteDiagnosis(Long diagnosisId)
            throws SQLException {

        if (diagnosisId == null || diagnosisId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid diagnosis ID.");
        }

        String sql = """
                DELETE FROM diagnoses
                WHERE diagnosis_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, diagnosisId);

            return ps.executeUpdate() > 0;
        }
    }

    // ResultSet Mapping
    private Diagnosis mapDiagnosis(ResultSet rs) throws SQLException {

        return new Diagnosis(
                rs.getLong("diagnosis_id"),
                rs.getLong("admission_id"),
                rs.getString("diagnosis_name"),
                rs.getString("description"),
                rs.getTimestamp("diagnosed_at").toLocalDateTime()
        );
    }
}
