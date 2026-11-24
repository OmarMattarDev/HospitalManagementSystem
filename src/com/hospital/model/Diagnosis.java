
package com.hospital.model;

import java.time.LocalDateTime;

public class Diagnosis {

    // Fields
    private Long diagnosisId;
    private Long admissionId;
    private String diagnosisName;
    private String description;
    private LocalDateTime diagnosedAt;

    // Constructor for new diagnoses
    public Diagnosis(Long admissionId, String diagnosisName,
                     String description) {

        setAdmissionId(admissionId);
        setDiagnosisName(diagnosisName);
        setDescription(description);

        this.diagnosedAt = LocalDateTime.now();
    }

    // Constructor for database mapping
    public Diagnosis(Long diagnosisId, Long admissionId,
                     String diagnosisName, String description,
                     LocalDateTime diagnosedAt) {

        if (diagnosisId == null || diagnosisId <= 0) {
            throw new IllegalArgumentException("Invalid diagnosis ID.");
        }

        this.diagnosisId = diagnosisId;

        setAdmissionId(admissionId);
        setDiagnosisName(diagnosisName);
        setDescription(description);

        validateDiagnosedAt(diagnosedAt);
        this.diagnosedAt = diagnosedAt;
    }

    // Getters
    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDiagnosedAt() {
        return diagnosedAt;
    }

    // Setters
    public void setAdmissionId(Long admissionId) {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException("Invalid admission ID.");
        }

        this.admissionId = admissionId;
    }

    public void setDiagnosisName(String diagnosisName) {

        if (diagnosisName == null || diagnosisName.isBlank()) {
            throw new IllegalArgumentException(
                    "Diagnosis name cannot be empty.");
        }

        if (diagnosisName.trim().length() > 150) {
            throw new IllegalArgumentException(
                    "Diagnosis name cannot exceed 150 characters.");
        }

        this.diagnosisName = diagnosisName.trim();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Validation Methods
    private void validateDiagnosedAt(LocalDateTime date) {

        if (date == null || date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Invalid diagnosis date.");
        }
    }

    // Object Methods
    @Override
    public String toString() {
        return "Diagnosis{" +
                "diagnosisId=" + diagnosisId +
                ", admissionId=" + admissionId +
                ", diagnosisName='" + diagnosisName + '\'' +
                ", description='" + description + '\'' +
                ", diagnosedAt=" + diagnosedAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Diagnosis)) {
            return false;
        }

        Diagnosis diagnosis = (Diagnosis) obj;

        return diagnosisId != null
                && diagnosisId.equals(diagnosis.diagnosisId);
    }

    @Override
    public int hashCode() {
        return diagnosisId == null
                ? System.identityHashCode(this)
                : diagnosisId.hashCode();
    }
}
