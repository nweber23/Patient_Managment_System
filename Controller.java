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
 * Now includes patient search, note history, and patient type switching with age validation
 */
public class Controller {
	// Core components
	private PatientManagement patientManagement;
	private Scanner scanner;

	// Date formatter for birthday input
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// ANSI Color codes for console output - Enhanced with more unique colors
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
	public static final String BRIGHT_RED = "\u001B[91m";
	public static final String BRIGHT_GREEN = "\u001B[92m";
	public static final String BRIGHT_YELLOW = "\u001B[93m";
	public static final String BRIGHT_BLUE = "\u001B[94m";
	public static final String BRIGHT_MAGENTA = "\u001B[95m";
	public static final String BRIGHT_CYAN = "\u001B[96m";
	public static final String BRIGHT_WHITE = "\u001B[97m";
	public static final String DARK_GRAY = "\u001B[90m";
	public static final String LIGHT_GRAY = "\u001B[37m";
	public static final String GOLD = "\u001B[38;5;220m";
	public static final String PINK = "\u001B[38;5;205m";
	public static final String LIME = "\u001B[38;5;154m";
	public static final String TEAL = "\u001B[38;5;51m";
	public static final String LAVENDER = "\u001B[38;5;183m";

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
		System.out.println(TEAL + BOLD + "Welcome to the Patient Management System!" + RESET);
		System.out.println(TEAL + "========================================" + RESET);

		int choice;
		do {
			printMenu();
			choice = getChoice();
			executeChoice(choice);
		} while (choice != 11);

		// Clean up resources
		scanner.close();
		System.out.println(LIME + "Thank you for using the Patient Management System. Goodbye!" + RESET);
	}

	/**
	 * Displays the main menu options to the user with unique colors
	 */
	private void printMenu() {
		System.out.println("\n" + GOLD + "====================" + RESET);
		System.out.println(GOLD + BOLD + "Patient Management System" + RESET);
		System.out.println(GOLD + "====================" + RESET);
		System.out.println(BRIGHT_GREEN + "[1] Add Patient" + RESET);
		System.out.println(BRIGHT_CYAN + "[2] Print Waiting Room" + RESET);
		System.out.println(BRIGHT_BLUE + "[3] Print Next Patient" + RESET);
		System.out.println(BRIGHT_MAGENTA + "[4] Call Up Next Patient" + RESET);
		System.out.println(BRIGHT_YELLOW + "[5] Remove Patient" + RESET);
		System.out.println(ORANGE + "[6] View Statistics" + RESET);
		System.out.println(LAVENDER + "[7] Add Patient Notes" + RESET);
		System.out.println(TEAL + "[8] Search Patient by Name" + RESET);
		System.out.println(PINK + "[9] Change Patient Type" + RESET);
		System.out.println(LIME + "[10] View Patient Note History" + RESET);
		System.out.println(BRIGHT_RED + "[11] Exit" + RESET);
		System.out.println(GOLD + "====================" + RESET);
	}

	/**
	 * Gets and validates user's menu choice
	 * Handles invalid input gracefully
	 *
	 * @return Valid menu choice (1-11)
	 */
	private int getChoice() {
		int choice = -1;
		boolean validInput = false;

		while (!validInput) {
			try {
				System.out.print(BRIGHT_WHITE + "Enter choice (1-11): " + RESET);
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume the leftover newline

				if (choice >= 1 && choice <= 11) {
					validInput = true;
				} else {
					System.out.println(BRIGHT_RED + "Please enter a number between 1 and 11." + RESET);
				}

			} catch (InputMismatchException e) {
				System.out.println(BRIGHT_RED + "Invalid input! Please enter a number between 1 and 11." + RESET);
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
					searchPatientByName();
					break;
				case 9:
					changePatientType();
					break;
				case 10:
					viewPatientNoteHistory();
					break;
				case 11:
					System.out.println(BRIGHT_YELLOW + "Exiting..." + RESET);
					break;
				default:
					System.out.println(BRIGHT_RED + "Invalid choice. Please try again." + RESET);
					break;
			}
		} catch (Exception e) {
			System.out.println(BRIGHT_RED + "An error occurred: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Handles searching for patients by name
	 * Uses the PatientManagement search functionality
	 */
	private void searchPatientByName() {
		try {
			System.out.println("\n" + TEAL + BOLD + "=== SEARCH PATIENT BY NAME ===" + RESET);

			// Check if there are any patients
			if (patientManagement.getTotalPatientCount() == 0) {
				System.out.println(BRIGHT_RED + "No patients in queue to search." + RESET);
				return;
			}

			System.out.print(BRIGHT_WHITE + "Enter patient name (or part of name) to search: " + RESET);
			String searchName = scanner.nextLine().trim();

			if (searchName.isEmpty()) {
				System.out.println(BRIGHT_RED + "Search name cannot be empty." + RESET);
				return;
			}

			// Use PatientManagement's search functionality
			patientManagement.printSearchResults(searchName);

		} catch (Exception e) {
			System.out.println(BRIGHT_RED + "Error searching for patient: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Handles changing a patient's type (Emergency, Senior, Regular)
	 * Enhanced with age validation for Senior patient type
	 */
	private void changePatientType() {
		try {
			System.out.println("\n" + PINK + BOLD + "=== CHANGE PATIENT TYPE ===" + RESET);

			// Check if there are any patients
			if (patientManagement.getTotalPatientCount() == 0) {
				System.out.println(BRIGHT_RED + "No patients in queue to change type." + RESET);
				return;
			}

			// Show current patients
			patientManagement.printPatients();

			System.out.println(BRIGHT_WHITE + "\nEnter the name of the patient to change type:" + RESET);
			System.out.print(BRIGHT_WHITE + "Patient name: " + RESET);
			String patientName = scanner.nextLine().trim();

			if (patientName.isEmpty()) {
				System.out.println(BRIGHT_RED + "Name cannot be empty." + RESET);
				return;
			}

			// Check if patient exists
			Patient patient = patientManagement.findPatientByExactName(patientName);
			if (patient == null) {
				System.out.println(BRIGHT_RED + "Patient '" + patientName + "' not found in queue." + RESET);
				return;
			}

			// Show current patient type and age
			System.out.println(BRIGHT_WHITE + "Current patient type: " + patient.getPatientType() + RESET);
			System.out.println(BRIGHT_WHITE + "Patient age: " + patient.getAge() + " years" + RESET);

			// Get new patient type
			String newType = getNewPatientType();
			if (newType == null) {
				System.out.println(BRIGHT_YELLOW + "Operation cancelled." + RESET);
				return;
			}

			// AGE VALIDATION: Block changing to Senior if patient is under 75
			if (newType.equals("Senior") && patient.getAge() < 75) {
				System.out.println(BRIGHT_RED + "ERROR: Cannot change patient to Senior type!" + RESET);
				System.out.println(BRIGHT_RED + "Patient " + patientName + " is only " + patient.getAge() + 
								   " years old. Senior status requires age 75 or older." + RESET);
				System.out.println(GOLD + "Only patients aged 75+ can be classified as Senior patients." + RESET);
				return;
			}

			// Confirm the change
			System.out.print(BRIGHT_YELLOW + "Are you sure you want to change " + patientName + 
							" from " + patient.getPatientType() + " to " + newType + "? (y/n): " + RESET);
			String confirmation = scanner.nextLine().trim().toLowerCase();

			if (!confirmation.equals("y") && !confirmation.equals("yes")) {
				System.out.println(BRIGHT_YELLOW + "Operation cancelled." + RESET);
				return;
			}

			// Attempt to change patient type
			boolean success = patientManagement.changePatientType(patientName, newType);

			if (!success) {
				System.out.println(BRIGHT_RED + "Failed to change patient type. Please try again." + RESET);
			} else {
				// Display updated queue summary
				displayQueueSummary();
			}

		} catch (Exception e) {
			System.out.println(BRIGHT_RED + "Error changing patient type: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Gets the new patient type from user input
	 * Enhanced with better color coding and age requirement information
	 *
	 * @return New patient type string, or null if cancelled
	 */
	private String getNewPatientType() {
		System.out.println(BRIGHT_WHITE + "\nAvailable patient types:" + RESET);
		System.out.println(BRIGHT_RED + "[1] Emergency" + RESET);
		System.out.println(ORANGE + "[2] Senior " + DARK_GRAY + "(requires age 75+)" + RESET);
		System.out.println(BRIGHT_BLUE + "[3] Regular" + RESET);
		System.out.println(BRIGHT_YELLOW + "[4] Cancel" + RESET);

		int choice = -1;
		boolean validInput = false;

		while (!validInput) {
			try {
				System.out.print(BRIGHT_WHITE + "Select new patient type (1-4): " + RESET);
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume leftover newline

				if (choice >= 1 && choice <= 4) {
					validInput = true;
				} else {
					System.out.println(BRIGHT_RED + "Please enter a number between 1 and 4." + RESET);
				}

			} catch (InputMismatchException e) {
				System.out.println(BRIGHT_RED + "Invalid input! Please enter a number between 1 and 4." + RESET);
				scanner.nextLine(); // Clear invalid input
			}
		}

		switch (choice) {
			case 1:
				return "Emergency";
			case 2:
				return "Senior";
			case 3:
				return "Regular";
			case 4:
				return null; // Cancel
			default:
				return null;
		}
	}

	/**
	 * Handles viewing a patient's complete note history
	 */
	private void viewPatientNoteHistory() {
		try {
			System.out.println("\n" + LIME + BOLD + "=== VIEW PATIENT NOTE HISTORY ===" + RESET);

			// Check if there are any patients
			if (patientManagement.getTotalPatientCount() == 0) {
				System.out.println(BRIGHT_RED + "No patients in queue to view notes." + RESET);
				return;
			}

			// Show current patients
			patientManagement.printPatients();

			System.out.println(BRIGHT_WHITE + "\nEnter the name of the patient to view note history:" + RESET);
			System.out.print(BRIGHT_WHITE + "Patient name: " + RESET);
			String patientName = scanner.nextLine().trim();

			if (patientName.isEmpty()) {
				System.out.println(BRIGHT_RED + "Name cannot be empty." + RESET);
				return;
			}

			// Find patient in the system
			Patient patient = patientManagement.findPatientByExactName(patientName);
			if (patient != null) {
				System.out.println("\n" + LIME + BOLD + "=== NOTE HISTORY FOR: " + patient.getName() + " ===" + RESET);
				System.out.println(BRIGHT_WHITE + "Patient Type: " + patient.getPatientType() + RESET);
				System.out.println(BRIGHT_WHITE + "Age: " + patient.getAge() + " | Birthday: " + patient.getBirthday() + RESET);
				System.out.println(TEAL + "Complete Note History:" + RESET);
				System.out.println(LAVENDER + patient.getFormattedNotesHistory() + RESET);
			} else {
				System.out.println(BRIGHT_RED + "Patient '" + patientName + "' not found in queue." + RESET);
			}

		} catch (Exception e) {
			System.out.println(BRIGHT_RED + "Error viewing note history: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Handles adding a new patient to the system
	 * Collects patient information and creates appropriate patient object
	 * Includes input validation for all fields
	 */
	private void addPatient() {
		try {
			System.out.println("\n" + BRIGHT_GREEN + BOLD + "=== ADD NEW PATIENT ===" + RESET);

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
			System.out.println(BRIGHT_RED + "Error adding patient: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Patient was not added. Please try again." + RESET);
		}
	}

	/**
	 * Handles removing a patient from the system
	 * Shows current patients and allows user to select which one to remove
	 */
	private void removePatient() {
		try {
			System.out.println("\n" + BRIGHT_YELLOW + BOLD + "=== REMOVE PATIENT ===" + RESET);

			// Check if there are any patients
			if (patientManagement.getTotalPatientCount() == 0) {
				System.out.println(BRIGHT_RED + "No patients in queue to remove." + RESET);
				return;
			}

			// Show current patients
			patientManagement.printPatients();

			System.out.println(BRIGHT_WHITE + "\nEnter the name of the patient to remove:" + RESET);
			System.out.print(BRIGHT_WHITE + "Patient name: " + RESET);
			String nameToRemove = scanner.nextLine().trim();

			if (nameToRemove.isEmpty()) {
				System.out.println(BRIGHT_RED + "Name cannot be empty." + RESET);
				return;
			}

			// Try to remove the patient
			boolean removed = patientManagement.removePatient(nameToRemove);

			if (removed) {
				System.out.println(BRIGHT_GREEN + "Patient '" + nameToRemove + "' has been removed from the queue." + RESET);
				displayQueueSummary();
			} else {
				System.out.println(BRIGHT_RED + "Patient '" + nameToRemove + "' not found in queue." + RESET);
			}

		} catch (Exception e) {
			System.out.println(BRIGHT_RED + "Error removing patient: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Please try again." + RESET);
		}
	}

	/**
	 * Handles adding notes to an existing patient
	 * Notes are appended to existing note history with timestamp
	 */
	private void addPatientNotes() {
		try {
			System.out.println("\n" + LAVENDER + BOLD + "=== ADD PATIENT NOTES ===" + RESET);

			// Check if there are any patients
			if (patientManagement.getTotalPatientCount() == 0) {
				System.out.println(BRIGHT_RED + "No patients in queue to add notes to." + RESET);
				return;
			}

			// Show current patients
			patientManagement.printPatients();

			System.out.println(BRIGHT_WHITE + "\nEnter the name of the patient to add notes to:" + RESET);
			System.out.print(BRIGHT_WHITE + "Patient name: " + RESET);
			String patientName = scanner.nextLine().trim();

			if (patientName.isEmpty()) {
				System.out.println(BRIGHT_RED + "Name cannot be empty." + RESET);
				return;
			}

			System.out.print(BRIGHT_WHITE + "Enter notes for " + patientName + ": " + RESET);
			String notes = scanner.nextLine().trim();

			if (notes.isEmpty()) {
				System.out.println(BRIGHT_RED + "Notes cannot be empty." + RESET);
				return;
			}

			// Find and update patient notes
			Patient patient = patientManagement.findPatientByExactName(patientName);
			if (patient != null) {
				patient.addNote(notes); // This will append with timestamp
				System.out.println(BRIGHT_GREEN + "Notes added successfully for patient '" + patientName + "'." + RESET);
				System.out.println(TEAL + "Note added: " + notes + RESET);
			} else {
				System.out.println(BRIGHT_RED + "Patient '" + patientName + "' not found in queue." + RESET);
			}

		} catch (Exception e) {
			System.out.println(BRIGHT_RED + "Error adding notes: " + e.getMessage() + RESET);
			System.out.println(BRIGHT_YELLOW + "Please try again." + RESET);
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
			System.out.print(BRIGHT_WHITE + "Enter patient name: " + RESET);
			name = scanner.nextLine().trim();

			if (name.isEmpty()) {
				System.out.println(BRIGHT_RED + "Name cannot be empty. Please enter a valid name." + RESET);
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
				System.out.print(BRIGHT_WHITE + "Enter patient age: " + RESET);
				age = scanner.nextInt();
				scanner.nextLine(); // Consume leftover newline

				if (age >= 0 && age <= 150) {
					validAge = true;
				} else {
					System.out.println(BRIGHT_RED + "Please enter a valid age between 0 and 150." + RESET);
				}

			} catch (InputMismatchException e) {
				System.out.println(BRIGHT_RED + "Invalid input! Please enter a number for age." + RESET);
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
				System.out.print(BRIGHT_WHITE + "Enter birthday (yyyy-MM-dd, e.g., 1990-05-15): " + RESET);
				String dateInput = scanner.nextLine().trim();
				birthday = LocalDate.parse(dateInput, DATE_FORMATTER);

				// Check if date is reasonable (not in the future, not too old)
				LocalDate today = LocalDate.now();
				LocalDate earliestDate = today.minusYears(150);

				if (birthday.isAfter(today)) {
					System.out.println(BRIGHT_RED + "Birthday cannot be in the future. Please enter a valid date." + RESET);
				} else if (birthday.isBefore(earliestDate)) {
					System.out.println(BRIGHT_RED + "Birthday seems too old. Please enter a valid date." + RESET);
				} else {
					validDate = true;
				}

			} catch (DateTimeParseException e) {
				System.out.println(BRIGHT_RED + "Invalid date format! Please use yyyy-MM-dd format (e.g., 1990-05-15)." + RESET);
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
			System.out.print(BRIGHT_WHITE + "Is this an emergency patient? (y/n): " + RESET);
			input = scanner.nextLine().trim().toLowerCase();

			if (!input.equals("y") && !input.equals("n") &&
					!input.equals("yes") && !input.equals("no")) {
				System.out.println(BRIGHT_RED + "Please enter 'y' for yes or 'n' for no." + RESET);
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
		System.out.print(BRIGHT_WHITE + "Enter optional notes (press Enter to skip): " + RESET);
		return scanner.nextLine().trim();
	}

	/**
	 * Calls the next patient and provides confirmation
	 * Shows additional information about remaining patients
	 */
	private void callNextPatient() {
		Patient calledPatient = patientManagement.dequeuePatient();

		if (calledPatient != null) {
			System.out.println("\n" + BRIGHT_GREEN + BOLD + "--- Patient Called ---" + RESET);
			System.out.println(BRIGHT_WHITE + "Patient: " + calledPatient.getName() + RESET);

			// Color code patient type
			String patientType = calledPatient.getPatientType();
			if (patientType.equals("Emergency")) {
				System.out.println(BRIGHT_RED + "Type: " + calledPatient.getTypeIcon() + RESET);
			} else if (patientType.equals("Senior")) {
				System.out.println(ORANGE + "Type: " + calledPatient.getTypeIcon() + RESET);
			} else {
				System.out.println(BRIGHT_BLUE + "Type: " + calledPatient.getTypeIcon() + RESET);
			}

			System.out.println(BRIGHT_WHITE + "Age: " + calledPatient.getAge() + RESET);
			System.out.println(BRIGHT_WHITE + "Birthday: " + calledPatient.getBirthday() + RESET);
			System.out.println(BRIGHT_WHITE + "Priority Level: " + calledPatient.getPriorityLevel() + RESET);

			// Show latest note if any
			if (!calledPatient.getLatestNote().isEmpty()) {
				System.out.println(LAVENDER + "Latest Note: " + calledPatient.getLatestNote() + RESET);
			}

			// Show remaining patient count
			displayQueueSummary();
		}
	}

	/**
	 * Displays a summary of current queue status with enhanced colors
	 */
	private void displayQueueSummary() {
		int totalRemaining = patientManagement.getTotalPatientCount();
		System.out.println(TEAL + "\n--- Queue Summary ---" + RESET);
		System.out.println(TEAL + "Patients remaining in queue: " + totalRemaining + RESET);

		if (totalRemaining > 0) {
			System.out.println(BRIGHT_RED + "Emergency patients: " + patientManagement.getEmergencyCount() + RESET);
			System.out.println(ORANGE + "Senior patients: " + patientManagement.getSeniorCount() + RESET);
			System.out.println(BRIGHT_BLUE + "Regular patients: " + patientManagement.getRegularCount() + RESET);
		}
	}
}