import java.time.LocalDate;

/**
 * EmergencyPatient class - Represents an emergency patient
 * Extends the Patient abstract class and implements the getPatientType method
 * Emergency patients have higher priority than regular patients
 */
public class EmergencyPatient extends Patient {

    /**
     * Constructor for EmergencyPatient
     * Calls the parent Patient constructor to initialize common fields
     *
     * @param name Patient's full name
     * @param age Patient's age in years
     * @param birthday Patient's date of birth
     */
    public EmergencyPatient(String name, int age, LocalDate birthday) {
        super(name, age, birthday);
    }

    /**
     * Implementation of abstract method from Patient class
     * Returns the type of this patient
     *
     * @return "Emergency" to identify this as an emergency patient
     */
    @Override
    public String getPatientType() {
        return "Emergency";
    }
}