import java.time.LocalDate;
public abstract class Patient {
    private String name;
    private int age;
    private LocalDate birthday;

    public Patient(String name, int age, LocalDate birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public String getName() {
        return this.name;
    }

    public abstract String getPatientType();
}

