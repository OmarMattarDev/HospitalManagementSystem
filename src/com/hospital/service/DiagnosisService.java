
package com.hospital.service;

import com.hospital.dao.DiagnosisDAO;
import com.hospital.model.Diagnosis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class DiagnosisService {

    private final DiagnosisDAO diagnosisDAO;

    public DiagnosisService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null.");
        }

        this.diagnosisDAO = new DiagnosisDAO(connection);
    }

    public Long addDiagnosis(Diagnosis diagnosis) throws SQLException {
        validateDiagnosis(diagnosis);
        return diagnosisDAO.addDiagnosis(diagnosis);
    }

    public Diagnosis getDiagnosisById(Long diagnosisId)
            throws SQLException {

        validateId(diagnosisId, "Diagnosis ID");
        return diagnosisDAO.getDiagnosisById(diagnosisId);
    }

    public List<Diagnosis> getAllDiagnoses() throws SQLException {
        return diagnosisDAO.getAllDiagnoses();
    }

    public List<Diagnosis> getDiagnosesByAdmission(Long admissionId)
            throws SQLException {

        validateId(admissionId, "Admission ID");
        return diagnosisDAO.getDiagnosesByAdmission(admissionId);
    }

    public boolean updateDiagnosis(Diagnosis diagnosis)
            throws SQLException {

        validateDiagnosis(diagnosis);
        validateId(diagnosis.getDiagnosisId(), "Diagnosis ID");

        return diagnosisDAO.updateDiagnosis(diagnosis);
    }

    public boolean deleteDiagnosis(Long diagnosisId) throws SQLException {
        validateId(diagnosisId, "Diagnosis ID");
        return diagnosisDAO.deleteDiagnosis(diagnosisId);
    }

    public List<Diagnosis> getDiagnosesSortedByDate()
            throws SQLException {

        return getAllDiagnoses().stream()
                .sorted(
                        Comparator.comparing(Diagnosis::getDiagnosedAt)
                                .reversed()
                )
                .toList();
    }

    private void validateDiagnosis(Diagnosis diagnosis) {
        if (diagnosis == null) {
            throw new IllegalArgumentException(
                    "Diagnosis cannot be null."
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
