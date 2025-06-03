import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class Controller {
    private PatientManagement patientManagement;
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Controller() {
        patientManagement = new PatientManagement();
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println(Colors.CYAN + Colors.BOLD + "Welcome to the Patient Management System!" + Colors.RESET);

        int choice;
        do {
            printMenu();
            choice = getChoice();
            executeChoice(choice);
        } while (choice != 11);

        scanner.close();
        System.out.println(Colors.GREEN + "Thank you for using the Patient Management System. Goodbye!" + Colors.RESET);
    }

    private void printMenu() {
        System.out.println("\n" + Colors.YELLOW + Colors.BOLD + "=== Patient Management System ===" + Colors.RESET);
        String[][] menuItems = {
            {"1", "Add Patient", Colors.CYAN},
            {"2", "Print Waiting Room", Colors.BLUE},
            {"3", "Print Next Patient", Colors.BLUE},
            {"4", "Call Up Next Patient", Colors.GREEN},
            {"5", "Remove Patient", Colors.RED},
            {"6", "View Statistics", Colors.BLUE},
            {"7", "Add Patient Notes", Colors.CYAN},
            {"8", "Search Patient by Name", Colors.BLUE},
            {"9", "Change Patient Type", Colors.YELLOW},
            {"10", "View Patient Note History", Colors.BLUE},
            {"11", "Exit", Colors.RED + Colors.BOLD}
        };
        
        for (String[] item : menuItems) {
            System.out.println(item[2] + "[" + item[0] + "] " + item[1] + Colors.RESET);
        }
        System.out.println(Colors.YELLOW + Colors.BOLD + "==============================" + Colors.RESET);
    }

    private int getChoice() {
        return getValidatedInput("Enter choice (1-11): ", Integer::parseInt, 
            choice -> choice >= 1 && choice <= 11, "Please enter a number between 1 and 11.");
    }

    private void executeChoice(int choice) {
        try {
            switch (choice) {
                case 1: addPatient(); break;
                case 2: patientManagement.printPatients(); break;
                case 3: patientManagement.printNextPatient(); break;
                case 4: callNextPatient(); break;
                case 5: removePatient(); break;
                case 6: patientManagement.printStatistics(); break;
                case 7: addPatientNotes(); break;
                case 8: searchPatientByName(); break;
                case 9: changePatientType(); break;
                case 10: viewPatientNoteHistory(); break;
                case 11: System.out.println(Colors.YELLOW + "Exiting..." + Colors.RESET); break;
            }
        } catch (Exception e) {
            System.out.println(Colors.RED + "Error: " + e.getMessage() + Colors.RESET);
        }
    }

    private void addPatient() {
        try {
            System.out.println("\n" + Colors.GREEN + "=== ADD NEW PATIENT ===" + Colors.RESET);

            String name = getInput("Enter patient name: ");
            int age = getValidatedInput("Enter patient age: ", Integer::parseInt, 
                a -> a >= 0 && a <= 150, "Enter age between 0 and 150.");
            LocalDate birthday = getDateInput("Enter birthday (yyyy-MM-dd): ");
            boolean isEmergency = getBooleanInput("Is this an emergency patient? (y/n): ");
            String notes = getInput("Enter optional notes (press Enter to skip): ");

            Patient patient = Patient.createByType(name, age, birthday, notes, isEmergency);
            patientManagement.queuePatient(patient);
            System.out.println(Colors.GREEN + "Patient added successfully!" + Colors.RESET);
            displayQueueSummary();

        } catch (Exception e) {
            System.out.println(Colors.RED + "Error adding patient: " + e.getMessage() + Colors.RESET);
        }
    }

    private void removePatient() {
        if (checkEmptyQueue()) return;
        
        patientManagement.printPatients();
        String name = getInput("Enter patient name to remove: ");
        
        if (patientManagement.removePatient(name)) {
            System.out.println(Colors.GREEN + "Patient removed successfully!" + Colors.RESET);
            displayQueueSummary();
        } else {
            System.out.println(Colors.RED + "Patient not found." + Colors.RESET);
        }
    }

    private void addPatientNotes() {
        if (checkEmptyQueue()) return;
        
        patientManagement.printPatients();
        String name = getInput("Enter patient name: ");
        String notes = getInput("Enter notes: ");
        
        Patient patient = patientManagement.findPatientByExactName(name);
        if (patient != null) {
            patient.addNote(notes);
            System.out.println(Colors.GREEN + "Notes added successfully!" + Colors.RESET);
        } else {
            System.out.println(Colors.RED + "Patient not found." + Colors.RESET);
        }
    }

    private void searchPatientByName() {
        if (checkEmptyQueue()) return;
        
        String searchName = getInput("Enter name to search: ");
        patientManagement.printSearchResults(searchName);
    }

    private void changePatientType() {
        if (checkEmptyQueue()) return;
        
        patientManagement.printPatients();
        String name = getInput("Enter patient name: ");
        
        Patient patient = patientManagement.findPatientByExactName(name);
        if (patient == null) {
            System.out.println(Colors.RED + "Patient not found." + Colors.RESET);
            return;
        }

        String newType = getNewPatientType();
        if (newType == null) return;

        if (newType.equals("Senior") && patient.getAge() < 75) {
            System.out.println(Colors.RED + "Patient must be 75+ for Senior type." + Colors.RESET);
            return;
        }

        if (getBooleanInput("Confirm change to " + newType + "? (y/n): ")) {
            patientManagement.changePatientType(name, newType);
            displayQueueSummary();
        }
    }

    private void viewPatientNoteHistory() {
        if (checkEmptyQueue()) return;
        
        patientManagement.printPatients();
        String name = getInput("Enter patient name: ");
        
        Patient patient = patientManagement.findPatientByExactName(name);
        if (patient != null) {
            System.out.println("\n" + Colors.CYAN + "=== NOTE HISTORY: " + name + " ===" + Colors.RESET);
            System.out.println(patient.getFormattedNotesHistory());
        } else {
            System.out.println(Colors.RED + "Patient not found." + Colors.RESET);
        }
    }

    private void callNextPatient() {
        Patient patient = patientManagement.dequeuePatient();
        if (patient != null) {
            System.out.println("\n" + Colors.GREEN + "Patient Called: " + patient.getName() + Colors.RESET);
            System.out.println("Type: " + patient.getTypeIcon());
            System.out.println("Age: " + patient.getAge());
            displayQueueSummary();
        }
    }

    // Utility methods
    private boolean checkEmptyQueue() {
        if (patientManagement.getTotalPatientCount() == 0) {
            System.out.println(Colors.RED + "No patients in queue." + Colors.RESET);
            return true;
        }
        return false;
    }

    private String getInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty() && prompt.contains("optional")) break;
            if (input.isEmpty()) System.out.println(Colors.RED + "Input cannot be empty." + Colors.RESET);
        } while (input.isEmpty() && !prompt.contains("optional"));
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

    private void displayQueueSummary() {
        System.out.println(Colors.CYAN + "Queue: " + patientManagement.getTotalPatientCount() + 
                          " patients remaining" + Colors.RESET);
    }
}