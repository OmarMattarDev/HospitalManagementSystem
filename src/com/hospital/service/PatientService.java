
package com.hospital.service;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import com.hospital.model.enums.BloodType;
import com.hospital.model.enums.Gender;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class PatientService {

    private final Connection connection;
    private final PatientDAO patientDAO;

    public PatientService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null.");
        }

        this.connection = connection;
        this.patientDAO = new PatientDAO(connection);
    }

    public Long addPatient(Patient patient) throws SQLException {
        validatePatient(patient);

        return executeTransaction(() -> patientDAO.addPatient(patient));
    }

    public Patient getPatientById(Long patientId) throws SQLException {
        validateId(patientId, "Patient ID");
        return patientDAO.getPatientById(patientId);
    }

    public List<Patient> getAllPatients() throws SQLException {
        return patientDAO.getAllPatients();
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        validatePatient(patient);
        validateId(patient.getPersonId(), "Patient ID");

        return executeTransaction(() -> patientDAO.updatePatient(patient));
    }

    public boolean deletePatient(Long patientId) throws SQLException {
        validateId(patientId, "Patient ID");

        return executeTransaction(() -> patientDAO.deletePatient(patientId));
    }

    public List<Patient> getPatientsByBloodType(BloodType bloodType)
            throws SQLException {

        if (bloodType == null) {
            throw new IllegalArgumentException("Blood type cannot be null.");
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getBloodType() == bloodType)
                .toList();
    }

    public List<Patient> getPatientsByGender(Gender gender)
            throws SQLException {

        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }

        return getAllPatients().stream()
                .filter(patient -> patient.getGender() == gender)
                .toList();
    }

    public List<Patient> getPatientsSortedByName() throws SQLException {
        return getAllPatients().stream()
                .sorted(
                        Comparator.comparing(Patient::getLastName)
                                .thenComparing(Patient::getFirstName)
                )
                .toList();
    }

    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
    }

    private void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero."
            );
        }
    }

    private <T> T executeTransaction(SqlOperation<T> operation)
            throws SQLException {

        boolean originalAutoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            T result = operation.execute();

            connection.commit();
            return result;

        } catch (SQLException | RuntimeException exception) {
            connection.rollback();
            throw exception;

        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    @FunctionalInterface
    private interface SqlOperation<T> {
        T execute() throws SQLException;
    }
}
