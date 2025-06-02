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
    private String notes; // New field for optional notes/comments

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
        this.notes = ""; // Default empty notes
    }

    /**
     * Constructor for Patient with notes
     * Initializes patient with name, age, birthday, and notes
     *
     * @param name Patient's full name
     * @param age Patient's age in years
     * @param birthday Patient's date of birth
     * @param notes Optional notes/comments about the patient
     */
    public Patient(String name, int age, LocalDate birthday, String notes) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.notes = notes != null ? notes : "";
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
     * Getter method for patient's notes
     *
     * @return Patient's notes as a String
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Setter method for patient's notes
     *
     * @param notes Notes/comments about the patient
     */
    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }

    /**
     * Check if patient is a senior (75+ years old)
     *
     * @return true if patient is 75 or older, false otherwise
     */
    public boolean isSenior() {
        return this.age >= 75;
    }

    /**
     * Get patient priority level for queue ordering
     * Emergency = 1 (highest), Senior = 2, Regular = 3 (lowest)
     *
     * @return Priority level as integer
     */
    public int getPriorityLevel() {
        if (getPatientType().equals("Emergency")) {
            return 1;
        } else if (isSenior()) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * Get display icon for patient type
     *
     * @return Icon string for the patient type
     */
    public String getTypeIcon() {
        if (getPatientType().equals("Emergency")) {
            return "🚨 [EMERGENCY]";
        } else if (isSenior()) {
            return "👴 [SENIOR]";
        } else {
            return "👥 [REGULAR]";
        }
    }

    /**
     * Abstract method to get patient type
     * Must be implemented by all subclasses to identify patient type
     *
     * @return String representing the type of patient
     */
    public abstract String getPatientType();
}