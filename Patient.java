import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Patient {
    private String name;
    private int age;
    private LocalDate birthday;
    private PatientType type;
    private StringBuilder notesHistory;
    private LocalDateTime arrivalTime;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Patient(String name, int age, LocalDate birthday, PatientType type) {
        this(name, age, birthday, type, "");
    }

    public Patient(String name, int age, LocalDate birthday, PatientType type, String notes) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.type = type;
        this.notesHistory = new StringBuilder();
        this.arrivalTime = LocalDateTime.now();
        
        if (notes != null && !notes.trim().isEmpty()) {
            addNoteWithTimestamp(notes);
        }
    }

    // Static factory methods for convenience
    public static Patient createEmergency(String name, int age, LocalDate birthday, String notes) {
        return new Patient(name, age, birthday, PatientType.EMERGENCY, notes);
    }

    public static Patient createSenior(String name, int age, LocalDate birthday, String notes) {
        return new Patient(name, age, birthday, PatientType.SENIOR, notes);
    }

    public static Patient createRegular(String name, int age, LocalDate birthday, String notes) {
        return new Patient(name, age, birthday, PatientType.REGULAR, notes);
    }

    public static Patient createByType(String name, int age, LocalDate birthday, String notes, boolean isEmergency) {
        if (isEmergency) {
            return createEmergency(name, age, birthday, notes);
        } else if (age >= 75) {
            return createSenior(name, age, birthday, notes);
        } else {
            return createRegular(name, age, birthday, notes);
        }
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public LocalDate getBirthday() { return birthday; }
    public PatientType getType() { return type; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public String getPatientType() { return type.name().toLowerCase(); }
    public String getTypeIcon() { return type.getIcon(); }
    public int getPriorityLevel() { return type.getPriority(); }

    // Notes methods
    public String getNotes() { return notesHistory.toString(); }
    
    public String getLatestNote() {
        String allNotes = notesHistory.toString();
        if (allNotes.isEmpty()) return "";
        
        String[] lines = allNotes.split("\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            if (!lines[i].trim().isEmpty()) {
                String line = lines[i].trim();
                int colonIndex = line.indexOf(": ");
                return colonIndex > 0 ? line.substring(colonIndex + 2) : line;
            }
        }
        return "";
    }

    public void addNote(String note) {
        if (note != null && !note.trim().isEmpty()) {
            addNoteWithTimestamp(note.trim());
        }
    }

    private void addNoteWithTimestamp(String note) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(TIMESTAMP_FORMATTER);
        
        if (notesHistory.length() > 0) {
            notesHistory.append("\n");
        }
        notesHistory.append("[").append(timestamp).append("]: ").append(note);
    }

    public void setNotes(String notes) { addNote(notes); }
    public boolean hasNotes() { return notesHistory.length() > 0; }
    public String getFormattedNotesHistory() {
        return notesHistory.length() == 0 ? "No notes recorded." : notesHistory.toString();
    }

    // Legacy compatibility methods
    public boolean isSeniorByAge() { return age >= 75; }
    public boolean isSenior() { return type == PatientType.SENIOR; }

    // Type changing
    public void changeType(PatientType newType) {
        PatientType oldType = this.type;
        this.type = newType;
        addNote("Patient type changed from " + oldType.name() + " to " + newType.name());
    }
    
    // Setter for name
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    // Setter for age
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;
        }
    }

    // Setter for birthday
    public void setBirthday(LocalDate birthday) {
        if (birthday != null && !birthday.isAfter(LocalDate.now()) && 
            !birthday.isBefore(LocalDate.now().minusYears(150))) {
            this.birthday = birthday;
        }
    }
}