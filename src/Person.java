public abstract class Person {

    private int id;

    private String name;

    private int age;

    public Person(
            int id,
            String name,
            int age
    )
    {
        setId(id);
        setName(name);
        setAge(age);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    protected void setId(int id) {
        if(id<=0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        this.id = id;
    }

    protected void setName(String name) {
        if(name==null||name.isBlank()){
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        String trimmedName=name.trim();
        if(trimmedName.length()<3){
            throw new IllegalArgumentException("Name must be at least 3 characters");
        }
        this.name = trimmedName;
    }

    protected void setAge(int age) {
        if(age<0||age>120){
            throw new IllegalArgumentException("Age must be between 0 and 120");
        }
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("\nId : ").append(id)
                .append("\nName : ").append(name)
                .append("\nAge : ").append(age)
                .append("\n")
                .toString();
    }
}
