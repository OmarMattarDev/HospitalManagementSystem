
package com.hospital.workflow;

import com.hospital.dao.AdmissionDAO;
import com.hospital.dao.DiagnosisDAO;
import com.hospital.model.Admission;
import com.hospital.model.Diagnosis;

import java.sql.Connection;
import java.sql.SQLException;

public class DiagnosisWorkflow {

    private final DiagnosisDAO diagnosisDAO;
    private final AdmissionDAO admissionDAO;

    public DiagnosisWorkflow(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.diagnosisDAO = new DiagnosisDAO(connection);
        this.admissionDAO = new AdmissionDAO(connection);
    }

    public Long addDiagnosis(Diagnosis diagnosis)
            throws SQLException {

        if (diagnosis == null) {
            throw new IllegalArgumentException(
                    "Diagnosis cannot be null."
            );
        }

        Admission admission =
                admissionDAO.getAdmissionById(diagnosis.getAdmissionId());

        if (admission == null) {
            throw new IllegalStateException(
                    "Admission does not exist."
            );
        }

        return diagnosisDAO.addDiagnosis(diagnosis);
    }
}
