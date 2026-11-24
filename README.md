# Hospital Management System

A Java-based Hospital Management System built using **Core Java, JDBC, and MySQL**. The project manages patients, doctors, departments, hospital admissions, diagnoses, and doctor assignments through a structured layered architecture.

The system focuses on applying object-oriented programming principles, database connectivity, transaction management, and clean separation of responsibilities.

## Features

### Patient Management
- Register new patients.
- Retrieve patient information.
- Update patient details.
- Delete patient records.
- Filter and sort patients.
- Track patient blood types and emergency contacts.

### Doctor Management
- Register and manage doctors.
- Assign doctors to hospital departments.
- Update doctor information and consultation fees.
- Activate or deactivate doctors.
- Filter doctors by specialization and department.

### Department Management
- Create and manage hospital departments.
- Update department information.
- Retrieve department details.
- View doctors associated with each department.

### Admission Management
- Admit patients to the hospital.
- Track admission and discharge dates.
- Manage admission statuses:
    - `ADMITTED`
    - `ICU`
    - `DISCHARGED`
- Calculate patient stay duration.
- Estimate admission costs based on daily costs and stay duration.

### Diagnosis Management
- Add diagnoses to patient admissions.
- Retrieve and update diagnosis information.
- Delete diagnosis records.

### Doctor Assignment Management
- Assign doctors to patient admissions.
- Release doctors from active assignments.
- Preserve doctor assignment history.
- Automatically release active doctor assignments when a patient is discharged.

### Reports and Statistics
- Patient statistics by gender and blood type.
- Doctor statistics by specialization and active status.
- Department statistics and doctor distribution.
- Admission statistics, average stay duration, and estimated costs.
- Combined hospital summary report.

## Technologies Used

- Java
- JDBC (Java Database Connectivity)
- MySQL
- MySQL Connector/J
- Object-Oriented Programming (OOP)
- Java Collections Framework
- Lambda Expressions
- Stream API
- IntelliJ IDEA
- Git & GitHub

## Project Architecture

The project follows a layered architecture to separate database operations, business logic, and application workflows.

```text
src/
├── com/
│   └── hospital/
│       ├── application/
│       │   └── Main.java
│       │
│       ├── config/
│       │   └── DatabaseConnection.java
│       │
│       ├── model/
│       │   ├── enums/
│       │   ├── Person.java
│       │   ├── Patient.java
│       │   ├── Doctor.java
│       │   ├── Department.java
│       │   ├── Admission.java
│       │   ├── Diagnosis.java
│       │   └── DoctorAssignment.java
│       │
│       ├── dao/
│       ├── service/
│       ├── workflow/
│       └── report/
│
└── config/
    └── database.properties.example
```

### Architecture Layers

| Layer | Responsibility |
|---|---|
| Config | Database connection configuration. |
| Model | Represents system entities and enums. |
| DAO | Handles database operations using JDBC. |
| Service | Contains business logic and validation. |
| Workflow | Coordinates multi-step business operations and transactions. |
| Report | Generates statistics and analytical reports. |
| Application | Contains the main entry point and application tests. |

## Database Design

The project uses a MySQL database named `HospitalManagementDB`.

It contains seven main tables:

| Table | Description |
|---|---|
| `persons` | Stores shared personal information. |
| `patients` | Stores patient-specific information. |
| `departments` | Stores hospital departments. |
| `doctors` | Stores doctor-specific information. |
| `admissions` | Stores patient admission records. |
| `diagnoses` | Stores diagnoses associated with admissions. |
| `doctor_assignments` | Tracks doctor assignments and their history. |

### Database Design Highlights

- Uses primary keys and foreign key constraints.
- Applies a normalized relational database structure.
- Uses inheritance in the Java model through an abstract `Person` class.
- Supports multiple admissions for the same patient.
- Preserves doctor assignment history.
- Uses appropriate constraints to protect referenced records.

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/OmarMattarDev/HospitalManagementSystem.git
```

Open the project in IntelliJ IDEA.

### 2. Create the MySQL Database

Open MySQL Workbench or another MySQL client.

Execute the SQL scripts in the following order:

1. `HospitalManagementDB/database_setup.sql`
2. `HospitalManagementDB/tables.sql`
3. `HospitalManagementDB/indexes.sql`

The `basic_queries.sql` file contains sample queries that can be executed after creating the database and its tables.

### 3. Configure Database Credentials

Inside `src/config/`, create a copy of:

`database.properties.example`

Name the copy:

`database.properties`

Update the configuration with your own MySQL credentials.

**Important:** Never commit your actual `database.properties` file or expose database passwords on GitHub.

### 4. Add MySQL Connector/J

Download MySQL Connector/J from the official MySQL website:

https://dev.mysql.com/downloads/connector/j/

Add the downloaded JAR file to the project's dependencies in IntelliJ IDEA.

The project uses manually configured JDBC dependencies and does not require Maven.

### 5. Run the Application

Open:

`src/com/hospital/application/Main.java`

Run the `Main` class to execute the application tests and demonstrate the implemented features.

## Project Structure

```text
HospitalManagementSystem/
│
├── HospitalManagementDB/
│   ├── database_setup.sql
│   ├── tables.sql
│   ├── indexes.sql
│   └── basic_queries.sql
│
├── src/
│   ├── com/hospital/
│   │   ├── application/
│   │   ├── config/
│   │   ├── dao/
│   │   ├── model/
│   │   ├── report/
│   │   ├── service/
│   │   └── workflow/
│   │
│   └── config/
│       └── database.properties.example
│
├── .gitignore
└── README.md
```

## Key Concepts Applied

- Object-Oriented Programming and inheritance.
- Abstract classes and enums.
- Encapsulation and model design.
- JDBC database connectivity.
- PreparedStatement and ResultSet.
- CRUD operations.
- DAO Pattern.
- Layered Architecture.
- Transaction Management (Commit and Rollback).
- Exception Handling and Try-with-Resources.
- Java Collections and Stream API.
- Relational Database Design and SQL.

## Author

**Omar Mattar**

Java Backend Development Learner

GitHub: https://github.com/OmarMattarDev

---

*This project was developed as a practical application of Java backend development concepts, combining object-oriented programming with JDBC and relational database management.*