
package com.hospital.application;

import com.hospital.config.DatabaseConnection;

import com.hospital.dao.*;

import com.hospital.model.*;
import com.hospital.model.enums.*;

import com.hospital.service.*;

import com.hospital.workflow.*;

import com.hospital.report.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("     HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("========================================");

        try (Connection connection = DatabaseConnection.getConnection()) {

            System.out.println("\nDatabase connection established successfully.");

            runHospitalSystem(connection);

        } catch (SQLException exception) {

            System.out.println("\nDatabase error occurred:");
            exception.printStackTrace();

        } catch (Exception exception) {

            System.out.println("\nApplication error occurred:");
            exception.printStackTrace();
        }
    }

    private static void runHospitalSystem(Connection connection)
            throws SQLException {

        // ========================================
        // 1. INITIALIZE SERVICES
        // ========================================

        PatientService patientService =
                new PatientService(connection);

        DoctorService doctorService =
                new DoctorService(connection);

        DepartmentService departmentService =
                new DepartmentService(connection);

        AdmissionService admissionService =
                new AdmissionService(connection);

        DiagnosisService diagnosisService =
                new DiagnosisService(connection);

        DoctorAssignmentService assignmentService =
                new DoctorAssignmentService(connection);


        // ========================================
        // 2. INITIALIZE WORKFLOWS
        // ========================================

        PatientWorkflow patientWorkflow =
                new PatientWorkflow(connection);

        DoctorWorkflow doctorWorkflow =
                new DoctorWorkflow(connection);

        DepartmentWorkflow departmentWorkflow =
                new DepartmentWorkflow(connection);

        AdmissionWorkflow admissionWorkflow =
                new AdmissionWorkflow(connection);

        DiagnosisWorkflow diagnosisWorkflow =
                new DiagnosisWorkflow(connection);

        DoctorAssignmentWorkflow assignmentWorkflow =
                new DoctorAssignmentWorkflow(connection);


        // ========================================
        // 3. INITIALIZE REPORTS
        // ========================================

        PatientReport patientReport =
                new PatientReport(connection);

        DoctorReport doctorReport =
                new DoctorReport(connection);

        DepartmentReport departmentReport =
                new DepartmentReport(connection);

        AdmissionReport admissionReport =
                new AdmissionReport(connection);

        HospitalReport hospitalReport =
                new HospitalReport(connection);


        // ========================================
        // 4. CREATE DEPARTMENTS
        // ========================================

        printSection("CREATING DEPARTMENTS");

        Department cardiology = new Department(
                "Cardiology",
                "Heart and cardiovascular diseases department",
                "Floor 1"
        );

        Department neurology = new Department(
                "Neurology",
                "Brain and nervous system department",
                "Floor 2"
        );

        Department orthopedics = new Department(
                "Orthopedics",
                "Bones and joints department",
                "Floor 3"
        );

        Long cardiologyId =
                departmentWorkflow.createDepartment(cardiology);

        Long neurologyId =
                departmentWorkflow.createDepartment(neurology);

        Long orthopedicsId =
                departmentWorkflow.createDepartment(orthopedics);

        System.out.println("Cardiology ID: " + cardiologyId);
        System.out.println("Neurology ID: " + neurologyId);
        System.out.println("Orthopedics ID: " + orthopedicsId);


        // ========================================
        // 5. REGISTER PATIENTS
        // ========================================

        printSection("REGISTERING PATIENTS");

        Patient patient1 = new Patient(
                "Omar",
                "Hassan",
                Gender.MALE,
                LocalDate.of(2001, 4, 12),
                "01012345678",
                "omar.hassan@example.com",
                "Cairo, Egypt",
                BloodType.O_POSITIVE,
                "Ahmed Hassan",
                "01011112222"
        );

        Patient patient2 = new Patient(
                "Sara",
                "Mohamed",
                Gender.FEMALE,
                LocalDate.of(1998, 8, 20),
                "01023456789",
                "sara.mohamed@example.com",
                "Giza, Egypt",
                BloodType.A_POSITIVE,
                "Mona Mohamed",
                "01022223333"
        );

        Patient patient3 = new Patient(
                "Ahmed",
                "Ali",
                Gender.MALE,
                LocalDate.of(1985, 2, 15),
                "01034567890",
                "ahmed.ali@example.com",
                "Alexandria, Egypt",
                BloodType.B_POSITIVE,
                "Ali Hassan",
                "01033334444"
        );

        Long patient1Id =
                patientWorkflow.registerPatient(patient1);

        Long patient2Id =
                patientWorkflow.registerPatient(patient2);

        Long patient3Id =
                patientWorkflow.registerPatient(patient3);

        System.out.println("Patient 1 ID: " + patient1Id);
        System.out.println("Patient 2 ID: " + patient2Id);
        System.out.println("Patient 3 ID: " + patient3Id);


        // ========================================
        // 6. REGISTER DOCTORS
        // ========================================

        printSection("REGISTERING DOCTORS");

        Doctor doctor1 = new Doctor(
                "Mahmoud",
                "Ibrahim",
                Gender.MALE,
                LocalDate.of(1980, 5, 10),
                "01112345678",
                "mahmoud.doctor@example.com",
                "Cairo, Egypt",
                cardiologyId,
                "Cardiologist",
                "DOC-CARD-001",
                new BigDecimal("500.00"),
                LocalDate.of(2010, 3, 1)
        );

        Doctor doctor2 = new Doctor(
                "Mona",
                "Ahmed",
                Gender.FEMALE,
                LocalDate.of(1987, 9, 18),
                "01123456789",
                "mona.doctor@example.com",
                "Giza, Egypt",
                neurologyId,
                "Neurologist",
                "DOC-NEUR-002",
                new BigDecimal("650.00"),
                LocalDate.of(2015, 6, 1)
        );

        Doctor doctor3 = new Doctor(
                "Khaled",
                "Samir",
                Gender.MALE,
                LocalDate.of(1978, 11, 25),
                "01134567890",
                "khaled.doctor@example.com",
                "Alexandria, Egypt",
                orthopedicsId,
                "Orthopedic Surgeon",
                "DOC-ORTH-003",
                new BigDecimal("700.00"),
                LocalDate.of(2008, 1, 15)
        );

        Long doctor1Id =
                doctorWorkflow.registerDoctor(doctor1);

        Long doctor2Id =
                doctorWorkflow.registerDoctor(doctor2);

        Long doctor3Id =
                doctorWorkflow.registerDoctor(doctor3);

        System.out.println("Doctor 1 ID: " + doctor1Id);
        System.out.println("Doctor 2 ID: " + doctor2Id);
        System.out.println("Doctor 3 ID: " + doctor3Id);


        // ========================================
        // 7. CREATE PATIENT ADMISSIONS
        // ========================================

        printSection("CREATING ADMISSIONS");

        Admission admission1 = new Admission(
                patient1Id,
                new BigDecimal("1200.00"),
                "A-101",
                "Patient admitted for heart examination."
        );

        Admission admission2 = new Admission(
                patient2Id,
                new BigDecimal("1500.00"),
                "B-205",
                "Patient admitted for neurological examination."
        );

        Admission admission3 = new Admission(
                patient3Id,
                new BigDecimal("1000.00"),
                "C-301",
                "Patient admitted for orthopedic treatment."
        );

        Long admission1Id =
                admissionWorkflow.admitPatient(admission1);

        Long admission2Id =
                admissionWorkflow.admitPatient(admission2);

        Long admission3Id =
                admissionWorkflow.admitPatient(admission3);

        System.out.println("Admission 1 ID: " + admission1Id);
        System.out.println("Admission 2 ID: " + admission2Id);
        System.out.println("Admission 3 ID: " + admission3Id);


        // ========================================
        // 8. ADD DIAGNOSES
        // ========================================

        printSection("ADDING DIAGNOSES");

        Diagnosis diagnosis1 = new Diagnosis(
                admission1Id,
                "Hypertension",
                "Patient has high blood pressure."
        );

        Diagnosis diagnosis2 = new Diagnosis(
                admission1Id,
                "Chest Pain",
                "Patient reports recurring chest pain."
        );

        Diagnosis diagnosis3 = new Diagnosis(
                admission2Id,
                "Migraine",
                "Patient suffers from recurring migraine."
        );

        Diagnosis diagnosis4 = new Diagnosis(
                admission3Id,
                "Knee Injury",
                "Patient has a knee joint injury."
        );

        Long diagnosis1Id =
                diagnosisWorkflow.addDiagnosis(diagnosis1);

        Long diagnosis2Id =
                diagnosisWorkflow.addDiagnosis(diagnosis2);

        Long diagnosis3Id =
                diagnosisWorkflow.addDiagnosis(diagnosis3);

        Long diagnosis4Id =
                diagnosisWorkflow.addDiagnosis(diagnosis4);

        System.out.println("Diagnosis 1 ID: " + diagnosis1Id);
        System.out.println("Diagnosis 2 ID: " + diagnosis2Id);
        System.out.println("Diagnosis 3 ID: " + diagnosis3Id);
        System.out.println("Diagnosis 4 ID: " + diagnosis4Id);


        // ========================================
        // 9. ASSIGN DOCTORS TO ADMISSIONS
        // ========================================

        printSection("ASSIGNING DOCTORS");

        DoctorAssignment assignment1 = new DoctorAssignment(
                admission1Id,
                doctor1Id,
                "Primary cardiologist."
        );

        DoctorAssignment assignment2 = new DoctorAssignment(
                admission2Id,
                doctor2Id,
                "Primary neurologist."
        );

        DoctorAssignment assignment3 = new DoctorAssignment(
                admission3Id,
                doctor3Id,
                "Primary orthopedic surgeon."
        );

        Long assignment1Id =
                assignmentWorkflow.assignDoctor(assignment1);

        Long assignment2Id =
                assignmentWorkflow.assignDoctor(assignment2);

        Long assignment3Id =
                assignmentWorkflow.assignDoctor(assignment3);

        System.out.println("Assignment 1 ID: " + assignment1Id);
        System.out.println("Assignment 2 ID: " + assignment2Id);
        System.out.println("Assignment 3 ID: " + assignment3Id);


        // ========================================
        // 10. TEST PATIENT SERVICES
        // ========================================

        printSection("TESTING PATIENT SERVICES");

        System.out.println("All Patients:");
        printList(patientService.getAllPatients());

        System.out.println("\nPatient By ID:");
        System.out.println(patientService.getPatientById(patient1Id));

        System.out.println("\nPatients With O_POSITIVE Blood Type:");
        printList(
                patientService.getPatientsByBloodType(
                        BloodType.O_POSITIVE
                )
        );

        System.out.println("\nMale Patients:");
        printList(
                patientService.getPatientsByGender(Gender.MALE)
        );

        System.out.println("\nPatients Sorted By Name:");
        printList(patientService.getPatientsSortedByName());


        // ========================================
        // 11. TEST DOCTOR SERVICES
        // ========================================

        printSection("TESTING DOCTOR SERVICES");

        System.out.println("All Doctors:");
        printList(doctorService.getAllDoctors());

        System.out.println("\nDoctors By Cardiology Department:");
        printList(
                doctorService.getDoctorsByDepartment(cardiologyId)
        );

        System.out.println("\nActive Doctors:");
        printList(doctorService.getActiveDoctors());

        System.out.println("\nDoctors By Specialization:");
        printList(
                doctorService.getDoctorsBySpecialization("Cardiologist")
        );

        System.out.println("\nDoctors Sorted By Consultation Fee:");
        printList(doctorService.getDoctorsSortedByConsultationFee());


        // ========================================
        // 12. TEST DEPARTMENT SERVICES
        // ========================================

        printSection("TESTING DEPARTMENT SERVICES");

        System.out.println("All Departments:");
        printList(departmentService.getAllDepartments());

        System.out.println("\nDepartment By Name:");
        System.out.println(
                departmentService.getDepartmentByName("Cardiology")
        );

        System.out.println("\nDepartments Sorted By Name:");
        printList(departmentService.getDepartmentsSortedByName());


        // ========================================
        // 13. TEST ADMISSION SERVICES
        // ========================================

        printSection("TESTING ADMISSION SERVICES");

        System.out.println("All Admissions:");
        printList(admissionService.getAllAdmissions());

        System.out.println("\nAdmissions By Patient:");
        printList(
                admissionService.getAdmissionsByPatient(patient1Id)
        );

        System.out.println("\nAdmissions With ADMITTED Status:");
        printList(
                admissionService.getAdmissionsByStatus(
                        AdmissionStatus.ADMITTED
                )
        );

        System.out.println("\nActive Admissions:");
        printList(admissionService.getActiveAdmissions());

        System.out.println("\nAdmissions Sorted By Date:");
        printList(admissionService.getAdmissionsSortedByDate());


        // ========================================
        // 14. TEST DIAGNOSIS SERVICES
        // ========================================

        printSection("TESTING DIAGNOSIS SERVICES");

        System.out.println("All Diagnoses:");
        printList(diagnosisService.getAllDiagnoses());

        System.out.println("\nDiagnoses By Admission:");
        printList(
                diagnosisService.getDiagnosesByAdmission(admission1Id)
        );

        System.out.println("\nDiagnoses Sorted By Date:");
        printList(diagnosisService.getDiagnosesSortedByDate());


        // ========================================
        // 15. TEST DOCTOR ASSIGNMENT SERVICES
        // ========================================

        printSection("TESTING DOCTOR ASSIGNMENT SERVICES");

        System.out.println("All Assignments:");
        printList(assignmentService.getAllAssignments());

        System.out.println("\nAssignments By Admission:");
        printList(
                assignmentService.getAssignmentsByAdmission(admission1Id)
        );

        System.out.println("\nAssignments By Doctor:");
        printList(
                assignmentService.getAssignmentsByDoctor(doctor1Id)
        );

        System.out.println("\nActive Assignments:");
        printList(assignmentService.getActiveAssignments());

        System.out.println("\nAssignments Sorted By Date:");
        printList(assignmentService.getAssignmentsSortedByDate());


        // ========================================
        // 16. TEST UPDATE OPERATIONS
        // ========================================

        printSection("TESTING UPDATE OPERATIONS");

        Patient patientToUpdate =
                patientService.getPatientById(patient1Id);

        patientToUpdate.setPhone("01099998888");

        patientService.updatePatient(patientToUpdate);

        System.out.println("Updated Patient:");
        System.out.println(
                patientService.getPatientById(patient1Id)
        );


        Doctor doctorToUpdate =
                doctorService.getDoctorById(doctor1Id);

        doctorToUpdate.setConsultationFee(
                new BigDecimal("550.00")
        );

        doctorService.updateDoctor(doctorToUpdate);

        System.out.println("\nUpdated Doctor:");
        System.out.println(
                doctorService.getDoctorById(doctor1Id)
        );


        Department departmentToUpdate =
                departmentService.getDepartmentById(cardiologyId);

        departmentToUpdate.setDescription(
                "Updated cardiology department description."
        );

        departmentService.updateDepartment(departmentToUpdate);

        System.out.println("\nUpdated Department:");
        System.out.println(
                departmentService.getDepartmentById(cardiologyId)
        );


        Diagnosis diagnosisToUpdate =
                diagnosisService.getDiagnosisById(diagnosis1Id);

        diagnosisToUpdate.setDescription(
                "Updated diagnosis description after examination."
        );

        diagnosisService.updateDiagnosis(diagnosisToUpdate);

        System.out.println("\nUpdated Diagnosis:");
        System.out.println(
                diagnosisService.getDiagnosisById(diagnosis1Id)
        );


        // ========================================
        // 17. TEST RELEASE DOCTOR
        // ========================================

        printSection("TESTING DOCTOR RELEASE");

        assignmentWorkflow.releaseDoctor(assignment2Id);

        System.out.println("Assignment released successfully.");

        System.out.println("\nActive Assignments After Release:");
        printList(assignmentService.getActiveAssignments());


        // ========================================
        // 18. TEST PATIENT DISCHARGE
        // ========================================

        printSection("TESTING PATIENT DISCHARGE");

        admissionWorkflow.dischargePatient(admission1Id);

        System.out.println("Patient discharged successfully.");

        System.out.println("\nUpdated Admission:");
        System.out.println(
                admissionService.getAdmissionById(admission1Id)
        );

        System.out.println("\nAssignments After Patient Discharge:");
        printList(
                assignmentService.getAssignmentsByAdmission(admission1Id)
        );


        // ========================================
        // 19. TEST DELETE OPERATIONS
        // ========================================

        printSection("TESTING DELETE OPERATIONS");

        // Temporary diagnosis
        Diagnosis temporaryDiagnosis = new Diagnosis(
                admission3Id,
                "Temporary Diagnosis",
                "This diagnosis is created only to test deletion."
        );

        Long temporaryDiagnosisId =
                diagnosisService.addDiagnosis(temporaryDiagnosis);

        boolean diagnosisDeleted =
                diagnosisService.deleteDiagnosis(temporaryDiagnosisId);

        System.out.println(
                "Temporary Diagnosis Deleted: " + diagnosisDeleted
        );


        // Temporary department
        Department temporaryDepartment = new Department(
                "Temporary Department",
                "Department created for deletion testing.",
                "Floor 4"
        );

        Long temporaryDepartmentId =
                departmentService.addDepartment(temporaryDepartment);

        boolean departmentDeleted =
                departmentService.deleteDepartment(temporaryDepartmentId);

        System.out.println(
                "Temporary Department Deleted: " + departmentDeleted
        );


        // Temporary patient
        Patient temporaryPatient = new Patient(
                "Temporary",
                "Patient",
                Gender.MALE,
                LocalDate.of(1995, 1, 1),
                "01077776666",
                "temporary.patient@example.com",
                "Cairo, Egypt",
                BloodType.AB_POSITIVE,
                "Temporary Contact",
                "01066667777"
        );

        Long temporaryPatientId =
                patientWorkflow.registerPatient(temporaryPatient);

        boolean patientDeleted =
                patientService.deletePatient(temporaryPatientId);

        System.out.println(
                "Temporary Patient Deleted: " + patientDeleted
        );


        // Temporary doctor
        Doctor temporaryDoctor = new Doctor(
                "Temporary",
                "Doctor",
                Gender.FEMALE,
                LocalDate.of(1990, 3, 10),
                "01177776666",
                "temporary.doctor@example.com",
                "Cairo, Egypt",
                cardiologyId,
                "Temporary Specialist",
                "DOC-TEMP-999",
                new BigDecimal("300.00"),
                LocalDate.of(2020, 1, 1)
        );

        Long temporaryDoctorId =
                doctorWorkflow.registerDoctor(temporaryDoctor);

        boolean doctorDeleted =
                doctorService.deleteDoctor(temporaryDoctorId);

        System.out.println(
                "Temporary Doctor Deleted: " + doctorDeleted
        );


        // ========================================
        // 20. PATIENT REPORT
        // ========================================

        printSection("PATIENT REPORT");

        System.out.println(
                "Total Patients: " +
                        patientReport.getTotalPatients()
        );

        System.out.println(
                "Patients By Gender: " +
                        patientReport.getPatientsCountByGender()
        );

        System.out.println(
                "Patients By Blood Type: " +
                        patientReport.getPatientsCountByBloodType()
        );


        // ========================================
        // 21. DOCTOR REPORT
        // ========================================

        printSection("DOCTOR REPORT");

        System.out.println(
                "Total Doctors: " +
                        doctorReport.getTotalDoctors()
        );

        System.out.println(
                "Active Doctors: " +
                        doctorReport.getActiveDoctorsCount()
        );

        System.out.println(
                "Inactive Doctors: " +
                        doctorReport.getInactiveDoctorsCount()
        );

        System.out.println(
                "Doctors By Specialization: " +
                        doctorReport.getDoctorsCountBySpecialization()
        );

        System.out.println(
                "Average Consultation Fee: " +
                        doctorReport.getAverageConsultationFee()
        );


        // ========================================
        // 22. DEPARTMENT REPORT
        // ========================================

        printSection("DEPARTMENT REPORT");

        System.out.println(
                "Total Departments: " +
                        departmentReport.getTotalDepartments()
        );

        System.out.println(
                "Doctors By Department: " +
                        departmentReport.getDoctorsCountByDepartment()
        );

        System.out.println(
                "Active Doctors By Department: " +
                        departmentReport.getActiveDoctorsCountByDepartment()
        );


        // ========================================
        // 23. ADMISSION REPORT
        // ========================================

        printSection("ADMISSION REPORT");

        System.out.println(
                "Total Admissions: " +
                        admissionReport.getTotalAdmissions()
        );

        System.out.println(
                "Active Admissions: " +
                        admissionReport.getActiveAdmissionsCount()
        );

        System.out.println(
                "Admissions By Status: " +
                        admissionReport.getAdmissionsCountByStatus()
        );

        System.out.println(
                "Average Stay Days: " +
                        admissionReport.getAverageStayDays()
        );

        System.out.println(
                "Total Estimated Admission Cost: " +
                        admissionReport.getTotalEstimatedAdmissionCost()
        );


        // ========================================
        // 24. COMPLETE HOSPITAL REPORT
        // ========================================

        printSection("COMPLETE HOSPITAL REPORT");

        System.out.println(
                hospitalReport.generateHospitalSummary()
        );


        // ========================================
        // 25. FINAL DATABASE DATA
        // ========================================

        printSection("FINAL DATABASE DATA");

        System.out.println("Final Patients:");
        printList(patientService.getAllPatients());

        System.out.println("\nFinal Doctors:");
        printList(doctorService.getAllDoctors());

        System.out.println("\nFinal Departments:");
        printList(departmentService.getAllDepartments());

        System.out.println("\nFinal Admissions:");
        printList(admissionService.getAllAdmissions());

        System.out.println("\nFinal Diagnoses:");
        printList(diagnosisService.getAllDiagnoses());

        System.out.println("\nFinal Doctor Assignments:");
        printList(assignmentService.getAllAssignments());


        // ========================================
        // FINISHED
        // ========================================

        printSection("APPLICATION FINISHED");

        System.out.println(
                "All sample operations have been executed successfully."
        );
    }


    // ========================================
    // HELPER METHODS
    // ========================================

    private static void printSection(String title) {

        System.out.println("\n");
        System.out.println("========================================");
        System.out.println(" " + title);
        System.out.println("========================================");
    }

    private static void printList(List<?> items) {

        if (items == null || items.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        items.forEach(System.out::println);
    }
}
