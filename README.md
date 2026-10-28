# 🏥 Hospital Management System

A Java-based Hospital Management System built to practice and apply Object-Oriented Programming (OOP), Java Collections, Lambda Expressions, and the Stream API through a practical application.

The project allows managing patients and doctors, handling patient statuses and doctor assignments, and analyzing hospital data using Java Streams and Collectors.

## 🚀 Features

- **Patient Management:** Add, search, update, and remove patients.
- **Doctor Management:** Add, search, update, and remove doctors.
- **Patient Status Management:** Manage patient statuses (`ADMITTED`, `DISCHARGED`, `ICU`).
- **Doctor–Patient Assignment:** Assign patients to doctors and remove patients from doctors.
- **Patient Care Operations:** Admit patients to ICU, discharge patients, update illnesses, manage stay duration, and change daily costs.
- **Doctor Information:** Manage doctor specializations and increase salaries.
- **Search and Filtering:** Find patients and doctors by ID and filter them using different criteria.
- **Sorting:** Sort patients by bill, age, and name, and doctors by salary, name, and patient count.
- **Top, Minimum, and Maximum Operations:** Find patients with the highest bills or longest stays, and doctors with the highest salaries or most assigned patients.
- **Statistics and Aggregation:** Calculate total bills, salaries, average values, and summary statistics.
- **Grouping and Counting:** Group patients by status or illness and doctors by specialization.
- **Partitioning:** Partition patients by ICU status and doctors by salary criteria.
- **Matching Operations:** Use `anyMatch()`, `allMatch()`, and `noneMatch()` to evaluate hospital data.
- **Data Mapping and Joining:** Create maps from patient IDs to names or bills, and process patient and doctor names.
- **Advanced Stream Processing:** Apply `Optional`, `flatMap`, and different Collector operations to analyze hospital data.

## 🛠️ Technologies Used

- Java
- IntelliJ IDEA
- Git
- GitHub

## 🧠 Concepts Practiced

### Object-Oriented Programming (OOP)

The project applies the following OOP concepts:

- Classes and Objects
- Encapsulation
- Inheritance
- Abstraction
- Interface Implementation (`Billable`)
- Constructor Chaining
- Method Overriding
- Polymorphism

### Enum

The `PatientStatus` enum represents the possible statuses of a patient:

```java
public enum PatientStatus {
    ADMITTED,
    DISCHARGED,
    ICU
}
```

### Java Collections

The project uses Java Collections to manage and organize hospital data:

- `List` for working with collections of patients and doctors.
- `Map` for storing patients and doctors by ID and tracking assigned patients.
- `HashMap` for ID-based data management and doctor–patient assignments.

### Lambda Expressions and Method References

Lambda expressions and method references are used for operations such as:

- Filtering
- Sorting
- Matching
- Finding elements
- Processing collections

### Comparator

`Comparator` is used to define different sorting criteria, including:

- Patients by bill
- Patients by age
- Patients by name
- Doctors by salary
- Doctors by name
- Doctors by patient count

### Stream API

The project uses the Stream API for:

- Filtering and mapping
- Sorting and finding elements
- Matching elements
- Collecting results
- Grouping and partitioning
- Joining strings
- Calculating averages and totals
- Generating summary statistics
- Processing nested collections using `flatMap`

### Collectors

The project practices several Collector operations:

- `toList()`
- `toSet()`
- `toMap()`
- `joining()`
- `groupingBy()`
- `partitioningBy()`
- `mapping()`
- `counting()`
- `averagingDouble()`
- `summingDouble()`
- `summarizingDouble()`
- `summarizingInt()`

### Optional

`Optional` is used when searching for elements that may not exist, including:

- Patient with the highest bill
- Youngest and oldest patients
- Highest-paid and lowest-paid doctors
- Doctor with the most assigned patients

### Summary Statistics

The project uses:

- `IntSummaryStatistics`
- `DoubleSummaryStatistics`

These classes help calculate summary information for patient stay durations, bills, and doctor salaries.

## 📂 Project Structure

```text
HospitalManagementSystem/
├── src/
│   ├── Billable.java
│   ├── Doctor.java
│   ├── Hospital.java
│   ├── Main.java
│   ├── Patient.java
│   ├── PatientStatus.java
│   └── Person.java
├── .gitignore
└── README.md
```

## ▶️ How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Make sure a compatible JDK is installed and configured.
4. Run `Main.java`.

## 📌 Main Operations

The `Hospital` class provides operations for managing and analyzing patients and doctors.

### Management

- `addPatient()`
- `removePatient()`
- `findPatient()`
- `addDoctor()`
- `removeDoctor()`
- `findDoctor()`
- `assignPatientToDoctor()`
- `removePatientFromDoctor()`

### Patient Operations

- `getPatients()`
- `getPatientCount()`
- `getAdmittedPatients()`
- `getDischargedPatients()`
- `getICUPatients()`
- `getPatientsStayedMoreThan()`
- `getPatientsWithBillGreaterThan()`
- `patientNames()`
- `patientBills()`

### Doctor Operations

- `getDoctors()`
- `getDoctorCount()`
- `getDoctorsWithSalaryGreaterThan()`
- `getDoctorsByPatientCount()`
- `findDoctorsBySpecialization()`
- `doctorNames()`
- `doctorSpecializations()`
- `uniqueSpecializations()`

### Sorting

- `sortPatientsByBills()`
- `sortPatientsByAge()`
- `sortPatientsByName()`
- `sortDoctorsBySalary()`
- `sortDoctorsByName()`
- `sortDoctorsByPatientCount()`

### Top, Minimum, and Maximum Operations

- `topHighestBills()`
- `topLongestStay()`
- `topHighestSalary()`
- `highestBillPatient()`
- `youngestPatient()`
- `oldestPatient()`
- `highestPaidDoctor()`
- `lowestPaidDoctor()`
- `doctorWithMostPatients()`

### Statistics

- `totalBills()`
- `totalDoctorsSalary()`
- `totalICUBills()`
- `totalAdmittedBills()`
- `averagePatientBill()`
- `averageDoctorSalary()`
- `averageStayDays()`
- `patientBillStatistics()`
- `salaryStatistics()`
- `stayDaysStatistics()`

### Grouping and Aggregation

- `groupPatientsByStatus()`
- `groupPatientsByIllness()`
- `groupDoctorsBySpecialization()`
- `patientNamesByStatus()`
- `countStatus()`
- `patientCountPerIllness()`
- `countPatientsForDoctors()`
- `countICUPatients()`
- `countDischargedPatients()`
- `countDoctorsWithoutPatients()`
- `averageBillPerIllness()`
- `averageSalaryPerSpecialization()`
- `totalBillsPerStatus()`
- `illnessStatistics()`

### Partitioning

- `partitionPatientsByICU()`
- `partitionDoctorsBySalary()`

### Matching and Finding

- `anyPatientInICU()`
- `allPatientsDischarged()`
- `nonePatientInICU()`
- `anyDoctorWithoutPatients()`

### Mapping and Joining

- `patientIdNameMap()`
- `patientBillsMap()`
- `allDoctorNames()`
- `allPatientNames()`
- `allAssignedPatients()`

## 🎯 Project Goal

The main goal of this project is to reinforce Core Java concepts through a practical application.

It focuses on applying OOP, interfaces, polymorphism, Java Collections, Lambda Expressions, Method References, Comparators, `Optional`, the Stream API, and Collectors to manage and analyze hospital data.

## 🔮 Future Improvements

Possible future enhancements include:

- Adding hospital entities such as `Department`, `Room`, `Appointment`, and `MedicalRecord`.
- Integrating a database using JDBC or Spring Data JPA.
- Implementing persistent data storage.
- Adding a graphical user interface using JavaFX.
- Implementing user authentication and role-based access control.
- Expanding billing features and adding insurance support.
- Developing a backend using Spring Boot.

## 👨‍💻 Author

**Omar Mattar**

Java Developer in Progress
