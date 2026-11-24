
package com.hospital.model;

import java.time.LocalDateTime;

public class DoctorAssignment {

    // Fields
    private Long assignmentId;
    private Long admissionId;
    private Long doctorId;
    private LocalDateTime assignedAt;
    private LocalDateTime releasedAt;
    private String notes;

    // Constructor for new assignments
    public DoctorAssignment(Long admissionId, Long doctorId,
                            String notes) {

        setAdmissionId(admissionId);
        setDoctorId(doctorId);

        this.assignedAt = LocalDateTime.now();
        this.releasedAt = null;

        setNotes(notes);
    }

    // Constructor for database mapping
    public DoctorAssignment(Long assignmentId, Long admissionId,
                            Long doctorId, LocalDateTime assignedAt,
                            LocalDateTime releasedAt, String notes) {

        if (assignmentId == null || assignmentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID.");
        }

        this.assignmentId = assignmentId;

        setAdmissionId(admissionId);
        setDoctorId(doctorId);

        validateAssignedAt(assignedAt);
        this.assignedAt = assignedAt;

        setReleasedAt(releasedAt);
        setNotes(notes);
    }

    // Getters
    public Long getAssignmentId() {
        return assignmentId;
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public LocalDateTime getReleasedAt() {
        return releasedAt;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setAdmissionId(Long admissionId) {

        if (admissionId == null || admissionId <= 0) {
            throw new IllegalArgumentException("Invalid admission ID.");
        }

        this.admissionId = admissionId;
    }

    public void setDoctorId(Long doctorId) {

        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("Invalid doctor ID.");
        }

        this.doctorId = doctorId;
    }

    public void setReleasedAt(LocalDateTime releasedAt) {

        if (releasedAt != null
                && assignedAt != null
                && releasedAt.isBefore(assignedAt)) {

            throw new IllegalArgumentException(
                    "Release date cannot be before assignment date.");
        }

        this.releasedAt = releasedAt;
    }

    public void setNotes(String notes) {

        if (notes != null && notes.trim().length() > 255) {
            throw new IllegalArgumentException(
                    "Notes cannot exceed 255 characters.");
        }

        this.notes = notes == null ? null : notes.trim();
    }

    // Validation Methods
    private void validateAssignedAt(LocalDateTime date) {

        if (date == null || date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Invalid assignment date.");
        }
    }

    // Business Methods
    public boolean isActive() {
        return releasedAt == null;
    }

    public void release() {

        if (!isActive()) {
            throw new IllegalStateException(
                    "This assignment has already been released.");
        }

        releasedAt = LocalDateTime.now();
    }

    // Object Methods
    @Override
    public String toString() {
        return "DoctorAssignment{" +
                "assignmentId=" + assignmentId +
                ", admissionId=" + admissionId +
                ", doctorId=" + doctorId +
                ", assignedAt=" + assignedAt +
                ", releasedAt=" + releasedAt +
                ", active=" + isActive() +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DoctorAssignment)) {
            return false;
        }

        DoctorAssignment assignment = (DoctorAssignment) obj;

        return assignmentId != null
                && assignmentId.equals(assignment.assignmentId);
    }

    @Override
    public int hashCode() {
        return assignmentId == null
                ? System.identityHashCode(this)
                : assignmentId.hashCode();
    }
}
