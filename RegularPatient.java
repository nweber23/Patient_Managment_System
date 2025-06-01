import java.time.LocalDate;

public class RegularPatient extends Patient {
    public RegularPatient(String name, int age, LocalDate birthday) {
        super(name, age, birthday);
    }

    @Override
    public String getPatientType() {
        return "Regular";
    }
}
