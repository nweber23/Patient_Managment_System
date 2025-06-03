import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class PatientEditor {
    private PatientManagement patientManagement;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public PatientEditor(PatientManagement patientManagement, Scanner scanner) {
        this.patientManagement = patientManagement;
        this.scanner = scanner;
    }
    
    public void editPatientInformation() {
        if (patientManagement.getTotalPatientCount() == 0) {
            System.out.println(Colors.RED + "No patients in queue." + Colors.RESET);
            return;
        }
        
        patientManagement.printPatients();
        String name = getInput("Enter patient name to edit: ");
        
        Patient patient = patientManagement.findPatientByExactName(name);
        if (patient == null) {
            System.out.println(Colors.RED + "Patient not found." + Colors.RESET);
            return;
        }
        
        showEditMenu(patient);
    }
    
    private void showEditMenu(Patient patient) {
        while (true) {
            System.out.println("\n" + Colors.CYAN + Colors.BOLD + 
                              "=== EDITING: " + patient.getName() + " ===" + Colors.RESET);
            
            displayCurrentInfo(patient);
            
            String[][] menuItems = {
                {"1", "Edit Name", Colors.CYAN},
                {"2", "Edit Age", Colors.BLUE},
                {"3", "Edit Birthday", Colors.BLUE},
                {"4", "Edit Patient Type", Colors.YELLOW},
                {"5", "Add Note", Colors.GREEN},
                {"6", "Back to Main Menu", Colors.WHITE}
            };
            
            for (String[] item : menuItems) {
                System.out.println(item[2] + "[" + item[0] + "] " + item[1] + Colors.RESET);
            }
            System.out.println(Colors.CYAN + Colors.BOLD + "========================" + Colors.RESET);
            
            int choice = getEditChoice();
            if (!executeEditChoice(choice, patient)) {
                break; // Exit edit menu
            }
        }
    }
    
    private void displayCurrentInfo(Patient patient) {
        System.out.println(Colors.WHITE + "Current Information:" + Colors.RESET);
        System.out.println(patient.getType().getColor() + "  Name: " + patient.getName() + Colors.RESET);
        System.out.println(Colors.WHITE + "  Age: " + patient.getAge() + Colors.RESET);
        System.out.println(Colors.WHITE + "  Birthday: " + patient.getBirthday() + Colors.RESET);
        System.out.println(Colors.WHITE + "  Type: " + patient.getType().name() + 
                          " " + patient.getTypeIcon() + Colors.RESET);
        System.out.println(Colors.WHITE + "  Priority: " + patient.getPriorityLevel() + Colors.RESET);
        System.out.println(Colors.WHITE + "  Arrival: " + 
                          patient.getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + Colors.RESET);
        
        if (patient.hasNotes()) {
            System.out.println(Colors.YELLOW + "  Latest Note: " + patient.getLatestNote() + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "  No notes recorded." + Colors.RESET);
        }
        System.out.println();
    }
    
    private int getEditChoice() {
        int choice = 0;
        while (choice < 1 || choice > 6) {
            try {
                System.out.print(Colors.YELLOW + Colors.BOLD + "Enter choice (1-6): " + Colors.RESET);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(Colors.RED + "Input cannot be empty." + Colors.RESET);
                    continue;
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 6) {
                    System.out.println(Colors.RED + "Please enter a number between 1 and 6." + Colors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Invalid input! Please enter a number between 1 and 6." + Colors.RESET);
            }
        }
        return choice;
    }
    
    private boolean executeEditChoice(int choice, Patient patient) {
        try {
            switch (choice) {
                case 1: editName(patient); break;
                case 2: editAge(patient); break;
                case 3: editBirthday(patient); break;
                case 4: editPatientType(patient); break;
                case 5: addNote(patient); break;
                case 6: return false; // Exit edit menu
            }
            return true;
        } catch (Exception e) {
            System.out.println(Colors.RED + "Error during edit: " + e.getMessage() + Colors.RESET);
            return true;
        }
    }
    
    private void editName(Patient patient) {
        String oldName = patient.getName();
        String newName = getInput("Enter new name (current: " + oldName + "): ");
        
        if (newName.equals(oldName)) {
            System.out.println(Colors.YELLOW + "Name unchanged." + Colors.RESET);
            return;
        }
        
        // Check if new name already exists
        if (patientManagement.findPatientByExactName(newName) != null) {
            System.out.println(Colors.RED + "A patient with name '" + newName + "' already exists." + Colors.RESET);
            return;
        }
        
        if (confirmChange("name", oldName, newName)) {
            // Need to use reflection or create setName method in Patient class
            // For now, we'll add a note about the name change
            patient.addNote("Name change requested from '" + oldName + "' to '" + newName + "' - requires manual update");
            System.out.println(Colors.YELLOW + "Name change noted. Manual update required." + Colors.RESET);
            System.out.println(Colors.CYAN + "Note: Patient class needs setName() method for full functionality." + Colors.RESET);
        }
    }
    
    private void editAge(Patient patient) {
        int oldAge = patient.getAge();
        int newAge = getValidatedInput("Enter new age (current: " + oldAge + "): ",
            Integer::parseInt, 
            age -> age >= 0 && age <= 150, 
            "Enter age between 0 and 150.");
        
        if (newAge == oldAge) {
            System.out.println(Colors.YELLOW + "Age unchanged." + Colors.RESET);
            return;
        }
        
        if (confirmChange("age", String.valueOf(oldAge), String.valueOf(newAge))) {
            // Need to use reflection or create setAge method in Patient class
            // For now, we'll add a note about the age change
            patient.addNote("Age change requested from " + oldAge + " to " + newAge + " - requires manual update");
            
            // Check if patient type should be updated based on new age
            if (newAge >= 75 && patient.getType() == PatientType.REGULAR) {
                System.out.println(Colors.YELLOW + "Patient would now be 75+. Consider changing to Senior type." + Colors.RESET);
            } else if (newAge < 75 && patient.getType() == PatientType.SENIOR) {
                System.out.println(Colors.YELLOW + "Patient would now be under 75. Consider changing from Senior type." + Colors.RESET);
            }
            
            System.out.println(Colors.YELLOW + "Age change noted. Manual update required." + Colors.RESET);
            System.out.println(Colors.CYAN + "Note: Patient class needs setAge() method for full functionality." + Colors.RESET);
        }
    }
    
    private void editBirthday(Patient patient) {
        LocalDate oldBirthday = patient.getBirthday();
        LocalDate newBirthday = getDateInput("Enter new birthday (current: " + oldBirthday + ", format: yyyy-MM-dd): ");
        
        if (newBirthday.equals(oldBirthday)) {
            System.out.println(Colors.YELLOW + "Birthday unchanged." + Colors.RESET);
            return;
        }
        
        if (confirmChange("birthday", oldBirthday.toString(), newBirthday.toString())) {
            // Need to use reflection or create setBirthday method in Patient class
            // For now, we'll add a note about the birthday change
            patient.addNote("Birthday change requested from " + oldBirthday + " to " + newBirthday + " - requires manual update");
            System.out.println(Colors.YELLOW + "Birthday change noted. Manual update required." + Colors.RESET);
            System.out.println(Colors.CYAN + "Note: Patient class needs setBirthday() method for full functionality." + Colors.RESET);
        }
    }
    
    private void editPatientType(Patient patient) {
        PatientType oldType = patient.getType();
        System.out.println("Current type: " + oldType.name() + " " + oldType.getIcon());
        
        String newTypeName = getNewPatientType();
        if (newTypeName == null) return; // Cancelled
        
        PatientType newType = PatientType.valueOf(newTypeName.toUpperCase());
        
        if (newType == oldType) {
            System.out.println(Colors.YELLOW + "Patient type unchanged." + Colors.RESET);
            return;
        }
        
        // Validation for Senior type
        if (newType == PatientType.SENIOR && patient.getAge() < 75) {
            System.out.println(Colors.RED + "Patient must be 75+ for Senior type." + Colors.RESET);
            return;
        }
        
        if (confirmChange("patient type", oldType.name(), newType.name())) {
            // Remove and re-add to update queue priority
            patientManagement.removePatient(patient.getName());
            patient.changeType(newType);
            patientManagement.queuePatient(patient);
            
            System.out.println(Colors.GREEN + "Patient type updated successfully!" + Colors.RESET);
            System.out.println(Colors.CYAN + "Patient moved to " + newType.getQueueName() + Colors.RESET);
        }
    }
    
    private void addNote(Patient patient) {
        String note = getInput("Enter note: ");
        patient.addNote(note);
        System.out.println(Colors.GREEN + "Note added successfully!" + Colors.RESET);
    }
    
    private boolean confirmChange(String field, String oldValue, String newValue) {
        System.out.println(Colors.YELLOW + "Change " + field + " from '" + oldValue + 
                          "' to '" + newValue + "'?" + Colors.RESET);
        return getBooleanInput("Confirm change? (y/n): ");
    }
    
    // Utility methods (similar to Controller class)
    private String getInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) System.out.println(Colors.RED + "Input cannot be empty." + Colors.RESET);
        } while (input.isEmpty());
        return input;
    }
    
    private <T> T getValidatedInput(String prompt, Function<String, T> parser, 
                                   Predicate<T> validator, String errorMessage) {
        T value = null;
        while (value == null) {
            try {
                System.out.print(Colors.YELLOW + Colors.BOLD + prompt + Colors.RESET);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(Colors.RED + "Input cannot be empty." + Colors.RESET);
                    continue;
                }
                
                value = parser.apply(input);
                if (!validator.test(value)) {
                    System.out.println(Colors.RED + errorMessage + Colors.RESET);
                    value = null;
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input! " + errorMessage + Colors.RESET);
            }
        }
        return value;
    }
    
    private LocalDate getDateInput(String prompt) {
        return getValidatedInput(prompt, 
            input -> LocalDate.parse(input, DATE_FORMATTER),
            date -> !date.isAfter(LocalDate.now()) && !date.isBefore(LocalDate.now().minusYears(150)),
            "Invalid date! Use yyyy-MM-dd format and ensure date is not in future or older than 150 years.");
    }
    
    private boolean getBooleanInput(String prompt) {
        String input;
        while (true) {
            System.out.print(Colors.YELLOW + Colors.BOLD + prompt + Colors.RESET);
            input = scanner.nextLine().trim().toLowerCase();
            
            if (input.matches("y|n|yes|no")) {
                return input.startsWith("y");
            } else {
                System.out.println(Colors.RED + "Please enter y/n or yes/no." + Colors.RESET);
            }
        }
    }
    
    private String getNewPatientType() {
        System.out.println("[1] Emergency [2] Senior [3] Regular [4] Cancel");
        int choice = getValidatedInput("Select type: ", Integer::parseInt,
            c -> c >= 1 && c <= 4, "Select 1-4.");
        
        switch (choice) {
            case 1: return "Emergency";
            case 2: return "Senior";
            case 3: return "Regular";
            default: return null;
        }
    }
}