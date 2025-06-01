import java.time.LocalDate;

/**
 * Abstract Patient class - Base class for all patient types
 * Contains common patient information and defines abstract methods
 * that must be implemented by subclasses
 */
public abstract class Patient {
    // Private fields to store patient information
    private String name;
    private int age;
    private LocalDate birthday;

    /**
     * Constructor for Patient
     * Initializes patient with name, age, and birthday
     *
     * @param name Patient's full name
     * @param age Patient's age in years
     * @param birthday Patient's date of birth
     */
    public Patient(String name, int age, LocalDate birthday) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    /**
     * Getter method for patient's name
     *
     * @return Patient's name as a String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for patient's age
     *
     * @return Patient's age as an integer
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Getter method for patient's birthday
     *
     * @return Patient's birthday as LocalDate
     */
    public LocalDate getBirthday() {
        return this.birthday;
    }

    /**
     * Abstract method to get patient type
     * Must be implemented by all subclasses to identify patient type
     *
     * @return String representing the type of patient
     */
    public abstract String getPatientType();
}

