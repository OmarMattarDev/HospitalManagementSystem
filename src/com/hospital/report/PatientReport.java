
package com.hospital.report;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import com.hospital.model.enums.BloodType;
import com.hospital.model.enums.Gender;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class PatientReport {

    private final PatientDAO patientDAO;

    public PatientReport(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.patientDAO = new PatientDAO(connection);
    }

    public long getTotalPatients() throws SQLException {
        return patientDAO.getAllPatients().size();
    }

    public Map<Gender, Long> getPatientsCountByGender()
            throws SQLException {

        return patientDAO.getAllPatients().stream()
                .filter(patient -> patient.getGender() != null)
                .collect(
                        Collectors.groupingBy(
                                Patient::getGender,
                                Collectors.counting()
                        )
                );
    }

    public Map<BloodType, Long> getPatientsCountByBloodType()
            throws SQLException {

        return patientDAO.getAllPatients().stream()
                .filter(patient -> patient.getBloodType() != null)
                .collect(
                        Collectors.groupingBy(
                                Patient::getBloodType,
                                Collectors.counting()
                        )
                );
    }
}
