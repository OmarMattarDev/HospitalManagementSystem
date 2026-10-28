import java.util.Locale;

public class Patient extends Person implements Billable{

    private String illness;

    private int daysStayed;

    private double dailyCost;

    private PatientStatus status;

    public Patient(
             int id
            ,String name
            ,int age
            ,String illness
            ,int daysStayed
            ,double dailyCost
            ,PatientStatus status
    )
    {
        super(id, name, age);
        setIllness(illness);
        setDaysStayed(daysStayed);
        setDailyCost(dailyCost);
        setStatus(status);
    }

    public String getIllness() {
        return illness;
    }

    public int getDaysStayed() {
        return daysStayed;
    }

    public double getDailyCost() {
        return dailyCost;
    }

    public PatientStatus getStatus() {
        return status;
    }

    @Override
    protected void setId(int id) {
        if(id<1000||id>4999){
            throw new IllegalArgumentException("Patient ID must be between 1,000 and 4,999");
        }
        super.setId(id);
    }

    @Override
    protected void setAge(int age) {
        if(age<0||age>120){
            throw new IllegalArgumentException("Patient age must be between 0 and 120");
        }
        super.setAge(age);
    }

    protected void setIllness(String illness) {
        if(illness==null||illness.isBlank()){
            throw new IllegalArgumentException("Illness cannot be null or blank");
        }
        String trimmedUpperIllness=illness.trim().toUpperCase(Locale.ROOT);
        if(trimmedUpperIllness.length()<3){
            throw new IllegalArgumentException("Illness must be at least 3 characters");
        }
        this.illness = trimmedUpperIllness;
    }

    protected void setDaysStayed(int daysStayed) {
        if(daysStayed<=0||daysStayed>365){
            throw new IllegalArgumentException("Days must be between 1 and 365 days (1 Year)");
        }
        this.daysStayed = daysStayed;
    }

    protected void setDailyCost(double dailyCost) {
        if(dailyCost<0||dailyCost>100000){
            throw new IllegalArgumentException("Daily cost must be between 0 and 100,000");
        }
        this.dailyCost = dailyCost;
    }

    protected void setStatus(PatientStatus status) {
        if(status==null){
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    @Override
    public double calculateBill() {
        return daysStayed*dailyCost;
    }

    public void admitToICU() {
        if(status==PatientStatus.ICU){
            throw new IllegalStateException("Patient is already admitted to ICU");
        }
        setStatus(PatientStatus.ICU);
    }

    public void discharge() {
        if(status==PatientStatus.DISCHARGED){
            throw new IllegalStateException("Patient is already discharged");
        }
        setStatus(PatientStatus.DISCHARGED);
    }

    public void admit() {
        if(status==PatientStatus.ADMITTED){
            throw new IllegalStateException("Patient is already admitted");
        }
        setStatus(PatientStatus.ADMITTED);
    }

    public void increaseStayDays(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be greater than 0");
        }
        if(isDischarged()){
            throw new IllegalStateException("Patient is already discharged");
        }

        setDaysStayed(daysStayed+days);
    }

    public void changeDailyCost(double newCost) {
        if(isDischarged()){
            throw new IllegalStateException("Patient is already discharged");
        }
        setDailyCost(newCost);
    }

    public void updateIllness(String illness) {
        if(isDischarged()){
            throw new IllegalStateException("Patient is already discharged");
        }
        setIllness(illness);
    }

    public boolean isInICU() {
        return this.status == PatientStatus.ICU;
    }

    public boolean isDischarged() {
        return this.status == PatientStatus.DISCHARGED;
    }

    public boolean isAdmitted() {
        return this.status == PatientStatus.ADMITTED;
    }

    public boolean hasStayedMoreThan(int days) {
        if(days<=0||days>365){
            throw new IllegalArgumentException("Days must be between 1 and 365 days (1 Year)");
        }
        return daysStayed > days;
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append("Illness : ").append(illness)
                .append("\nDays Stayed : ").append(daysStayed)
                .append("\nDaily Cost : ").append(dailyCost)
                .append("\nStatus : ").append(status)
                .append("\n")
                .toString();
    }
}
