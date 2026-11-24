
package com.hospital.report;

import com.hospital.dao.AdmissionDAO;
import com.hospital.model.Admission;
import com.hospital.model.enums.AdmissionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class AdmissionReport {

    private final AdmissionDAO admissionDAO;

    public AdmissionReport(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.admissionDAO = new AdmissionDAO(connection);
    }

    public long getTotalAdmissions() throws SQLException {
        return admissionDAO.getAllAdmissions().size();
    }

    public Map<AdmissionStatus, Long> getAdmissionsCountByStatus()
            throws SQLException {

        return admissionDAO.getAllAdmissions().stream()
                .filter(admission -> admission.getStatus() != null)
                .collect(
                        Collectors.groupingBy(
                                Admission::getStatus,
                                Collectors.counting()
                        )
                );
    }

    public long getActiveAdmissionsCount() throws SQLException {
        return admissionDAO.getActiveAdmissions().size();
    }

    public double getAverageStayDays() throws SQLException {

        var admissions = admissionDAO.getAllAdmissions();

        if (admissions.isEmpty()) {
            return 0.0;
        }

        return admissions.stream()
                .mapToLong(Admission::getStayDays)
                .average()
                .orElse(0.0);
    }

    public BigDecimal getTotalEstimatedAdmissionCost()
            throws SQLException {

        return admissionDAO.getAllAdmissions().stream()
                .filter(admission -> admission.getDailyCost() != null)
                .map(admission ->
                        admission.getDailyCost().multiply(
                                BigDecimal.valueOf(admission.getStayDays())
                        )
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
