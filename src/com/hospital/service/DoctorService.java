
package com.hospital.service;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class DoctorService {

    private final Connection connection;
    private final DoctorDAO doctorDAO;

    public DoctorService(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null.");
        }

        this.connection = connection;
        this.doctorDAO = new DoctorDAO(connection);
    }

    public Long addDoctor(Doctor doctor) throws SQLException {
        validateDoctor(doctor);

        return executeTransaction(() -> doctorDAO.addDoctor(doctor));
    }

    public Doctor getDoctorById(Long doctorId) throws SQLException {
        validateId(doctorId, "Doctor ID");
        return doctorDAO.getDoctorById(doctorId);
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDAO.getAllDoctors();
    }

    public List<Doctor> getDoctorsByDepartment(Long departmentId)
            throws SQLException {

        validateId(departmentId, "Department ID");
        return doctorDAO.getDoctorsByDepartment(departmentId);
    }

    public boolean updateDoctor(Doctor doctor) throws SQLException {
        validateDoctor(doctor);
        validateId(doctor.getPersonId(), "Doctor ID");

        return executeTransaction(() -> doctorDAO.updateDoctor(doctor));
    }

    public boolean deleteDoctor(Long doctorId) throws SQLException {
        validateId(doctorId, "Doctor ID");

        return executeTransaction(() -> doctorDAO.deleteDoctor(doctorId));
    }

    public List<Doctor> getActiveDoctors() throws SQLException {
        return getAllDoctors().stream()
                .filter(Doctor::isActive)
                .toList();
    }

    public List<Doctor> getDoctorsSortedByConsultationFee()
            throws SQLException {

        return getAllDoctors().stream()
                .sorted(Comparator.comparing(Doctor::getConsultationFee))
                .toList();
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization)
            throws SQLException {

        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException(
                    "Specialization cannot be empty."
            );
        }

        return getAllDoctors().stream()
                .filter(doctor ->
                        doctor.getSpecialization() != null
                                && doctor.getSpecialization()
                                .equalsIgnoreCase(specialization.trim())
                )
                .toList();
    }

    private void validateDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
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
