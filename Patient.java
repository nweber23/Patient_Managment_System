import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Patient class - Base class for all patient types
 * Contains common patient information and defines abstract methods
 * that must be implemented by subclasses
 * Enhanced with note history functionality and flexible senior classification
 */
public abstract class Patient {
    // Private fields to store patient information
    private String name;
    private int age;
    private LocalDate birthday;
    private StringBuilder notesHistory; // Changed to StringBuilder for note history
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        this.notesHistory = new StringBuilder(); // Initialize empty notes history
    }

    /**
     * Constructor for Patient with notes
     * Initializes patient with name, age, birthday, and initial notes
     *
     * @param name Patient's full name
     * @param age Patient's age in years
     * @param birthday Patient's date of birth
     * @param notes Initial notes/comments about the patient
     */
    public Patient(String name, int age, LocalDate birthday, String notes) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.notesHistory = new StringBuilder();
        if (notes != null && !notes.trim().isEmpty()) {
            addNoteWithTimestamp(notes);
        }
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
     * Getter method for patient's notes (all history)
     *
     * @return Patient's complete notes history as a String
     */
    public String getNotes() {
        return this.notesHistory.toString();
    }

    /**
     * Getter method for the most recent note only
     *
     * @return Most recent note or empty string if no notes
     */
    public String getLatestNote() {
        String allNotes = this.notesHistory.toString();
        if (allNotes.isEmpty()) {
            return "";
        }
        
        // Split by line breaks and get the last non-empty line
        String[] lines = allNotes.split("\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            if (!lines[i].trim().isEmpty()) {
                // Remove timestamp for display
                String line = lines[i].trim();
                int colonIndex = line.indexOf(": ");
                if (colonIndex > 0 && colonIndex < line.length() - 2) {
                    return line.substring(colonIndex + 2);
                }
                return line;
            }
        }
        return "";
    }

    /**
     * Adds a new note with timestamp (doesn't overwrite existing notes)
     *
     * @param note New note to add
     */
    public void addNote(String note) {
        if (note != null && !note.trim().isEmpty()) {
            addNoteWithTimestamp(note.trim());
        }
    }

    /**
     * Private helper method to add note with timestamp
     *
     * @param note Note to add with timestamp
     */
    private void addNoteWithTimestamp(String note) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(TIMESTAMP_FORMATTER);
        
        if (notesHistory.length() > 0) {
            notesHistory.append("\n");
        }
        notesHistory.append("[").append(timestamp).append("]: ").append(note);
    }

    /**
     * Legacy setter method for compatibility - now adds note instead of replacing
     *
     * @param notes Notes/comments about the patient
     */
    public void setNotes(String notes) {
        addNote(notes);
    }

    /**
     * Check if patient has any notes
     *
     * @return true if patient has notes, false otherwise
     */
    public boolean hasNotes() {
        return notesHistory.length() > 0;
    }

    /**
     * Get formatted notes history for display
     *
     * @return Formatted notes history
     */
    public String getFormattedNotesHistory() {
        if (notesHistory.length() == 0) {
            return "No notes recorded.";
        }
        return notesHistory.toString();
    }

    /**
     * Check if patient is naturally a senior by age (75+ years old)
     * This is different from the patient type - a patient can be designated
     * as Senior type even if they are under 75 through manual classification
     *
     * @return true if patient is 75 or older, false otherwise
     */
    public boolean isSeniorByAge() {
        return this.age >= 75;
    }

    /**
     * Check if patient is considered a senior (either by age OR by manual classification)
     * This method now checks the patient type instead of just age
     *
     * @return true if patient is classified as Senior type, false otherwise
     */
    public boolean isSenior() {
        return getPatientType().equals("Senior");
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
        } else if (getPatientType().equals("Senior")) {
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
            return "[EMERGENCY]";
        } else if (getPatientType().equals("Senior")) {
            return "[SENIOR]";
        } else {
            return "[REGULAR]";
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