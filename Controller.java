import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Controller class - Main controller for the Patient Management System
 * Handles user interface, input validation, and coordinates between user and PatientManagement
 * Provides menu-driven interface for managing patients with color support
 * Enhanced with statistics functionality and improved patient management
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
	public static final String ORANGE = "\u001B[38;5;208m";

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
		} while (choice != 8);

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
		System.out.println(ORANGE + "[6] View Statistics" + RESET);
		System.out.println(WHITE + "[7] Add Patient Notes" + RESET);
		System.out.println(RED + "[8] Exit" + RESET);
		System.out.println(BLUE + "====================" + RESET);
	}

	/**
	 * Gets and validates user's menu choice
	 * Handles invalid input gracefully
	 *
	 * @return Valid menu choice (1-8)
	 */
	private int getChoice() {
		int choice = -1;
		boolean validInput = false;

		while (!validInput) {
			try {
				System.out.print(WHITE + "Enter choice (1-8): " + RESET);
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume the leftover newline

				if (choice >= 1 && choice <= 8) {
					validInput = true;
				} else {
					System.out.println(RED + "Please enter a number between 1 and 8." + RESET);
				}

			} catch (InputMismatchException e) {
				System.out.println(RED + "Invalid input! Please enter a number between 1 and 8." + RESET);
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
					patientManagement.printStatistics();
					break;
				case 7:
					addPatientNotes();
					break;
				case 8:
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

			// Get optional notes
			String notes = getPatientNotes();

			// Create appropriate patient object
			Patient patient;
			if (isEmergency) {
				patient = new EmergencyPatient(name, age, birthday, notes);
			} else if (age >= 75) {
				patient = new SeniorPatient(name, age, birthday, notes);
			} else {
				patient = new RegularPatient(name, age, birthday, notes);
			}

			// Add patient to management system
			patientManagement.queuePatient(patient);

			// Display queue status after adding
			displayQueueSummary();

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
				displayQueueSummary();
			} else {
				System.out.println(RED + "Patient '" + nameToRemove + "' not found in queue." + RESET);
			}

		} catch (Exception e) {
			System.out.println(RED + "Error removing patient: " + e.getMessage() + RESET);
			System.out.println(YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Handles adding notes to an existing patient
	 */
	private void addPatientNotes() {
		try {
			System.out.println("\n" + WHITE + BOLD + "=== ADD PATIENT NOTES ===" + RESET);

			// Check if there are any patients
			if (patientManagement.getTotalPatientCount() == 0) {
				System.out.println(RED + "No patients in queue to add notes to." + RESET);
				return;
			}

			// Show current patients
			patientManagement.printPatients();

			System.out.println(WHITE + "\nEnter the name of the patient to add notes to:" + RESET);
			System.out.print(WHITE + "Patient name: " + RESET);
			String patientName = scanner.nextLine().trim();

			if (patientName.isEmpty()) {
				System.out.println(RED + "Name cannot be empty." + RESET);
				return;
			}

			System.out.print(WHITE + "Enter notes for " + patientName + ": " + RESET);
			String notes = scanner.nextLine().trim();

			// Find and update patient notes
			Patient patient = findPatient(patientName);
			if (patient != null) {
				patient.setNotes(notes);
				System.out.println(GREEN + "Notes added successfully for patient '" + patientName + "'." + RESET);
			} else {
				System.out.println(RED + "Patient '" + patientName + "' not found in queue." + RESET);
			}

		} catch (Exception e) {
			System.out.println(RED + "Error adding notes: " + e.getMessage() + RESET);
			System.out.println(YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Finds a patient in any queue by name
	 *
	 * @param patientName Name of patient to find
	 * @return Patient object if found, null otherwise
	 */
	private Patient findPatient(String patientName) {
		// This would require access to the patient arrays in PatientManagement
		// For now, we'll return null as this functionality would need to be added to PatientManagement
		return null;
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

				// Check if date is reasonable (not in the future, not too old)
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
	 * Gets optional notes for patient
	 *
	 * @return Notes string (can be empty)
	 */
	private String getPatientNotes() {
		System.out.print(WHITE + "Enter optional notes (press Enter to skip): " + RESET);
		return scanner.nextLine().trim();
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
				System.out.println(RED + "Type: " + calledPatient.getTypeIcon() + RESET);
			} else if (patientType.equals("Senior")) {
				System.out.println(ORANGE + "Type: " + calledPatient.getTypeIcon() + RESET);
			} else {
				System.out.println(BLUE + "Type: " + calledPatient.getTypeIcon() + RESET);
			}

			System.out.println(WHITE + "Age: " + calledPatient.getAge() + RESET);
			System.out.println(WHITE + "Birthday: " + calledPatient.getBirthday() + RESET);
			System.out.println(WHITE + "Priority Level: " + calledPatient.getPriorityLevel() + RESET);

			// Show notes if any
			if (!calledPatient.getNotes().isEmpty()) {
				System.out.println(YELLOW + "Notes: " + calledPatient.getNotes() + RESET);
			}

			// Show remaining patient count
			displayQueueSummary();
		}
	}

	/**
	 * Displays a summary of current queue status
	 */
	private void displayQueueSummary() {
		int totalRemaining = patientManagement.getTotalPatientCount();
		System.out.println(CYAN + "\n--- Queue Summary ---" + RESET);
		System.out.println(CYAN + "Patients remaining in queue: " + totalRemaining + RESET);

		if (totalRemaining > 0) {
			System.out.println(RED + "Emergency patients: " + patientManagement.getEmergencyCount() + RESET);
			System.out.println(ORANGE + "Senior patients: " + patientManagement.getSeniorCount() + RESET);
			System.out.println(BLUE + "Regular patients: " + patientManagement.getRegularCount() + RESET);
		}
	}
}