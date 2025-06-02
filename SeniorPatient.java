import java.time.LocalDate;

/**
 * SeniorPatient class - Represents a senior patient (75+ years old)
 * Extends the Patient abstract class and implements the getPatientType method
 * Senior patients have higher priority than regular patients but lower than emergency patients
 */
public class SeniorPatient extends Patient {

    /**
     * Constructor for SeniorPatient
     * Calls the parent Patient constructor to initialize common fields
     *
     * @param name Patient's full name
     * @param age Patient's age in years (should be 75+)
     * @param birthday Patient's date of birth
     */
    public SeniorPatient(String name, int age, LocalDate birthday) {
        super(name, age, birthday);
    }

    /**
     * Constructor for SeniorPatient with notes
     * Calls the parent Patient constructor to initialize common fields
     *
     * @param name Patient's full name
     * @param age Patient's age in years (should be 75+)
     * @param birthday Patient's date of birth
     * @param notes Optional notes/comments about the patient
     */
    public SeniorPatient(String name, int age, LocalDate birthday, String notes) {
        super(name, age, birthday, notes);
    }

    /**
     * Implementation of abstract method from Patient class
     * Returns the type of this patient
     *
     * @return "Senior" to identify this as a senior patient
     */
    @Override
    public String getPatientType() {
        return "Senior";
    }
}