
package com.hospital.workflow;

import com.hospital.dao.AdmissionDAO;
import com.hospital.dao.DoctorAssignmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.model.Admission;
import com.hospital.model.Doctor;
import com.hospital.model.DoctorAssignment;
import com.hospital.model.enums.AdmissionStatus;

import java.sql.Connection;
import java.sql.SQLException;

public class DoctorAssignmentWorkflow {

    private final DoctorAssignmentDAO doctorAssignmentDAO;
    private final DoctorDAO doctorDAO;
    private final AdmissionDAO admissionDAO;

    public DoctorAssignmentWorkflow(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.doctorAssignmentDAO =
                new DoctorAssignmentDAO(connection);

        this.doctorDAO = new DoctorDAO(connection);
        this.admissionDAO = new AdmissionDAO(connection);
    }

    public Long assignDoctor(DoctorAssignment assignment)
            throws SQLException {

        if (assignment == null) {
            throw new IllegalArgumentException(
                    "Doctor assignment cannot be null."
            );
        }

        Doctor doctor =
                doctorDAO.getDoctorById(assignment.getDoctorId());

        if (doctor == null) {
            throw new IllegalStateException(
                    "Doctor does not exist."
            );
        }

        if (!doctor.isActive()) {
            throw new IllegalStateException(
                    "Cannot assign an inactive doctor."
            );
        }

        Admission admission =
                admissionDAO.getAdmissionById(assignment.getAdmissionId());

        if (admission == null) {
            throw new IllegalStateException(
                    "Admission does not exist."
            );
        }

        AdmissionStatus status = admission.getStatus();

        if (status != AdmissionStatus.ADMITTED
                && status != AdmissionStatus.ICU) {

            throw new IllegalStateException(
                    "Cannot assign a doctor to an inactive admission."
            );
        }

        return doctorAssignmentDAO.addAssignment(assignment);
    }

    public boolean releaseDoctor(Long assignmentId)
            throws SQLException {

        validateId(assignmentId, "Assignment ID");

        DoctorAssignment assignment =
                doctorAssignmentDAO.getAssignmentById(assignmentId);

        if (assignment == null) {
            throw new IllegalStateException(
                    "Doctor assignment does not exist."
            );
        }

        if (assignment.getReleasedAt() != null) {
            throw new IllegalStateException(
                    "This assignment has already been released."
            );
        }

        return doctorAssignmentDAO.releaseAssignment(assignmentId);
    }

    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero."
            );
        }
    }
}
