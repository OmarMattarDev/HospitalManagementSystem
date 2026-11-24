
package com.hospital.service;

import com.hospital.dao.AdmissionDAO;
import com.hospital.model.Admission;
import com.hospital.model.enums.AdmissionStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class AdmissionService {

    private final AdmissionDAO admissionDAO;

    public AdmissionService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null.");
        }

        this.admissionDAO = new AdmissionDAO(connection);
    }

    public Long addAdmission(Admission admission) throws SQLException {
        validateAdmission(admission);
        return admissionDAO.addAdmission(admission);
    }

    public Admission getAdmissionById(Long admissionId)
            throws SQLException {

        validateId(admissionId, "Admission ID");
        return admissionDAO.getAdmissionById(admissionId);
    }

    public List<Admission> getAllAdmissions() throws SQLException {
        return admissionDAO.getAllAdmissions();
    }

    public List<Admission> getAdmissionsByPatient(Long patientId)
            throws SQLException {

        validateId(patientId, "Patient ID");
        return admissionDAO.getAdmissionsByPatient(patientId);
    }

    public List<Admission> getAdmissionsByStatus(AdmissionStatus status)
            throws SQLException {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Admission status cannot be null."
            );
        }

        return admissionDAO.getAdmissionsByStatus(status);
    }

    public List<Admission> getActiveAdmissions() throws SQLException {
        return admissionDAO.getActiveAdmissions();
    }

    public boolean updateAdmission(Admission admission)
            throws SQLException {

        validateAdmission(admission);
        validateId(admission.getAdmissionId(), "Admission ID");

        return admissionDAO.updateAdmission(admission);
    }

    public boolean deleteAdmission(Long admissionId) throws SQLException {
        validateId(admissionId, "Admission ID");
        return admissionDAO.deleteAdmission(admissionId);
    }

    public List<Admission> getAdmissionsSortedByDate()
            throws SQLException {

        return getAllAdmissions().stream()
                .sorted(
                        Comparator.comparing(Admission::getAdmissionDate)
                                .reversed()
                )
                .toList();
    }

    private void validateAdmission(Admission admission) {
        if (admission == null) {
            throw new IllegalArgumentException(
                    "Admission cannot be null."
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
