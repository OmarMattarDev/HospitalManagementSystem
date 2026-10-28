import java.util.*;
public class Doctor extends Person{

    private String specialization;

    private double salary;

    private final Map<Integer,Patient> patients;

    public Doctor(
             int id
            ,String name
            ,int age
            ,String specialization
            ,double salary
    )
    {
        super(id, name, age);
        setSpecialization(specialization);
        setSalary(salary);
        patients=new HashMap<>();
    }

    @Override
    protected void setId(int id) {
        if(id<5000){
            throw new IllegalArgumentException("Doctor ID cannot be less than 5,000");
        }
        super.setId(id);
    }

    @Override
    protected void setAge(int age) {
        if(age<23||age>75){
            throw new IllegalArgumentException("Doctor age must be between 23 and 75");
        }
        super.setAge(age);
    }

    public String getSpecialization() {
        return specialization;
    }

    public double getSalary() {
        return salary;
    }

    public Map<Integer,Patient> getPatients() {
        return new HashMap<>(patients);
    }

    protected void setSpecialization(String specialization) {
        if(specialization==null||specialization.isBlank()){
            throw new IllegalArgumentException("Specialization cannot be null or blank");
        }
        String trimmedUpperSpecialization=specialization.trim().toUpperCase(Locale.ROOT);
        if(trimmedUpperSpecialization.length()<3){
            throw new IllegalArgumentException("Specialization must be at least 3 characters");
        }
        this.specialization = trimmedUpperSpecialization;
    }

    protected void setSalary(double salary) {
        if(salary<6000||salary>200000){
            throw new IllegalArgumentException("Doctor salary must be between 6,000 and 200,000");
        }
        this.salary = salary;
    }

    public void increaseSalary(double amount){
        if(amount<=0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        setSalary(salary+amount);
    }

    public boolean assignPatient(Patient patient){
        if(patient==null){
            throw new IllegalArgumentException("Patient cannot be null");
        }
        return patients.putIfAbsent(patient.getId(),patient)==null;
    }

    public boolean removePatient(int patientId){
        if(patientId<1000||patientId>4999){
            throw new IllegalArgumentException("Patient ID must be between 1,000 and 4,999");
        }
        return patients.remove(patientId)!=null;
    }

    public boolean hasPatient(int patientId){
        if(patientId<1000||patientId>4999){
            throw new IllegalArgumentException("Patient ID must be between 1,000 and 4,999");
        }
        return patients.containsKey(patientId);
    }

    public Patient findPatient(int patientId){
        if(patientId<1000||patientId>4999){
            throw new IllegalArgumentException("Patient ID must be between 1,000 and 4,999");
        }
        return patients.get(patientId);
    }

    public int getPatientCount(){
        return patients.size();
    }

    @Override
    public String toString() {
        String  patientsID= patients.isEmpty()
                ? "None"
                : patients.keySet().toString();
        return new StringBuilder(super.toString())
                .append("Specialization : ").append(specialization)
                .append("\nSalary : ").append(salary)
                .append("\nPatients ID : ").append(patientsID)
                .append("\n")
                .toString();
    }
}
