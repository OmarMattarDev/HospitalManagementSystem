
package com.hospital.model;

import com.hospital.model.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Doctor extends Person {

    // Fields
    private Long departmentId;
    private String specialization;
    private String licenseNumber;
    private BigDecimal consultationFee;
    private LocalDate hireDate;
    private boolean active;

    // Constructor for new doctors
    public Doctor(String firstName, String lastName,
                  Gender gender, LocalDate dateOfBirth,
                  String phone, String email, String address,
                  Long departmentId, String specialization,
                  String licenseNumber, BigDecimal consultationFee,
                  LocalDate hireDate) {

        super(firstName, lastName, gender, dateOfBirth,
                phone, email, address);

        setDepartmentId(departmentId);
        setSpecialization(specialization);
        setLicenseNumber(licenseNumber);
        setConsultationFee(consultationFee);
        setHireDate(hireDate);

        this.active = true;
    }

    // Constructor for database mapping
    public Doctor(Long doctorId, String firstName, String lastName,
                  Gender gender, LocalDate dateOfBirth,
                  String phone, String email, String address,
                  LocalDateTime createdAt,
                  Long departmentId, String specialization,
                  String licenseNumber, BigDecimal consultationFee,
                  LocalDate hireDate, boolean active) {

        super(doctorId, firstName, lastName, gender, dateOfBirth,
                phone, email, address, createdAt);

        setDepartmentId(departmentId);
        setSpecialization(specialization);
        setLicenseNumber(licenseNumber);
        setConsultationFee(consultationFee);
        setHireDate(hireDate);

        this.active = active;
    }

    // Getters
    public Long getDepartmentId() {
        return departmentId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setDepartmentId(Long departmentId) {

        if (departmentId != null && departmentId <= 0) {
            throw new IllegalArgumentException("Invalid department ID.");
        }

        this.departmentId = departmentId;
    }

    public void setSpecialization(String specialization) {

        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException(
                    "Specialization cannot be empty.");
        }

        if (specialization.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Specialization cannot exceed 100 characters.");
        }

        this.specialization = specialization.trim();
    }

    public void setLicenseNumber(String licenseNumber) {

        if (licenseNumber == null || licenseNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "License number cannot be empty.");
        }

        if (licenseNumber.trim().length() > 50) {
            throw new IllegalArgumentException(
                    "License number cannot exceed 50 characters.");
        }

        this.licenseNumber = licenseNumber.trim();
    }

    public void setConsultationFee(BigDecimal consultationFee) {

        if (consultationFee == null
                || consultationFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Consultation fee cannot be negative or null.");
        }

        this.consultationFee = consultationFee;
    }

    public void setHireDate(LocalDate hireDate) {

        if (hireDate == null || hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid hire date.");
        }

        this.hireDate = hireDate;
    }

    // Business Methods
    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    // Object Methods
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + getPersonId() +
                ", fullName='" + getFullName() + '\'' +
                ", departmentId=" + departmentId +
                ", specialization='" + specialization + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", consultationFee=" + consultationFee +
                ", hireDate=" + hireDate +
                ", active=" + active +
                '}';
    }
}
