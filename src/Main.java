import java.util.*;

public class Main {

    public static void main(String[] args) {

        // =========================
        // Create Hospital
        // =========================

        Hospital hospital = new Hospital();


        // =========================
        // Create Patients
        // =========================

        Patient p1 = new Patient(
                1001,
                "Omar",
                22,
                "Cardiology",
                5,
                1200.0,
                PatientStatus.ADMITTED
        );

        Patient p2 = new Patient(
                1002,
                "Ali",
                45,
                "Neurology",
                12,
                2500.0,
                PatientStatus.ICU
        );

        Patient p3 = new Patient(
                1003,
                "Ahmed",
                60,
                "Cardiology",
                3,
                800.0,
                PatientStatus.DISCHARGED
        );

        Patient p4 = new Patient(
                1004,
                "Mona",
                34,
                "Orthopedics",
                15,
                1800.0,
                PatientStatus.ICU
        );

        Patient p5 = new Patient(
                1005,
                "Sara",
                29,
                "Neurology",
                2,
                950.0,
                PatientStatus.ADMITTED
        );


        // =========================
        // Create Doctors
        // =========================

        Doctor d1 = new Doctor(
                5001,
                "Dr. Mohamed",
                45,
                "Cardiology",
                25000.0
        );

        Doctor d2 = new Doctor(
                5002,
                "Dr. Khaled",
                52,
                "Neurology",
                35000.0
        );

        Doctor d3 = new Doctor(
                5003,
                "Dr. Youssef",
                38,
                "Orthopedics",
                18000.0
        );

        Doctor d4 = new Doctor(
                5004,
                "Dr. Hassan",
                29,
                "Cardiology",
                12000.0
        );


        // =========================
        // Add Patients & Doctors
        // =========================

        hospital.addPatient(p1);
        hospital.addPatient(p2);
        hospital.addPatient(p3);
        hospital.addPatient(p4);
        hospital.addPatient(p5);

        hospital.addDoctor(d1);
        hospital.addDoctor(d2);
        hospital.addDoctor(d3);
        hospital.addDoctor(d4);


        // =========================
        // Assign Patients to Doctors
        // =========================

        hospital.assignPatientToDoctor(5001, 1001);
        hospital.assignPatientToDoctor(5001, 1003);
        hospital.assignPatientToDoctor(5002, 1002);
        hospital.assignPatientToDoctor(5002, 1005);
        hospital.assignPatientToDoctor(5003, 1004);


        // =========================
        // Basic Information
        // =========================

        System.out.println("========================================");
        System.out.println("       HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("========================================");

        System.out.println("\nTotal Patients : " + hospital.getPatientCount());
        System.out.println("Total Doctors  : " + hospital.getDoctorCount());


        // =========================
        // Display All Records
        // =========================

        System.out.println("\n========== ALL PATIENTS ==========");
        hospital.getPatients().forEach(System.out::println);

        System.out.println("\n========== ALL DOCTORS ==========");
        hospital.getDoctors().forEach(System.out::println);


        // =========================
        // Find Operations
        // =========================

        System.out.println("\n========== FIND OPERATION ==========");
        System.out.println("Find Patient 1001 : " + hospital.findPatient(1001).getName());
        System.out.println("Find Doctor 5001  : " + hospital.findDoctor(5001).getName());


        // =========================
        // Filtering Patients
        // =========================

        System.out.println("\n========== ICU PATIENTS ==========");
        hospital.getICUPatients()
                .forEach(p -> System.out.println(p.getName() + " -> Illness: " + p.getIllness()));

        System.out.println("\n========== DISCHARGED PATIENTS ==========");
        hospital.getDischargedPatients()
                .forEach(p -> System.out.println(p.getName()));

        System.out.println("\n========== PATIENTS STAYED > 5 DAYS ==========");
        hospital.getPatientsStayedMoreThan(5)
                .forEach(p -> System.out.println(p.getName() + " -> Stayed: " + p.getDaysStayed() + " days"));

        System.out.println("\n========== PATIENTS WITH BILL > 10,000 ==========");
        hospital.getPatientsWithBillGreaterThan(10000)
                .forEach(p -> System.out.println(p.getName() + " -> Bill: " + p.calculateBill()));


        // =========================
        // Filtering Doctors
        // =========================

        System.out.println("\n========== DOCTORS BY SPECIALIZATION (CARDIOLOGY) ==========");
        hospital.findDoctorsBySpecialization("Cardiology")
                .forEach(d -> System.out.println(d.getName()));

        System.out.println("\n========== DOCTORS SALARY > 20,000 ==========");
        hospital.getDoctorsWithSalaryGreaterThan(20000)
                .forEach(d -> System.out.println(d.getName() + " -> " + d.getSalary()));

        System.out.println("\n========== DOCTORS WITH PATIENTS > 1 ==========");
        hospital.getDoctorsByPatientCount(1)
                .forEach(d -> System.out.println(d.getName() + " -> Patients: " + d.getPatientCount()));


        // =========================
        // Sorting
        // =========================

        System.out.println("\n========== PATIENTS SORTED BY BILL (DESC) ==========");
        hospital.sortPatientsByBills()
                .forEach(p -> System.out.println(p.getName() + " -> " + p.calculateBill()));

        System.out.println("\n========== DOCTORS SORTED BY SALARY (DESC) ==========");
        hospital.sortDoctorsBySalary()
                .forEach(d -> System.out.println(d.getName() + " -> " + d.getSalary()));

        System.out.println("\n========== PATIENTS SORTED BY AGE ==========");
        hospital.sortPatientsByAge()
                .forEach(p -> System.out.println(p.getName() + " -> Age: " + p.getAge()));


        // =========================
        // Top N Queries
        // =========================

        System.out.println("\n========== TOP 3 HIGHEST BILL PATIENTS ==========");
        hospital.topHighestBills(3)
                .forEach(p -> System.out.println(p.getName() + " -> Bill: " + p.calculateBill()));

        System.out.println("\n========== TOP 2 LONGEST STAY PATIENTS ==========");
        hospital.topLongestStay(2)
                .forEach(p -> System.out.println(p.getName() + " -> Days: " + p.getDaysStayed()));


        // =========================
        // Min / Max + Optional
        // =========================

        System.out.println("\n========== EXTREMES (MIN / MAX) ==========");

        hospital.highestBillPatient()
                .ifPresent(p -> System.out.println("Highest Bill Patient : " + p.getName() + " (" + p.calculateBill() + ")"));

        hospital.highestPaidDoctor()
                .ifPresent(d -> System.out.println("Highest Paid Doctor  : " + d.getName() + " (" + d.getSalary() + ")"));

        hospital.youngestPatient()
                .ifPresent(p -> System.out.println("Youngest Patient     : " + p.getName() + " (" + p.getAge() + " yo)"));

        hospital.doctorWithMostPatients()
                .ifPresent(d -> System.out.println("Doctor Most Patients : " + d.getName() + " (" + d.getPatientCount() + " patients)"));


        // =========================
        // Statistics & Calculations
        // =========================

        System.out.println("\n========== FINANCIAL & BILL STATISTICS ==========");
        System.out.println("Total Bills        : " + hospital.totalBills());
        System.out.println("Total ICU Bills    : " + hospital.totalICUBills());
        System.out.println("Average Patient Bill: " + hospital.averagePatientBill());
        System.out.println("Total Doctors Salary: " + hospital.totalDoctorsSalary());
        System.out.println("Average Doctor Salary: " + hospital.averageDoctorSalary());

        System.out.println("\n========== DETAILED SUMMARY STATISTICS ==========");
        System.out.println("Bill Stats : " + hospital.patientBillStatistics());
        System.out.println("Stay Stats : " + hospital.stayDaysStatistics());


        // =========================
        // Grouping & Aggregating
        // =========================

        System.out.println("\n========== PATIENTS BY STATUS ==========");
        hospital.groupPatientsByStatus()
                .forEach((status, patients) ->
                        System.out.println(status + " -> " + patients.stream().map(Patient::getName).toList())
                );

        System.out.println("\n========== DOCTORS BY SPECIALIZATION ==========");
        hospital.groupDoctorsBySpecialization()
                .forEach((spec, doctors) ->
                        System.out.println(spec + " -> " + doctors.stream().map(Doctor::getName).toList())
                );

        System.out.println("\n========== PATIENT COUNT PER ILLNESS ==========");
        hospital.patientCountPerIllness()
                .forEach((illness, count) -> System.out.println(illness + " -> " + count));

        System.out.println("\n========== AVERAGE BILL PER ILLNESS ==========");
        hospital.averageBillPerIllness()
                .forEach((illness, avg) -> System.out.println(illness + " -> " + avg));


        // =========================
        // Partitioning
        // =========================

        System.out.println("\n========== PARTITION PATIENTS BY ICU ==========");
        Map<Boolean, List<Patient>> icuPartition = hospital.partitionPatientsByICU();
        System.out.println("In ICU     : " + icuPartition.get(true).stream().map(Patient::getName).toList());
        System.out.println("Not In ICU : " + icuPartition.get(false).stream().map(Patient::getName).toList());


        // =========================
        // Matching Operations
        // =========================

        System.out.println("\n========== MATCHING CHECKS ==========");
        System.out.println("Any Patient in ICU?           : " + hospital.anyPatientInICU());
        System.out.println("All Patients Discharged?      : " + hospital.allPatientsDischarged());
        System.out.println("Any Doctor without Patients?  : " + hospital.anyDoctorWithoutPatients());


        // =========================
        // Joining & Collections
        // =========================

        System.out.println("\n========== NAMES & LISTS ==========");
        System.out.println("All Doctor Names   : " + hospital.allDoctorNames());
        System.out.println("All Patient Names  : " + hospital.allPatientNames());
        System.out.println("Unique Specializations : " + hospital.uniqueSpecializations());


        // =========================
        // Assignments & Relationships
        // =========================

        System.out.println("\n========== DOCTOR PATIENT MAPPING ==========");
        hospital.countPatientsForDoctors()
                .forEach((docId, count) ->
                        System.out.println("Doctor ID [" + docId + "] -> Assigned Patients: " + count)
                );

        System.out.println("\n========== ALL ASSIGNED PATIENTS ==========");
        hospital.allAssignedPatients()
                .forEach(p -> System.out.println("Assigned: " + p.getName()));


        // =========================
        // Data Updates Test
        // =========================

        System.out.println("\n========== UPDATING DATA ==========");
        p1.increaseStayDays(3);
        p1.admitToICU();
        d1.increaseSalary(3000);

        System.out.println("Updated Omar Stay Days : " + p1.getDaysStayed());
        System.out.println("Updated Omar Status    : " + p1.getStatus());
        System.out.println("Updated Dr.Mohamed Salary : " + d1.getSalary());


        // =========================
        // Final Summary
        // =========================

        System.out.println("\n========================================");
        System.out.println("             FINAL SUMMARY");
        System.out.println("========================================");

        System.out.println("Patients Count  : " + hospital.getPatientCount());
        System.out.println("Doctors Count   : " + hospital.getDoctorCount());
        System.out.println("ICU Patients    : " + hospital.countICUPatients());
        System.out.println("Total Hospital Bills : " + hospital.totalBills());
        System.out.println("Total Salaries Expense: " + hospital.totalDoctorsSalary());

        System.out.println("\n========================================");
        System.out.println("      HOSPITAL SYSTEM FINISHED");
        System.out.println("========================================");
    }
}