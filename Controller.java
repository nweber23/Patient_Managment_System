import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Controller class - Main controller for the Patient Management System
 * Provides a console-based interface for patient queue management with priority handling
 */
public class Controller {
	// Core system components
	private PatientManagement patientManagement;
	private Scanner scanner;
	
	// Date format for consistent birthday input validation
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// ANSI color codes for enhanced console output
	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[31m";      // Error messages
	public static final String GREEN = "\u001B[32m";    // Success messages
	public static final String YELLOW = "\u001B[33m";   // Warnings and headers
	public static final String BLUE = "\u001B[34m";     // Information
	public static final String CYAN = "\u001B[36m";     // Highlights
	public static final String BOLD = "\u001B[1m";      // Emphasis

	public Controller() {
		patientManagement = new PatientManagement();
		scanner = new Scanner(System.in);
	}

	/**
	 * Main application loop - displays menu and processes user choices
	 * Continues until user selects exit option (11)
	 */
	public void run() {
		System.out.println(CYAN + BOLD + "Welcome to the Patient Management System!" + RESET);

		int choice;
		do {
			printMenu();
			choice = getChoice();
			executeChoice(choice);
		} while (choice != 11);

		scanner.close();
		System.out.println(GREEN + "Thank you for using the Patient Management System. Goodbye!" + RESET);
	}

/**
 * Displays the main menu with all available options
 * Enhanced with color coding for better visual organization
 */
private void printMenu() {
	System.out.println("\n" + YELLOW + BOLD + "=== Patient Management System ===" + RESET);
	System.out.println(CYAN + "[1] Add Patient");
	System.out.println(BLUE + "[2] Print Waiting Room");
	System.out.println(BLUE + "[3] Print Next Patient");
	System.out.println(GREEN + "[4] Call Up Next Patient");
	System.out.println(RED + "[5] Remove Patient");
	System.out.println(BLUE + "[6] View Statistics");
	System.out.println(CYAN + "[7] Add Patient Notes");
	System.out.println(BLUE + "[8] Search Patient by Name");
	System.out.println(YELLOW + "[9] Change Patient Type");
	System.out.println(BLUE + "[10] View Patient Note History");
	System.out.println(RED + BOLD + "[11] Exit" + RESET);
	System.out.println(YELLOW + BOLD + "==============================" + RESET);
}

	/**
	 * Gets and validates user's menu choice with input validation
	 * Handles invalid input gracefully and repeats until valid choice
	 * @return Valid menu choice (1-11)
	 */
	private int getChoice() {
		int choice = -1;
		while (choice < 1 || choice > 11) {
			try {
				System.out.print(YELLOW + BOLD + "Enter choice (1-11): " + RESET);
				choice = scanner.nextInt();
				scanner.nextLine(); // Consume leftover newline
				if (choice < 1 || choice > 11) {
					System.out.println(RED + "Please enter a number between 1 and 11." + RESET);
				}
			} catch (InputMismatchException e) {
				System.out.println(RED + "Invalid input! Please enter a number." + RESET);
				scanner.nextLine(); // Clear invalid input buffer
			}
		}
		return choice;
	}

	/**
	 * Executes the action corresponding to user's menu choice
	 * Includes comprehensive error handling for all operations
	 * @param choice User's validated menu selection
	 */

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
				case 11: System.out.println(YELLOW + "Exiting..." + RESET); break;
			}
		} catch (Exception e) {
			System.out.println(RED + "Error: " + e.getMessage() + RESET);
		}
	}

	/**
	 * Handles adding a new patient to the queue system
	 * Automatically determines patient type based on age and emergency status:
	 * - Emergency patients get highest priority regardless of age
	 * - Patients 75+ become Senior patients (unless emergency)
	 * - All others become Regular patients
	 */
	private void addPatient() {
		try {
			System.out.println("\n" + GREEN + "=== ADD NEW PATIENT ===" + RESET);

			// Collect patient information with validation
			String name = getInput("Enter patient name: ");
			int age = getIntInput("Enter patient age: ", 0, 150);
			LocalDate birthday = getDateInput("Enter birthday (yyyy-MM-dd): ");
			boolean isEmergency = getBooleanInput("Is this an emergency patient? (y/n): ");
			String notes = getInput("Enter optional notes (press Enter to skip): ");

			// Create appropriate patient type based on priority rules
			Patient patient;
			if (isEmergency) {
				patient = new EmergencyPatient(name, age, birthday, notes);
			} else if (age >= 75) {
				patient = new SeniorPatient(name, age, birthday, notes);
			} else {
				patient = new RegularPatient(name, age, birthday, notes);
			}

			patientManagement.queuePatient(patient);
			System.out.println(GREEN + "Patient added successfully!" + RESET);
			displayQueueSummary();

		} catch (Exception e) {
			System.out.println(RED + "Error adding patient: " + e.getMessage() + RESET);
		}
	}

	/**
	 * Removes a patient from the queue by name
	 * Shows current queue before removal for user reference
	 */

	private void removePatient() {
		if (checkEmptyQueue()) return;
		
		patientManagement.printPatients();
		String name = getInput("Enter patient name to remove: ");
		
		if (patientManagement.removePatient(name)) {
			System.out.println(GREEN + "Patient removed successfully!" + RESET);
			displayQueueSummary();
		} else {
			System.out.println(RED + "Patient not found." + RESET);
		}
	}

	/**
	 * Adds timestamped notes to an existing patient's record
	 * Notes are appended to patient's note history for tracking purposes
	 */
	private void addPatientNotes() {
		if (checkEmptyQueue()) return;
		
		patientManagement.printPatients();
		String name = getInput("Enter patient name: ");
		String notes = getInput("Enter notes: ");
		
		Patient patient = patientManagement.findPatientByExactName(name);
		if (patient != null) {
			patient.addNote(notes); // Automatically adds timestamp
			System.out.println(GREEN + "Notes added successfully!" + RESET);
		} else {
			System.out.println(RED + "Patient not found." + RESET);
		}
	}

	/**
	 * Searches for patients by name (supports partial matching)
	 * Useful for finding patients when exact name spelling is uncertain
	 */

	private void searchPatientByName() {
		if (checkEmptyQueue()) return;
		
		String searchName = getInput("Enter name to search: ");
		patientManagement.printSearchResults(searchName);
	}

	/**
	 * Changes a patient's priority type with age validation
	 * Enforces business rule: Senior type requires age 75+
	 * Allows switching between Emergency, Senior, and Regular types
	 */
	private void changePatientType() {
		if (checkEmptyQueue()) return;
		
		patientManagement.printPatients();
		String name = getInput("Enter patient name: ");
		
		Patient patient = patientManagement.findPatientByExactName(name);
		if (patient == null) {
			System.out.println(RED + "Patient not found." + RESET);
			return;
		}

		String newType = getNewPatientType();
		if (newType == null) return;

		// Business rule: Age validation for Senior patient type
		if (newType.equals("Senior") && patient.getAge() < 75) {
			System.out.println(RED + "Patient must be 75+ for Senior type." + RESET);
			return;
		}

		if (getBooleanInput("Confirm change to " + newType + "? (y/n): ")) {
			patientManagement.changePatientType(name, newType);
			displayQueueSummary();
		}
	}

	/**
	 * Displays complete note history for a patient
	 * Shows all timestamped notes in chronological order
	 */

	private void viewPatientNoteHistory() {
		if (checkEmptyQueue()) return;
		
		patientManagement.printPatients();
		String name = getInput("Enter patient name: ");
		
		Patient patient = patientManagement.findPatientByExactName(name);
		if (patient != null) {
			System.out.println("\n" + CYAN + "=== NOTE HISTORY: " + name + " ===" + RESET);
			System.out.println(patient.getFormattedNotesHistory());
		} else {
			System.out.println(RED + "Patient not found." + RESET);
		}
	}

	/**
	 * Calls and removes the next patient from queue based on priority
	 * Priority order: Emergency > Senior > Regular (within each type: FIFO)
	 * Displays patient details and updates queue summary
	 */
	private void callNextPatient() {
		Patient patient = patientManagement.dequeuePatient();
		if (patient != null) {
			System.out.println("\n" + GREEN + "Patient Called: " + patient.getName() + RESET);
			System.out.println("Type: " + patient.getTypeIcon());
			System.out.println("Age: " + patient.getAge());
			displayQueueSummary();
		}
	}

	// ========== UTILITY METHODS ==========
	
	/**
	 * Checks if the patient queue is empty and displays appropriate message
	 * @return true if queue is empty, false otherwise
	 */
	private boolean checkEmptyQueue() {
		if (patientManagement.getTotalPatientCount() == 0) {
			System.out.println(RED + "No patients in queue." + RESET);
			return true;
		}
		return false;
	}

	/**
	 * Generic input method with validation for required and optional fields
	 * Handles empty input validation based on prompt context
	 * @param prompt The input prompt to display to user
	 * @return User input string (trimmed)
	 */
	private String getInput(String prompt) {
		String input;
		do {
			System.out.print(prompt);
			input = scanner.nextLine().trim();
			if (input.isEmpty() && prompt.contains("optional")) break;
			if (input.isEmpty()) System.out.println(RED + "Input cannot be empty." + RESET);
		} while (input.isEmpty() && !prompt.contains("optional"));
		return input;
	}

	/**
	 * Gets and validates integer input within specified range
	 * Handles InputMismatchException and range validation
	 * @param prompt Message to display to user
	 * @param min Minimum acceptable value (inclusive)
	 * @param max Maximum acceptable value (inclusive)
	 * @return Valid integer within specified range
	 */
	private int getIntInput(String prompt, int min, int max) {
		int value = -1;
		while (value < min || value > max) {
			try {
				System.out.print(prompt);
				value = scanner.nextInt();
				scanner.nextLine(); // Consume leftover newline
				if (value < min || value > max) {
					System.out.println(RED + "Enter value between " + min + " and " + max + RESET);
				}
			} catch (InputMismatchException e) {
				System.out.println(RED + "Invalid input!" + RESET);
				scanner.nextLine(); // Clear input buffer
			}
		}
		return value;
	}

	/**
	 * Gets and validates date input in yyyy-MM-dd format
	 * Validates date range (not future, not older than 150 years)
	 * @param prompt Message to display to user
	 * @return Valid LocalDate object
	 */
	private LocalDate getDateInput(String prompt) {
		LocalDate date = null;
		while (date == null) {
			try {
				System.out.print(prompt);
				String input = scanner.nextLine().trim();
				date = LocalDate.parse(input, DATE_FORMATTER);
				
				// Validate reasonable date range
				if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.now().minusYears(150))) {
					System.out.println(RED + "Invalid date range." + RESET);
					date = null;
				}
			} catch (DateTimeParseException e) {
				System.out.println(RED + "Invalid date format! Use yyyy-MM-dd" + RESET);
			}
		}
		return date;
	}

	/**
	 * Gets boolean input from user (y/n, yes/no)
	 * Case-insensitive, accepts y/n or yes/no variations
	 * @param prompt Question to display to user
	 * @return true for yes, false for no
	 */

	private boolean getBooleanInput(String prompt) {
		String input;
		do {
			System.out.print(prompt);
			input = scanner.nextLine().trim().toLowerCase();
		} while (!input.matches("y|n|yes|no"));
		return input.startsWith("y");
	}

	/**
	 * Displays patient type selection menu and gets user choice
	 * @return Selected patient type string, or null if cancelled
	 */
	private String getNewPatientType() {
		System.out.println("[1] Emergency [2] Senior [3] Regular [4] Cancel");
		int choice = getIntInput("Select type: ", 1, 4);
		
		switch (choice) {
			case 1: return "Emergency";
			case 2: return "Senior";
			case 3: return "Regular";
			default: return null; // Cancel operation
		}
	}

	/**
	 * Displays current queue summary with patient count
	 * Provides quick overview of remaining patients in system
	 */

	private void displayQueueSummary() {
		System.out.println(CYAN + "Queue: " + patientManagement.getTotalPatientCount() + 
						   " patients remaining" + RESET);
	}
}