import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

/**
 * Controller class - Main controller for the Patient Management System
 * Handles user interface, input validation, and coordinates between user and PatientManagement
 * Provides menu-driven interface for managing patients
 */
public class Controller {
    // Core components
    private PatientManagement patientManagement;
    private Scanner scanner;

    // Date formatter for birthday input
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor for Controller
     * Initializes PatientManagement system and Scanner for user input
     */
    public Controller() {
        patientManagement = new PatientManagement();
        scanner = new Scanner(System.in);
    }

    /**
     * Main application loop
     * Displays menu, gets user choice, and executes corresponding action
     * Continues until user chooses to exit
     */
    public void run() {
        System.out.println("Welcome to the Patient Management System!");
        System.out.println("========================================");

        int choice;
        do {
            printMenu();
            choice = getChoice();
            executeChoice(choice);
        } while (choice != 5);

        // Clean up resources
        scanner.close();
        System.out.println("Thank you for using the Patient Management System. Goodbye!");
    }

    /**
     * Displays the main menu options to the user
     */
    private void printMenu() {
        System.out.println("\n====================");
        System.out.println("Patient Management System");
        System.out.println("====================");
        System.out.println("[1] Add Patient");
        System.out.println("[2] Print Waiting Room");
        System.out.println("[3] Print Next Patient");
        System.out.println("[4] Call Up Next Patient");
        System.out.println("[5] Exit");
        System.out.println("====================");
    }

    /**
     * Gets and validates user's menu choice
     * Handles invalid input gracefully
     *
     * @return Valid menu choice (1-5)
     */
    private int getChoice() {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter choice (1-5): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                if (choice >= 1 && choice <= 5) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between 1 and 5.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        return choice;
    }

    /**
     * Executes the action corresponding to user's menu choice
     *
     * @param choice User's menu selection
     */
    private void executeChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    patientManagement.printPatients();
                    break;
                case 3:
                    patientManagement.printNextPatient();
                    break;
                case 4:
                    callNextPatient();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    /**
     * Handles adding a new patient to the system
     * Collects patient information and creates appropriate patient object
     * Includes input validation for all fields
     */
    private void addPatient() {
        try {
            System.out.println("\n=== ADD NEW PATIENT ===");

            // Get patient name
            String name = getPatientName();

            // Get patient age
            int age = getPatientAge();

            // Get patient birthday
            LocalDate birthday = getPatientBirthday();

            // Get emergency status
            boolean isEmergency = getEmergencyStatus();

            // Create appropriate patient object
            Patient patient;
            if (isEmergency) {
                patient = new EmergencyPatient(name, age, birthday);
            } else {
                patient = new RegularPatient(name, age, birthday);
            }

            // Add patient to management system
            patientManagement.queuePatient(patient);

        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
            System.out.println("Patient was not added. Please try again.");
        }
    }

    /**
     * Gets and validates patient name
     *
     * @return Valid patient name (non-empty)
     */
    private String getPatientName() {
        String name;
        do {
            System.out.print("Enter patient name: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            }
        } while (name.isEmpty());

        return name;
    }

    /**
     * Gets and validates patient age
     *
     * @return Valid age (0-150)
     */
    private int getPatientAge() {
        int age = -1;
        boolean validAge = false;

        while (!validAge) {
            try {
                System.out.print("Enter patient age: ");
                age = scanner.nextInt();
                scanner.nextLine(); // Consume leftover newline

                if (age >= 0 && age <= 150) {
                    validAge = true;
                } else {
                    System.out.println("Please enter a valid age between 0 and 150.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number for age.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        return age;
    }

    /**
     * Gets and validates patient birthday
     *
     * @return Valid LocalDate for birthday
     */
    private LocalDate getPatientBirthday() {
        LocalDate birthday = null;
        boolean validDate = false;

        while (!validDate) {
            try {
                System.out.print("Enter birthday (yyyy-MM-dd, e.g., 1990-05-15): ");
                String dateInput = scanner.nextLine().trim();
                birthday = LocalDate.parse(dateInput, DATE_FORMATTER);

                // Check if date is reasonable (not in future, not too old)
                LocalDate today = LocalDate.now();
                LocalDate earliestDate = today.minusYears(150);

                if (birthday.isAfter(today)) {
                    System.out.println("Birthday cannot be in the future. Please enter a valid date.");
                } else if (birthday.isBefore(earliestDate)) {
                    System.out.println("Birthday seems too old. Please enter a valid date.");
                } else {
                    validDate = true;
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use yyyy-MM-dd format (e.g., 1990-05-15).");
            }
        }

        return birthday;
    }

    /**
     * Gets emergency status from user
     *
     * @return true if emergency patient, false if regular patient
     */
    private boolean getEmergencyStatus() {
        String input;
        do {
            System.out.print("Is this an emergency patient? (y/n): ");
            input = scanner.nextLine().trim().toLowerCase();

            if (!input.equals("y") && !input.equals("n") &&
                    !input.equals("yes") && !input.equals("no")) {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        } while (!input.equals("y") && !input.equals("n") &&
                !input.equals("yes") && !input.equals("no"));

        return input.equals("y") || input.equals("yes");
    }

    /**
     * Calls the next patient and provides confirmation
     * Shows additional information about remaining patients
     */
    private void callNextPatient() {
        Patient calledPatient = patientManagement.dequeuePatient();

        if (calledPatient != null) {
            System.out.println("\n--- Patient Called ---");
            System.out.println("Patient: " + calledPatient.getName());
            System.out.println("Type: " + calledPatient.getPatientType());
            System.out.println("Age: " + calledPatient.getAge());
            System.out.println("Birthday: " + calledPatient.getBirthday());

            // Show remaining patient count
            int totalRemaining = patientManagement.getTotalPatientCount();
            System.out.println("\nPatients remaining in queue: " + totalRemaining);

            if (totalRemaining > 0) {
                System.out.println("Emergency patients waiting: " + patientManagement.getEmergencyCount());
                System.out.println("Regular patients waiting: " + patientManagement.getRegularCount());
            }
        }
    }
}