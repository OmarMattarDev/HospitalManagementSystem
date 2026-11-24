
-- 1. Persons Table
CREATE TABLE persons (
    person_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    date_of_birth DATE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;


-- 2. Patients Table
CREATE TABLE patients (
    patient_id BIGINT PRIMARY KEY,
    blood_type ENUM(
        'A_POSITIVE',
        'A_NEGATIVE',
        'B_POSITIVE',
        'B_NEGATIVE',
        'AB_POSITIVE',
        'AB_NEGATIVE',
        'O_POSITIVE',
        'O_NEGATIVE'
    ),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),

    CONSTRAINT fk_patients_person
        FOREIGN KEY (patient_id)
        REFERENCES persons(person_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB;


-- 3. Departments Table
CREATE TABLE departments (
    department_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;


-- 4. Doctors Table
CREATE TABLE doctors (
    doctor_id BIGINT PRIMARY KEY,
    department_id BIGINT,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    hire_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_doctor_fee
        CHECK (consultation_fee >= 0),

    CONSTRAINT fk_doctors_person
        FOREIGN KEY (doctor_id)
        REFERENCES persons(person_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_doctors_department
        FOREIGN KEY (department_id)
        REFERENCES departments(department_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE = InnoDB;


-- 5. Admissions Table
CREATE TABLE admissions (
    admission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    admission_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discharge_date DATETIME,
    status ENUM(
        'ADMITTED',
        'DISCHARGED',
        'ICU'
    ) NOT NULL DEFAULT 'ADMITTED',
    daily_cost DECIMAL(10,2) NOT NULL,
    room_number VARCHAR(20),
    notes TEXT,

    CONSTRAINT chk_admission_cost
        CHECK (daily_cost >= 0),

    CONSTRAINT chk_discharge_date
        CHECK (
            discharge_date IS NULL
            OR discharge_date >= admission_date
        ),

    CONSTRAINT chk_admission_status
        CHECK (
            (status = 'DISCHARGED' AND discharge_date IS NOT NULL)
            OR
            (status IN ('ADMITTED', 'ICU') AND discharge_date IS NULL)
        ),

    CONSTRAINT fk_admissions_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB;


-- 6. Diagnoses Table
CREATE TABLE diagnoses (
    diagnosis_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    admission_id BIGINT NOT NULL,
    diagnosis_name VARCHAR(150) NOT NULL,
    description TEXT,
    diagnosed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_diagnoses_admission
        FOREIGN KEY (admission_id)
        REFERENCES admissions(admission_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB;


-- 7. Doctor Assignments Table
CREATE TABLE doctor_assignments (
    assignment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    admission_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    released_at DATETIME,
    notes VARCHAR(255),

    CONSTRAINT chk_assignment_dates
        CHECK (
            released_at IS NULL
            OR released_at >= assigned_at
        ),

    CONSTRAINT fk_assignments_admission
        FOREIGN KEY (admission_id)
        REFERENCES admissions(admission_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_assignments_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(doctor_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB;
