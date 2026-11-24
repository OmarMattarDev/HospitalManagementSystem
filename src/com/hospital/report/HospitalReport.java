
package com.hospital.report;

import java.sql.Connection;
import java.sql.SQLException;

public class HospitalReport {

    private final PatientReport patientReport;
    private final DoctorReport doctorReport;
    private final DepartmentReport departmentReport;
    private final AdmissionReport admissionReport;

    public HospitalReport(Connection connection) {

        if (connection == null) {
            throw new IllegalArgumentException(
                    "Connection cannot be null."
            );
        }

        this.patientReport = new PatientReport(connection);
        this.doctorReport = new DoctorReport(connection);
        this.departmentReport = new DepartmentReport(connection);
        this.admissionReport = new AdmissionReport(connection);
    }

    public String generateHospitalSummary() throws SQLException {

        StringBuilder report = new StringBuilder();

        report.append("====================================\n");
        report.append("       HOSPITAL GENERAL REPORT\n");
        report.append("====================================\n\n");

        report.append("PATIENT STATISTICS\n");
        report.append("------------------\n");

        report.append("Total Patients: ")
                .append(patientReport.getTotalPatients())
                .append("\n");

        report.append("Patients By Gender: ")
                .append(patientReport.getPatientsCountByGender())
                .append("\n");

        report.append("Patients By Blood Type: ")
                .append(patientReport.getPatientsCountByBloodType())
                .append("\n\n");


        report.append("DOCTOR STATISTICS\n");
        report.append("-----------------\n");

        report.append("Total Doctors: ")
                .append(doctorReport.getTotalDoctors())
                .append("\n");

        report.append("Active Doctors: ")
                .append(doctorReport.getActiveDoctorsCount())
                .append("\n");

        report.append("Inactive Doctors: ")
                .append(doctorReport.getInactiveDoctorsCount())
                .append("\n");

        report.append("Doctors By Specialization: ")
                .append(doctorReport.getDoctorsCountBySpecialization())
                .append("\n");

        report.append("Average Consultation Fee: ")
                .append(doctorReport.getAverageConsultationFee())
                .append("\n\n");


        report.append("DEPARTMENT STATISTICS\n");
        report.append("---------------------\n");

        report.append("Total Departments: ")
                .append(departmentReport.getTotalDepartments())
                .append("\n");

        report.append("Doctors By Department: ")
                .append(departmentReport.getDoctorsCountByDepartment())
                .append("\n");

        report.append("Active Doctors By Department: ")
                .append(departmentReport.getActiveDoctorsCountByDepartment())
                .append("\n\n");


        report.append("ADMISSION STATISTICS\n");
        report.append("--------------------\n");

        report.append("Total Admissions: ")
                .append(admissionReport.getTotalAdmissions())
                .append("\n");

        report.append("Active Admissions: ")
                .append(admissionReport.getActiveAdmissionsCount())
                .append("\n");

        report.append("Admissions By Status: ")
                .append(admissionReport.getAdmissionsCountByStatus())
                .append("\n");

        report.append("Average Stay Days: ")
                .append(admissionReport.getAverageStayDays())
                .append("\n");

        report.append("Total Estimated Admission Cost: ")
                .append(admissionReport.getTotalEstimatedAdmissionCost())
                .append("\n\n");

        report.append("====================================\n");
        report.append("          END OF REPORT\n");
        report.append("====================================\n");

        return report.toString();
    }
}
