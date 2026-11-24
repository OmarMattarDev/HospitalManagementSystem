
package com.hospital.workflow;

import com.hospital.dao.AdmissionDAO;
import com.hospital.dao.DoctorAssignmentDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Admission;
import com.hospital.model.DoctorAssignment;
import com.hospital.model.Patient;
import com.hospital.model.enums.AdmissionStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AdmissionWorkflow {

    private final Connection connection;
    private final AdmissionDAO admissionDAO;
    private final PatientDAO patientDAO;
    private final DoctorAssignmentDAO doctorAssignmentDAO;

    public AdmissionWorkflow(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.connection = connection;
        this.admissionDAO = new AdmissionDAO(connection);
        this.patientDAO = new PatientDAO(connection);
        this.doctorAssignmentDAO =
                new DoctorAssignmentDAO(connection);
    }

    public Long admitPatient(Admission admission)
            throws SQLException {

        if (admission == null) {
            throw new IllegalArgumentException(
                    "Admission cannot be null."
            );
        }

        Patient patient =
                patientDAO.getPatientById(admission.getPatientId());

        if (patient == null) {
            throw new IllegalStateException(
                    "Patient does not exist."
            );
        }

        return admissionDAO.addAdmission(admission);
    }

    public boolean dischargePatient(Long admissionId)
            throws SQLException {

        validateId(admissionId, "Admission ID");

        return WorkflowTransaction.execute(
                connection,
                () -> {

                    Admission admission =
                            admissionDAO.getAdmissionById(admissionId);

                    if (admission == null) {
                        throw new IllegalStateException(
                                "Admission does not exist."
                        );
                    }

                    AdmissionStatus currentStatus =
                            admission.getStatus();

                    if (currentStatus == AdmissionStatus.DISCHARGED) {
                        throw new IllegalStateException(
                                "Patient is already discharged."
                        );
                    }

                    admission.setStatus(AdmissionStatus.DISCHARGED);
                    admission.setDischargeDate(LocalDateTime.now());

                    boolean admissionUpdated =
                            admissionDAO.updateAdmission(admission);

                    if (!admissionUpdated) {
                        throw new SQLException(
                                "Failed to update admission."
                        );
                    }

                    List<DoctorAssignment> assignments =
                            doctorAssignmentDAO
                                    .getAssignmentsByAdmission(admissionId);

                    for (DoctorAssignment assignment : assignments) {

                        if (assignment.getReleasedAt() == null) {

                            doctorAssignmentDAO.releaseAssignment(
                                    assignment.getAssignmentId()
                            );
                        }
                    }

                    return true;
                }
        );
    }

    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero."
            );
        }
    }
}
