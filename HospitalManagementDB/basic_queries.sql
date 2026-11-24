
-- 1. Get All Patients
SELECT
    p.person_id,
    p.first_name,
    p.last_name,
    p.phone,
    pt.blood_type
FROM persons p
JOIN patients pt
    ON p.person_id = pt.patient_id;


-- 2. Get All Doctors With Departments
SELECT
    d.doctor_id,
    p.first_name,
    p.last_name,
    d.specialization,
    dep.department_name
FROM doctors d
JOIN persons p
    ON d.doctor_id = p.person_id
LEFT JOIN departments dep
    ON d.department_id = dep.department_id;


-- 3. Get Currently Admitted Patients
SELECT
    a.admission_id,
    p.first_name,
    p.last_name,
    a.admission_date,
    a.status
FROM admissions a
JOIN patients pt
    ON a.patient_id = pt.patient_id
JOIN persons p
    ON pt.patient_id = p.person_id
WHERE a.status IN ('ADMITTED', 'ICU');


-- 4. Get Patients In ICU
SELECT
    p.person_id,
    p.first_name,
    p.last_name,
    a.admission_id
FROM admissions a
JOIN patients pt
    ON a.patient_id = pt.patient_id
JOIN persons p
    ON pt.patient_id = p.person_id
WHERE a.status = 'ICU';


-- 5. Get Doctors Assigned To Each Admission
SELECT
    a.admission_id,
    p.first_name AS doctor_first_name,
    p.last_name AS doctor_last_name,
    d.specialization,
    da.assigned_at,
    da.released_at
FROM doctor_assignments da
JOIN doctors d
    ON da.doctor_id = d.doctor_id
JOIN persons p
    ON d.doctor_id = p.person_id
JOIN admissions a
    ON da.admission_id = a.admission_id;


-- 6. Get Diagnoses For Each Admission
SELECT
    a.admission_id,
    dg.diagnosis_name,
    dg.diagnosed_at
FROM diagnoses dg
JOIN admissions a
    ON dg.admission_id = a.admission_id;


-- 7. Count Patients By Admission Status
SELECT
    status,
    COUNT(*) AS total_admissions
FROM admissions
GROUP BY status;


-- 8. Get Total Hospital Admission Costs
SELECT
    SUM(
        daily_cost *
        CASE
            WHEN status = 'DISCHARGED'
                THEN GREATEST(
                    DATEDIFF(discharge_date, admission_date),
                    1
                )
            ELSE GREATEST(
                DATEDIFF(CURRENT_DATE(), admission_date),
                1
            )
        END
    ) AS total_cost
FROM admissions;


-- 9. Get Active Doctor Assignments
SELECT
    doctor_id,
    admission_id,
    assigned_at
FROM doctor_assignments
WHERE released_at IS NULL;
