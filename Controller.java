import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

/**
 * Controller class - Main controller for the Patient Management System
 * Handles user interface, input validation, and coordinates between user and PatientManagement
 * Provides menu-driven interface for managing patients with color support
 */
public class Controller {
    // Core components
    private PatientManagement patientManagement;
    private Scanner scanner;

    // Date formatter for birthday input
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ANSI Color codes for console output
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

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
        System.out.println(CYAN + BOLD + "Welcome to the Patient Management System!" + RESET);
        System.out.println(CYAN + "========================================" + RESET);

        int choice;
        do {
            printMenu();
            choice = getChoice();
            executeChoice(choice);
        } while (choice != 6);

        // Clean up resources
        scanner.close();
        System.out.println(GREEN + "Thank you for using the Patient Management System. Goodbye!" + RESET);
    }

    /**
     * Displays the main menu options to the user with colors
     */
    private void printMenu() {
        System.out.println("\n" + BLUE + "====================" + RESET);
        System.out.println(BLUE + BOLD + "Patient Management System" + RESET);
        System.out.println(BLUE + "====================" + RESET);
        System.out.println(GREEN + "[1] Add Patient" + RESET);
        System.out.println(CYAN + "[2] Print Waiting Room" + RESET);
        System.out.println(BLUE + "[3] Print Next Patient" + RESET);
        System.out.println(PURPLE + "[4] Call Up Next Patient" + RESET);
        System.out.println(YELLOW + "[5] Remove Patient" + RESET);
        System.out.println(RED + "[6] Exit" + RESET);
        System.out.println(BLUE + "====================" + RESET);
    }

    /**
     * Gets and validates user's menu choice
     * Handles invalid input gracefully
     *
     * @return Valid menu choice (1-6)
     */
    private int getChoice() {
        int choice = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print(WHITE + "Enter choice (1-6): " + RESET);
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                if (choice >= 1 && choice <= 6) {
                    validInput = true;
                } else {
                    System.out.println(RED + "Please enter a number between 1 and 6." + RESET);
                }

            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid input! Please enter a number between 1 and 6." + RESET);
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
                    removePatient();
                    break;
                case 6:
                    System.out.println(YELLOW + "Exiting..." + RESET);
                    break;
                default:
                    System.out.println(RED + "Invalid choice. Please try again." + RESET);
                    break;
            }
        } catch (Exception e) {
            System.out.println(RED + "An error occurred: " + e.getMessage() + RESET);
            System.out.println(YELLOW + "Please try again." + RESET);
        }
    }

    /**
     * Handles adding a new patient to the system
     * Collects patient information and creates appropriate patient object
     * Includes input validation for all fields
     */
    private void addPatient() {
        try {
            System.out.println("\n" + GREEN + BOLD + "=== ADD NEW PATIENT ===" + RESET);

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
            System.out.println(RED + "Error adding patient: " + e.getMessage() + RESET);
            System.out.println(YELLOW + "Patient was not added. Please try again." + RESET);
        }
    }

    /**
     * Handles removing a patient from the system
     * Shows current patients and allows user to select which one to remove
     */
    private void removePatient() {
        try {
            System.out.println("\n" + YELLOW + BOLD + "=== REMOVE PATIENT ===" + RESET);

            // Check if there are any patients
            if (patientManagement.getTotalPatientCount() == 0) {
                System.out.println(RED + "No patients in queue to remove." + RESET);
                return;
            }

            // Show current patients
            patientManagement.printPatients();

            System.out.println(WHITE + "\nEnter the name of the patient to remove:" + RESET);
            System.out.print(WHITE + "Patient name: " + RESET);
            String nameToRemove = scanner.nextLine().trim();

            if (nameToRemove.isEmpty()) {
                System.out.println(RED + "Name cannot be empty." + RESET);
                return;
            }

            // Try to remove the patient
            boolean removed = patientManagement.removePatient(nameToRemove);

            if (removed) {
                System.out.println(GREEN + "Patient '" + nameToRemove + "' has been removed from the queue." + RESET);
            } else {
                System.out.println(RED + "Patient '" + nameToRemove + "' not found in queue." + RESET);
            }

        } catch (Exception e) {
            System.out.println(RED + "Error removing patient: " + e.getMessage() + RESET);
            System.out.println(YELLOW + "Please try again." + RESET);
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
            System.out.print(WHITE + "Enter patient name: " + RESET);
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println(RED + "Name cannot be empty. Please enter a valid name." + RESET);
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
                System.out.print(WHITE + "Enter patient age: " + RESET);
                age = scanner.nextInt();
                scanner.nextLine(); // Consume leftover newline

                if (age >= 0 && age <= 150) {
                    validAge = true;
                } else {
                    System.out.println(RED + "Please enter a valid age between 0 and 150." + RESET);
                }

            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid input! Please enter a number for age." + RESET);
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
                System.out.print(WHITE + "Enter birthday (yyyy-MM-dd, e.g., 1990-05-15): " + RESET);
                String dateInput = scanner.nextLine().trim();
                birthday = LocalDate.parse(dateInput, DATE_FORMATTER);

                // Check if date is reasonable (not in future, not too old)
                LocalDate today = LocalDate.now();
                LocalDate earliestDate = today.minusYears(150);

                if (birthday.isAfter(today)) {
                    System.out.println(RED + "Birthday cannot be in the future. Please enter a valid date." + RESET);
                } else if (birthday.isBefore(earliestDate)) {
                    System.out.println(RED + "Birthday seems too old. Please enter a valid date." + RESET);
                } else {
                    validDate = true;
                }

            } catch (DateTimeParseException e) {
                System.out.println(RED + "Invalid date format! Please use yyyy-MM-dd format (e.g., 1990-05-15)." + RESET);
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
            System.out.print(WHITE + "Is this an emergency patient? (y/n): " + RESET);
            input = scanner.nextLine().trim().toLowerCase();

            if (!input.equals("y") && !input.equals("n") &&
                    !input.equals("yes") && !input.equals("no")) {
                System.out.println(RED + "Please enter 'y' for yes or 'n' for no." + RESET);
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
            System.out.println("\n" + GREEN + BOLD + "--- Patient Called ---" + RESET);
            System.out.println(WHITE + "Patient: " + calledPatient.getName() + RESET);

            // Color code patient type
            String patientType = calledPatient.getPatientType();
            if (patientType.equals("Emergency")) {
                System.out.println(RED + "Type: " + patientType + RESET);
            } else {
                System.out.println(BLUE + "Type: " + patientType + RESET);
            }

            System.out.println(WHITE + "Age: " + calledPatient.getAge() + RESET);
            System.out.println(WHITE + "Birthday: " + calledPatient.getBirthday() + RESET);

            // Show remaining patient count
            int totalRemaining = patientManagement.getTotalPatientCount();
            System.out.println(CYAN + "\nPatients remaining in queue: " + totalRemaining + RESET);

            if (totalRemaining > 0) {
                System.out.println(RED + "Emergency patients waiting: " + patientManagement.getEmergencyCount() + RESET);
                System.out.println(BLUE + "Regular patients waiting: " + patientManagement.getRegularCount() + RESET);
            }
        }
    }
}