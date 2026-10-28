import java.util.*;
import java.util.stream.Collectors;

public class Hospital {

    private final Map<Integer, Patient> patients;

    private final Map<Integer, Doctor> doctors;

    public Hospital() {
        patients = new HashMap<>();
        doctors = new HashMap<>();
    }

    public boolean addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        return patients.putIfAbsent(patient.getId(), patient) == null;
    }

    public boolean removePatient(int id) {
        if (id < 1000 || id > 4999) {
            throw new IllegalArgumentException("Patient ID must be between 1,000 and 4,999");
        }
        return patients.remove(id) != null;
    }

    public Patient findPatient(int id) {
        if (id < 1000 || id > 4999) {
            throw new IllegalArgumentException("Patient ID must be between 1,000 and 4,999");
        }
        return patients.get(id);
    }

    public List<Patient> getPatients() {
        return patients.values()
                .stream()
                .toList();
    }

    public int getPatientCount() {
        return patients.size();
    }

    public boolean addDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        return doctors.putIfAbsent(doctor.getId(), doctor) == null;
    }

    public boolean removeDoctor(int id) {
        if (id < 5000) {
            throw new IllegalArgumentException("Doctor ID cannot be less than 5,000");
        }
        return doctors.remove(id) != null;
    }

    public Doctor findDoctor(int id) {
        if (id < 5000) {
            throw new IllegalArgumentException("Doctor ID cannot be less than 5,000");
        }
        return doctors.get(id);
    }

    public List<Doctor> getDoctors() {
        return doctors.values()
                .stream()
                .toList();
    }

    public int getDoctorCount() {
        return doctors.size();
    }

    public boolean assignPatientToDoctor(
            int doctorId,
            int patientId
    ) {
        Doctor doctor = findDoctor(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        Patient patient = findPatient(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }
        return doctor.assignPatient(patient);
    }

    public boolean removePatientFromDoctor(
            int doctorId,
            int patientId
    ) {
        Doctor doctor = findDoctor(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        return doctor.removePatient(patientId);
    }

    public double totalBills() {
        return patients.values()
                .stream()
                .mapToDouble(
                        Patient::calculateBill
                )
                .sum();
    }

    public Map<PatientStatus, Long> countStatus() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getStatus,
                                Collectors.counting()
                        )
                );
    }

    public List<Doctor> sortDoctorsBySalary() {
        return doctors.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                        Doctor::getSalary
                                )
                                .reversed()
                )
                .toList();
    }

    public List<Patient> sortPatientsByBills() {
        return patients.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                        Patient::calculateBill
                                )
                                .reversed()
                )
                .toList();
    }

    public Optional<Doctor> highestPaidDoctor() {
        return doctors.values()
                .stream()
                .max(
                        Comparator.comparing(
                                Doctor::getSalary
                        )
                );
    }

    public Optional<Patient> highestBillPatient() {
        return patients.values()
                .stream()
                .max(
                        Comparator.comparing(
                                Patient::calculateBill
                        )
                );
    }

    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization cannot be null or blank");
        }
        return doctors.values()
                .stream()
                .filter(
                        doctor ->
                                doctor.getSpecialization().equalsIgnoreCase(specialization.trim())
                )
                .toList();
    }

    public Map<Integer, Integer> countPatientsForDoctors() {
        return doctors.values()
                .stream()
                .collect(
                        Collectors.toMap(
                                Doctor::getId,
                                Doctor::getPatientCount
                        )
                );
    }

    public List<Patient> getAdmittedPatients() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.ADMITTED
                )
                .toList();
    }

    public List<Patient> getDischargedPatients() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.DISCHARGED
                )
                .toList();
    }

    public List<Patient> getICUPatients() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.ICU
                )
                .toList();
    }

    public List<Patient> getPatientsStayedMoreThan(int days) {
        if (days <= 0 || days > 365) {
            throw new IllegalArgumentException("Days must be between 1 and 365 days (1 Year)");
        }
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getDaysStayed() > days
                )
                .toList();
    }

    public List<Patient> getPatientsWithBillGreaterThan(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.calculateBill() > amount
                )
                .toList();
    }

    public List<Doctor> getDoctorsWithSalaryGreaterThan(double salary) {
        if (salary < 6000 || salary > 200000) {
            throw new IllegalArgumentException("Doctor salary must be between 6,000 and 200,000");
        }
        return doctors.values()
                .stream()
                .filter(
                        doctor ->
                                doctor.getSalary() > salary
                )
                .toList();
    }

    public List<Doctor> getDoctorsByPatientCount(int minPatients) {
        if (minPatients <= 0) {
            throw new IllegalArgumentException("Minimum Patients must be greater than 0");
        }
        return doctors.values()
                .stream()
                .filter(
                        doctor ->
                                doctor.getPatientCount() > minPatients
                )
                .toList();
    }

    public List<String> patientNames() {
        return patients.values()
                .stream()
                .map(
                        Patient::getName
                )
                .toList();
    }

    public List<String> doctorNames() {
        return doctors.values()
                .stream()
                .map(
                        Doctor::getName
                )
                .toList();
    }

    public List<Double> patientBills() {
        return patients.values()
                .stream()
                .map(
                        Patient::calculateBill
                )
                .toList();
    }

    public List<String> doctorSpecializations() {
        return doctors.values()
                .stream()
                .map(
                        Doctor::getSpecialization
                )
                .toList();
    }

    public Set<String> uniqueSpecializations() {
        return doctors.values()
                .stream()
                .map(
                        Doctor::getSpecialization
                )
                .collect(
                        Collectors.toSet()
                );
    }

    public List<Patient> sortPatientsByAge() {
        return patients.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Patient::getAge
                        )
                )
                .toList();
    }

    public List<Patient> sortPatientsByName() {
        return patients.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Patient::getName
                        )
                )
                .toList();
    }

    public List<Doctor> sortDoctorsByName() {
        return doctors.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Doctor::getName
                        )
                )
                .toList();
    }

    public List<Doctor> sortDoctorsByPatientCount() {
        return doctors.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Doctor::getPatientCount
                        )
                )
                .toList();
    }

    public Optional<Patient> youngestPatient() {
        return patients.values()
                .stream()
                .min(
                        Comparator.comparing(
                                Patient::getAge
                        )
                );
    }

    public Optional<Patient> oldestPatient() {
        return patients.values()
                .stream()
                .max(
                        Comparator.comparing(
                                Patient::getAge
                        )
                );
    }

    public Optional<Doctor> lowestPaidDoctor() {
        return doctors.values()
                .stream()
                .min(
                        Comparator.comparing(
                                Doctor::getSalary
                        )
                );
    }

    public Optional<Doctor> doctorWithMostPatients() {
        return doctors.values()
                .stream()
                .max(
                        Comparator.comparing(
                                Doctor::getPatientCount
                        )
                );
    }

    public boolean anyPatientInICU() {
        return patients.values()
                .stream()
                .anyMatch(
                        patient ->
                                patient.getStatus() == PatientStatus.ICU
                );
    }

    public boolean allPatientsDischarged() {
        return patients.values()
                .stream()
                .allMatch(
                        patient ->
                                patient.getStatus() == PatientStatus.DISCHARGED
                );
    }

    public boolean nonePatientInICU() {
        return patients.values()
                .stream()
                .noneMatch(
                        patient ->
                                patient.getStatus() == PatientStatus.ICU
                );
    }

    public boolean anyDoctorWithoutPatients() {
        return doctors.values()
                .stream()
                .anyMatch(
                        doctor ->
                                doctor.getPatientCount() == 0
                );
    }

    public long countICUPatients() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.ICU
                )
                .count();
    }

    public long countDischargedPatients() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.DISCHARGED
                )
                .count();
    }

    public long countDoctorsWithoutPatients() {
        return doctors.values()
                .stream()
                .filter(
                        doctor ->
                                doctor.getPatientCount() == 0
                )
                .count();
    }

    public double totalDoctorsSalary() {
        return doctors.values()
                .stream()
                .mapToDouble(
                        Doctor::getSalary
                )
                .sum();
    }

    public double totalICUBills() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.ICU
                )
                .mapToDouble(
                        Patient::calculateBill
                )
                .sum();
    }

    public double totalAdmittedBills() {
        return patients.values()
                .stream()
                .filter(
                        patient ->
                                patient.getStatus() == PatientStatus.ADMITTED
                )
                .mapToDouble(
                        Patient::calculateBill
                )
                .sum();
    }

    public double averagePatientBill() {
        return patients.values()
                .stream()
                .mapToDouble(
                        Patient::calculateBill
                )
                .average()
                .orElse(0.0);
    }

    public double averageDoctorSalary() {
        return doctors.values()
                .stream()
                .mapToDouble(
                        Doctor::getSalary
                )
                .average()
                .orElse(0.0);
    }

    public double averageStayDays() {
        return patients.values()
                .stream()
                .mapToDouble(
                        Patient::getDaysStayed
                )
                .average()
                .orElse(0.0);
    }

    public DoubleSummaryStatistics patientBillStatistics() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.summarizingDouble(
                                Patient::calculateBill
                        )
                );
    }

    public DoubleSummaryStatistics salaryStatistics() {
        return doctors.values()
                .stream()
                .collect(
                        Collectors.summarizingDouble(
                                Doctor::getSalary
                        )
                );
    }

    public IntSummaryStatistics stayDaysStatistics() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.summarizingInt(
                                Patient::getDaysStayed
                        )
                );
    }

    public Map<PatientStatus, List<Patient>> groupPatientsByStatus() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getStatus
                        )
                );
    }

    public Map<String, List<Doctor>> groupDoctorsBySpecialization() {
        return doctors.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Doctor::getSpecialization
                        )
                );
    }

    public Map<String, List<Patient>> groupPatientsByIllness() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getIllness
                        )
                );
    }

    public Map<Boolean, List<Patient>> partitionPatientsByICU() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.partitioningBy(
                                patient ->
                                        patient.getStatus() == PatientStatus.ICU
                        )
                );
    }

    public Map<Boolean, List<Doctor>> partitionDoctorsBySalary(double salary) {
        if (salary < 6000 || salary > 200000) {
            throw new IllegalArgumentException("Doctor salary must be between 6,000 and 200,000");
        }
        return doctors.values()
                .stream()
                .collect(
                        Collectors.partitioningBy(
                                doctor ->
                                        doctor.getSalary() >= salary
                        )
                );
    }

    public Map<Integer, String> patientIdNameMap() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.toMap(
                                Patient::getId,
                                Patient::getName
                        )
                );
    }

    public Map<Integer, Double> patientBillsMap() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.toMap(
                                Patient::getId,
                                Patient::calculateBill
                        )
                );
    }

    public List<Patient> topHighestBills(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        return patients.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                        Patient::calculateBill
                                )
                                .reversed()
                )
                .limit(n)
                .toList();
    }

    public List<Doctor> topHighestSalary(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        return doctors.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                        Doctor::getSalary
                                )
                                .reversed()
                )
                .limit(n)
                .toList();
    }

    public List<Patient> topLongestStay(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        return patients.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                        Patient::getDaysStayed
                                )
                                .reversed()
                )
                .limit(n)
                .toList();
    }

    public Map<PatientStatus, List<String>> patientNamesByStatus() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getStatus,
                                Collectors.mapping(
                                        Patient::getName,
                                        Collectors.toList()
                                )
                        )
                );
    }

    public Set<Patient> allAssignedPatients() {
        return doctors.values()
                .stream()
                .map(
                        Doctor::getPatients
                )
                .flatMap(
                        p ->
                                p.values().stream()
                )
                .collect(
                        Collectors.toSet()
                );
    }

    public String allDoctorNames() {
        return doctors.values()
                .stream()
                .map(
                        Doctor::getName
                )
                .collect(
                        Collectors.joining(", ")
                );
    }

    public String allPatientNames() {
        return patients.values()
                .stream()
                .map(
                        Patient::getName
                )
                .collect(
                        Collectors.joining(", ")
                );
    }

    public Map<String, Double> averageBillPerIllness() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getIllness,
                                Collectors.averagingDouble(
                                        Patient::calculateBill
                                )
                        )
                );
    }

    public Map<String, Long> patientCountPerIllness() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getIllness,
                                Collectors.counting()
                        )
                );
    }

    public Map<String, Double> averageSalaryPerSpecialization() {
        return doctors.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Doctor::getSpecialization,
                                Collectors.averagingDouble(
                                        Doctor::getSalary
                                )
                        )
                );
    }

    public Map<PatientStatus, Double> totalBillsPerStatus() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getStatus,
                                Collectors.summingDouble(
                                        Patient::calculateBill
                                )
                        )
                );
    }

    public Map<String, DoubleSummaryStatistics> illnessStatistics() {
        return patients.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Patient::getIllness,
                                Collectors.summarizingDouble(
                                        Patient::calculateBill
                                )
                        )
                );
    }
}