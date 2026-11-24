
package com.hospital.service;

import com.hospital.dao.DoctorAssignmentDAO;
import com.hospital.model.DoctorAssignment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class DoctorAssignmentService {

    private final DoctorAssignmentDAO doctorAssignmentDAO;

    public DoctorAssignmentService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null.");
        }

        this.doctorAssignmentDAO = new DoctorAssignmentDAO(connection);
    }

    public Long addAssignment(DoctorAssignment assignment)
            throws SQLException {

        validateAssignment(assignment);
        return doctorAssignmentDAO.addAssignment(assignment);
    }

    public DoctorAssignment getAssignmentById(Long assignmentId)
            throws SQLException {

        validateId(assignmentId, "Assignment ID");
        return doctorAssignmentDAO.getAssignmentById(assignmentId);
    }

    public List<DoctorAssignment> getAllAssignments()
            throws SQLException {

        return doctorAssignmentDAO.getAllAssignments();
    }

    public List<DoctorAssignment> getAssignmentsByAdmission(Long admissionId)
            throws SQLException {

        validateId(admissionId, "Admission ID");
        return doctorAssignmentDAO.getAssignmentsByAdmission(admissionId);
    }

    public List<DoctorAssignment> getAssignmentsByDoctor(Long doctorId)
            throws SQLException {

        validateId(doctorId, "Doctor ID");
        return doctorAssignmentDAO.getAssignmentsByDoctor(doctorId);
    }

    public List<DoctorAssignment> getActiveAssignments()
            throws SQLException {

        return doctorAssignmentDAO.getActiveAssignments();
    }

    public boolean updateAssignment(DoctorAssignment assignment)
            throws SQLException {

        validateAssignment(assignment);
        validateId(assignment.getAssignmentId(), "Assignment ID");

        return doctorAssignmentDAO.updateAssignment(assignment);
    }

    public boolean releaseAssignment(Long assignmentId)
            throws SQLException {

        validateId(assignmentId, "Assignment ID");
        return doctorAssignmentDAO.releaseAssignment(assignmentId);
    }

    public boolean deleteAssignment(Long assignmentId)
            throws SQLException {

        validateId(assignmentId, "Assignment ID");
        return doctorAssignmentDAO.deleteAssignment(assignmentId);
    }

    public List<DoctorAssignment> getAssignmentsSortedByDate()
            throws SQLException {

        return getAllAssignments().stream()
                .sorted(
                        Comparator.comparing(DoctorAssignment::getAssignedAt)
                                .reversed()
                )
                .toList();
    }

    private void validateAssignment(DoctorAssignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException(
                    "Doctor assignment cannot be null."
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
