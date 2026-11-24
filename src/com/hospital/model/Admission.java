
package com.hospital.model;

import com.hospital.model.enums.AdmissionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Admission {

    // Fields
    private Long admissionId;
    private Long patientId;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private AdmissionStatus status;
    private BigDecimal dailyCost;
    private String roomNumber;
    private String notes;

    // Constructor for new admissions
    public Admission(Long patientId, BigDecimal dailyCost,
                     String roomNumber, String notes) {

        setPatientId(patientId);

        this.admissionDate = LocalDateTime.now();
        this.dischargeDate = null;

        setStatus(AdmissionStatus.ADMITTED);
        setDailyCost(dailyCost);
        setRoomNumber(roomNumber);
        setNotes(notes);
    }

    // Constructor for database mapping
    public Admission(Long admissionId, Long patientId,
                     LocalDateTime admissionDate,
                     LocalDateTime dischargeDate,
                     AdmissionStatus status,
                     BigDecimal dailyCost,
                     String roomNumber, String notes) {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException("Invalid admission ID.");
        }

        this.admissionId = admissionId;

        setPatientId(patientId);
        validateAdmissionDate(admissionDate);
        this.admissionDate = admissionDate;

        setDischargeDate(dischargeDate);
        setStatus(status);
        setDailyCost(dailyCost);
        setRoomNumber(roomNumber);
        setNotes(notes);

        validateAdmissionStatus();
    }

    // Getters
    public Long getAdmissionId() {
        return admissionId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public AdmissionStatus getStatus() {
        return status;
    }

    public BigDecimal getDailyCost() {
        return dailyCost;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setPatientId(Long patientId) {

        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("Invalid patient ID.");
        }

        this.patientId = patientId;
    }

    public void setDischargeDate(LocalDateTime dischargeDate) {

        if (dischargeDate != null
                && (admissionDate != null
                && dischargeDate.isBefore(admissionDate))) {

            throw new IllegalArgumentException(
                    "Discharge date cannot be before admission date.");
        }

        this.dischargeDate = dischargeDate;
    }

    public void setStatus(AdmissionStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Admission status cannot be null.");
        }

        this.status = status;
    }

    public void setDailyCost(BigDecimal dailyCost) {

        if (dailyCost == null
                || dailyCost.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Daily cost cannot be negative or null.");
        }

        this.dailyCost = dailyCost;
    }

    public void setRoomNumber(String roomNumber) {

        if (roomNumber != null && roomNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Room number cannot be blank.");
        }

        if (roomNumber != null && roomNumber.trim().length() > 20) {
            throw new IllegalArgumentException(
                    "Room number cannot exceed 20 characters.");
        }

        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Validation Methods
    private void validateAdmissionDate(LocalDateTime date) {

        if (date == null || date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid admission date.");
        }
    }

    private void validateAdmissionStatus() {

        if (status == AdmissionStatus.DISCHARGED
                && dischargeDate == null) {

            throw new IllegalArgumentException(
                    "Discharged admission must have a discharge date.");
        }

        if (status != AdmissionStatus.DISCHARGED
                && dischargeDate != null) {

            throw new IllegalArgumentException(
                    "Active admission cannot have a discharge date.");
        }
    }

    // Business Methods
    public boolean isActive() {
        return status == AdmissionStatus.ADMITTED
                || status == AdmissionStatus.ICU;
    }

    public void transferToICU() {

        if (status != AdmissionStatus.ADMITTED) {
            throw new IllegalStateException(
                    "Only admitted patients can be transferred to ICU.");
        }

        status = AdmissionStatus.ICU;
    }

    public void discharge() {

        if (!isActive()) {
            throw new IllegalStateException(
                    "Patient is not currently admitted.");
        }

        dischargeDate = LocalDateTime.now();
        status = AdmissionStatus.DISCHARGED;
    }

    public long getStayDays() {

        LocalDateTime endDate = dischargeDate == null
                ? LocalDateTime.now()
                : dischargeDate;

        long days = java.time.temporal.ChronoUnit.DAYS.between(
                admissionDate.toLocalDate(),
                endDate.toLocalDate()
        );

        return Math.max(days, 1);
    }

    public BigDecimal calculateTotalCost() {
        return dailyCost.multiply(BigDecimal.valueOf(getStayDays()));
    }

    // Object Methods
    @Override
    public String toString() {
        return "Admission{" +
                "admissionId=" + admissionId +
                ", patientId=" + patientId +
                ", admissionDate=" + admissionDate +
                ", dischargeDate=" + dischargeDate +
                ", status=" + status +
                ", dailyCost=" + dailyCost +
                ", roomNumber='" + roomNumber + '\'' +
                ", stayDays=" + getStayDays() +
                ", totalCost=" + calculateTotalCost() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Admission)) {
            return false;
        }

        Admission admission = (Admission) obj;

        return admissionId != null
                && admissionId.equals(admission.admissionId);
    }

    @Override
    public int hashCode() {
        return admissionId == null
                ? System.identityHashCode(this)
                : admissionId.hashCode();
    }
}
