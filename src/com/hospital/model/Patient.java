
package com.hospital.model;

import com.hospital.model.enums.BloodType;
import com.hospital.model.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient extends Person {

    // Fields
    private BloodType bloodType;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Constructor for new patients
    public Patient(String firstName, String lastName,
                   Gender gender, LocalDate dateOfBirth,
                   String phone, String email, String address,
                   BloodType bloodType,
                   String emergencyContactName,
                   String emergencyContactPhone) {

        super(firstName, lastName, gender, dateOfBirth,
                phone, email, address);

        setBloodType(bloodType);
        setEmergencyContactName(emergencyContactName);
        setEmergencyContactPhone(emergencyContactPhone);
    }

    // Constructor for database mapping
    public Patient(Long patientId, String firstName, String lastName,
                   Gender gender, LocalDate dateOfBirth,
                   String phone, String email, String address,
                   LocalDateTime createdAt,
                   BloodType bloodType,
                   String emergencyContactName,
                   String emergencyContactPhone) {

        super(patientId, firstName, lastName, gender, dateOfBirth,
                phone, email, address, createdAt);

        setBloodType(bloodType);
        setEmergencyContactName(emergencyContactName);
        setEmergencyContactPhone(emergencyContactPhone);
    }

    // Getters
    public BloodType getBloodType() {
        return bloodType;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    // Setters
    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public void setEmergencyContactName(String name) {

        if (name != null && name.isBlank()) {
            throw new IllegalArgumentException(
                    "Emergency contact name cannot be blank.");
        }

        if (name != null && name.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Emergency contact name cannot exceed 100 characters.");
        }

        this.emergencyContactName = name == null ? null : name.trim();
    }

    public void setEmergencyContactPhone(String phone) {

        if (phone != null) {
            String trimmedPhone = phone.trim();

            if (trimmedPhone.length() < 7 || trimmedPhone.length() > 20) {
                throw new IllegalArgumentException(
                        "Emergency phone must be between 7 and 20 characters.");
            }

            int startIndex = trimmedPhone.startsWith("+") ? 1 : 0;

            if (startIndex == trimmedPhone.length()) {
                throw new IllegalArgumentException("Invalid emergency phone.");
            }

            for (int i = startIndex; i < trimmedPhone.length(); i++) {
                if (!Character.isDigit(trimmedPhone.charAt(i))) {
                    throw new IllegalArgumentException(
                            "Emergency phone must contain only digits and an optional +.");
                }
            }
        }

        this.emergencyContactPhone = phone == null ? null : phone.trim();
    }

    // Business Methods
    public boolean hasEmergencyContact() {
        return emergencyContactName != null
                && emergencyContactPhone != null;
    }

    // Object Methods
    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + getPersonId() +
                ", fullName='" + getFullName() + '\'' +
                ", bloodType=" + bloodType +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", emergencyContactPhone='" + emergencyContactPhone + '\'' +
                '}';
    }
}
