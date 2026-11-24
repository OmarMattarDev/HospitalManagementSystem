
package com.hospital.dao;

import com.hospital.model.DoctorAssignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorAssignmentDAO {

    // Fields
    private final Connection connection;

    // Constructor
    public DoctorAssignmentDAO(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Database connection cannot be null.");
        }

        this.connection = connection;
    }

    // Add Doctor Assignment
    public Long addAssignment(DoctorAssignment assignment)
            throws SQLException {

        if (assignment == null) {
            throw new IllegalArgumentException(
                    "Assignment cannot be null.");
        }

        String sql = """
                INSERT INTO doctor_assignments
                (admission_id, doctor_id, assigned_at, released_at, notes)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, assignment.getAdmissionId());
            ps.setLong(2, assignment.getDoctorId());

            ps.setTimestamp(3, Timestamp.valueOf(
                    assignment.getAssignedAt()));

            if (assignment.getReleasedAt() == null) {
                ps.setNull(4, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(4, Timestamp.valueOf(
                        assignment.getReleasedAt()));
            }

            ps.setString(5, assignment.getNotes());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException(
                "Failed to retrieve generated assignment ID.");
    }

    // Get Assignment By ID
    public DoctorAssignment getAssignmentById(Long assignmentId)
            throws SQLException {

        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID.");
        }

        String sql = """
                SELECT *
                FROM doctor_assignments
                WHERE assignment_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, assignmentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapAssignment(rs);
                }
            }
        }

        return null;
    }

    // Get All Assignments
    public List<DoctorAssignment> getAllAssignments()
            throws SQLException {

        List<DoctorAssignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM doctor_assignments
                ORDER BY assigned_at DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                assignments.add(mapAssignment(rs));
            }
        }

        return assignments;
    }

    // Get Assignments By Admission
    public List<DoctorAssignment> getAssignmentsByAdmission(
            Long admissionId) throws SQLException {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid admission ID.");
        }

        List<DoctorAssignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM doctor_assignments
                WHERE admission_id = ?
                ORDER BY assigned_at DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, admissionId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    assignments.add(mapAssignment(rs));
                }
            }
        }

        return assignments;
    }

    // Get Assignments By Doctor
    public List<DoctorAssignment> getAssignmentsByDoctor(
            Long doctorId) throws SQLException {

        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid doctor ID.");
        }

        List<DoctorAssignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM doctor_assignments
                WHERE doctor_id = ?
                ORDER BY assigned_at DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    assignments.add(mapAssignment(rs));
                }
            }
        }

        return assignments;
    }

    // Get Active Assignments
    public List<DoctorAssignment> getActiveAssignments()
            throws SQLException {

        List<DoctorAssignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM doctor_assignments
                WHERE released_at IS NULL
                ORDER BY assigned_at DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                assignments.add(mapAssignment(rs));
            }
        }

        return assignments;
    }

    // Update Assignment
    public boolean updateAssignment(DoctorAssignment assignment)
            throws SQLException {

        if (assignment == null || assignment.getAssignmentId() == null) {
            throw new IllegalArgumentException(
                    "Invalid assignment.");
        }

        String sql = """
                UPDATE doctor_assignments
                SET admission_id = ?,
                    doctor_id = ?,
                    assigned_at = ?,
                    released_at = ?,
                    notes = ?
                WHERE assignment_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, assignment.getAdmissionId());
            ps.setLong(2, assignment.getDoctorId());

            ps.setTimestamp(3, Timestamp.valueOf(
                    assignment.getAssignedAt()));

            if (assignment.getReleasedAt() == null) {
                ps.setNull(4, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(4, Timestamp.valueOf(
                        assignment.getReleasedAt()));
            }

            ps.setString(5, assignment.getNotes());
            ps.setLong(6, assignment.getAssignmentId());

            return ps.executeUpdate() > 0;
        }
    }

    // Release Assignment
    public boolean releaseAssignment(Long assignmentId)
            throws SQLException {

        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID.");
        }

        String sql = """
                UPDATE doctor_assignments
                SET released_at = CURRENT_TIMESTAMP
                WHERE assignment_id = ?
                  AND released_at IS NULL
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, assignmentId);

            return ps.executeUpdate() > 0;
        }
    }

    // Delete Assignment
    public boolean deleteAssignment(Long assignmentId)
            throws SQLException {

        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID.");
        }

        String sql = """
                DELETE FROM doctor_assignments
                WHERE assignment_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, assignmentId);

            return ps.executeUpdate() > 0;
        }
    }

    // ResultSet Mapping
    private DoctorAssignment mapAssignment(ResultSet rs)
            throws SQLException {

        Timestamp releasedTimestamp = rs.getTimestamp("released_at");

        return new DoctorAssignment(
                rs.getLong("assignment_id"),
                rs.getLong("admission_id"),
                rs.getLong("doctor_id"),
                rs.getTimestamp("assigned_at").toLocalDateTime(),
                releasedTimestamp == null
                        ? null
                        : releasedTimestamp.toLocalDateTime(),
                rs.getString("notes")
        );
    }
}
