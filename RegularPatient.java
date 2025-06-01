import java.time.LocalDate;

/**
 * RegularPatient class - Represents a non-emergency patient
 * Extends the Patient abstract class and implements the getPatientType method
 * Regular patients have lower priority than emergency patients
 */
public class RegularPatient extends Patient {

    /**
     * Constructor for RegularPatient
     * Calls the parent Patient constructor to initialize common fields
     *
     * @param name Patient's full name
     * @param age Patient's age in years
     * @param birthday Patient's date of birth
     */
    public RegularPatient(String name, int age, LocalDate birthday) {
        super(name, age, birthday);
    }

    /**
     * Implementation of abstract method from Patient class
     * Returns the type of this patient
     *
     * @return "Regular" to identify this as a regular patient
     */
    @Override
    public String getPatientType() {
        return "Regular";
    }
}