
package com.hospital.workflow;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import java.sql.Connection;
import java.sql.SQLException;

public class PatientWorkflow {

    private final Connection connection;
    private final PatientDAO patientDAO;

    public PatientWorkflow(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.connection = connection;
        this.patientDAO = new PatientDAO(connection);
    }

    public Long registerPatient(Patient patient) throws SQLException {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Patient cannot be null."
            );
        }

        return WorkflowTransaction.execute(
                connection,
                () -> patientDAO.addPatient(patient)
        );
    }
}
