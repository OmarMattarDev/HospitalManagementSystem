
-- Patients
CREATE INDEX idx_persons_name
ON persons(first_name, last_name);

CREATE INDEX idx_persons_phone
ON persons(phone);


-- Doctors
CREATE INDEX idx_doctors_department
ON doctors(department_id);

CREATE INDEX idx_doctors_specialization
ON doctors(specialization);


-- Admissions
CREATE INDEX idx_admissions_patient
ON admissions(patient_id);

CREATE INDEX idx_admissions_status
ON admissions(status);

CREATE INDEX idx_admissions_date
ON admissions(admission_date);


-- Diagnoses
CREATE INDEX idx_diagnoses_admission
ON diagnoses(admission_id);


-- Doctor Assignments
CREATE INDEX idx_assignments_admission
ON doctor_assignments(admission_id);

CREATE INDEX idx_assignments_doctor
ON doctor_assignments(doctor_id);

CREATE INDEX idx_assignments_active
ON doctor_assignments(released_at);
