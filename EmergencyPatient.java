import java.time.LocalDate;

public class EmergencyPatient extends Patient {
    public EmergencyPatient(String name, int age, LocalDate birthday) {
        super(name, age, birthday);
    }

    @Override
    public String getPatientType() {
        return "Emergency";
    }
}
