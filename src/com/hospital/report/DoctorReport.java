
package com.hospital.report;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DoctorReport {

    private final DoctorDAO doctorDAO;

    public DoctorReport(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.doctorDAO = new DoctorDAO(connection);
    }

    public long getTotalDoctors() throws SQLException {
        return doctorDAO.getAllDoctors().size();
    }

    public long getActiveDoctorsCount() throws SQLException {
        return doctorDAO.getAllDoctors().stream()
                .filter(Doctor::isActive)
                .count();
    }

    public long getInactiveDoctorsCount() throws SQLException {
        return doctorDAO.getAllDoctors().stream()
                .filter(doctor -> !doctor.isActive())
                .count();
    }

    public Map<String, Long> getDoctorsCountBySpecialization()
            throws SQLException {

        return doctorDAO.getAllDoctors().stream()
                .filter(doctor ->
                        doctor.getSpecialization() != null
                                && !doctor.getSpecialization().isBlank()
                )
                .collect(
                        Collectors.groupingBy(
                                Doctor::getSpecialization,
                                Collectors.counting()
                        )
                );
    }

    public BigDecimal getAverageConsultationFee()
            throws SQLException {

        return doctorDAO.getAllDoctors().stream()
                .map(Doctor::getConsultationFee)
                .filter(Objects::nonNull)
                .collect(
                        Collectors.averagingDouble(
                                BigDecimal::doubleValue
                        )
                ) == 0.0
                ? BigDecimal.ZERO
                : calculateAverageFee();
    }

    private BigDecimal calculateAverageFee() throws SQLException {

        var fees = doctorDAO.getAllDoctors().stream()
                .map(Doctor::getConsultationFee)
                .filter(Objects::nonNull)
                .toList();

        if (fees.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = fees.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf(fees.size()),
                2,
                RoundingMode.HALF_UP
        );
    }
}
