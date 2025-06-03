import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class Controller {
    private PatientManagement patientManagement;
    private Scanner scanner;
    private BulkOperations bulkOperations;
    private PatientEditor patientEditor;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Controller() {
        patientManagement = new PatientManagement();
        scanner = new Scanner(System.in);
        bulkOperations = new BulkOperations(patientManagement, scanner);
        patientEditor = new PatientEditor(patientManagement, scanner);
    }

    public void run() {
        System.out.println(Colors.CYAN + Colors.BOLD + "Welcome to the Patient Management System!" + Colors.RESET);
        displaySystemInfo();

        int choice;
        do {
            printMenu();
            choice = getChoice();
            executeChoice(choice);
        } while (choice != 15);

        scanner.close();
        System.out.println(Colors.GREEN + "Thank you for using the Patient Management System. Goodbye!" + Colors.RESET);
    }

    private void displaySystemInfo() {
        System.out.println(Colors.WHITE + "System Capacity Limits:" + Colors.RESET);
        System.out.println(Colors.RED + "  Emergency: " + QueueConfig.MAX_EMERGENCY_CAPACITY + Colors.RESET);
        System.out.println(Colors.ORANGE + "  Senior: " + QueueConfig.MAX_SENIOR_CAPACITY + Colors.RESET);
        System.out.println(Colors.BLUE + "  Regular: " + QueueConfig.MAX_REGULAR_CAPACITY + Colors.RESET);
        System.out.println(Colors.CYAN + "  Total: " + QueueConfig.MAX_TOTAL_CAPACITY + Colors.RESET);
    }

    private void printMenu() {
        System.out.println("\n" + Colors.YELLOW + Colors.BOLD + "=== Patient Management System ===" + Colors.RESET);
        
        // Display capacity warnings
        displayCapacityWarnings();
        
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
            {"11", "Edit Patient Information", Colors.PURPLE},
            {"12", "Bulk Operations", Colors.RED + Colors.BOLD},
            {"13", "Queue Management", Colors.ORANGE},
            {"14", "System Status", Colors.CYAN},
            {"15", "Exit", Colors.RED + Colors.BOLD}
        };
        
        for (String[] item : menuItems) {
            System.out.println(item[2] + "[" + item[0] + "] " + item[1] + Colors.RESET);
        }
        System.out.println(Colors.YELLOW + Colors.BOLD + "==============================" + Colors.RESET);
    }

    private void displayCapacityWarnings() {
        int emergencyCount = patientManagement.getEmergencyCount();
        int seniorCount = patientManagement.getSeniorCount();
        int regularCount = patientManagement.getRegularCount();
        int totalCount = patientManagement.getTotalPatientCount();
        
        boolean hasWarnings = false;
        
        if (QueueConfig.isNearCapacity(emergencyCount, QueueConfig.MAX_EMERGENCY_CAPACITY)) {
            System.out.println(Colors.RED + "⚠ Emergency Queue: " + emergencyCount + "/" + 
                              QueueConfig.MAX_EMERGENCY_CAPACITY + " - " + 
                              QueueConfig.getCapacityStatus(emergencyCount, QueueConfig.MAX_EMERGENCY_CAPACITY) + Colors.RESET);
            hasWarnings = true;
        }
        
        if (QueueConfig.isNearCapacity(seniorCount, QueueConfig.MAX_SENIOR_CAPACITY)) {
            System.out.println(Colors.ORANGE + "⚠ Senior Queue: " + seniorCount + "/" + 
                              QueueConfig.MAX_SENIOR_CAPACITY + " - " + 
                              QueueConfig.getCapacityStatus(seniorCount, QueueConfig.MAX_SENIOR_CAPACITY) + Colors.RESET);
            hasWarnings = true;
        }
        
        if (QueueConfig.isNearCapacity(regularCount, QueueConfig.MAX_REGULAR_CAPACITY)) {
            System.out.println(Colors.BLUE + "⚠ Regular Queue: " + regularCount + "/" + 
                              QueueConfig.MAX_REGULAR_CAPACITY + " - " + 
                              QueueConfig.getCapacityStatus(regularCount, QueueConfig.MAX_REGULAR_CAPACITY) + Colors.RESET);
            hasWarnings = true;
        }
        
        if (QueueConfig.isNearCapacity(totalCount, QueueConfig.MAX_TOTAL_CAPACITY)) {
            System.out.println(Colors.RED + Colors.BOLD + "⚠ TOTAL CAPACITY: " + totalCount + "/" + 
                              QueueConfig.MAX_TOTAL_CAPACITY + " - " + 
                              QueueConfig.getCapacityStatus(totalCount, QueueConfig.MAX_TOTAL_CAPACITY) + Colors.RESET);
            hasWarnings = true;
        }
        
        if (hasWarnings) {
            System.out.println();
        }
    }

    private int getChoice() {
        return getValidatedInput("Enter choice (1-15): ", Integer::parseInt, 
            choice -> choice >= 1 && choice <= 15, "Please enter a number between 1 and 15.");
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
                case 11: patientEditor.editPatientInformation(); break;
                case 12: bulkOperations.showBulkOperationsMenu(); break;
                case 13: showQueueManagement(); break;
                case 14: showSystemStatus(); break;
                case 15: System.out.println(Colors.YELLOW + "Exiting..." + Colors.RESET); break;
            }
        } catch (Exception e) {
            System.out.println(Colors.RED + "Error: " + e.getMessage() + Colors.RESET);
        }
    }

    private void addPatient() {
        try {
            System.out.println("\n" + Colors.GREEN + "=== ADD NEW PATIENT ===" + Colors.RESET);

            // Check total capacity first
            if (QueueConfig.isAtCapacity(patientManagement.getTotalPatientCount(), QueueConfig.MAX_TOTAL_CAPACITY)) {
                System.out.println(Colors.RED + Colors.BOLD + "SYSTEM AT MAXIMUM CAPACITY!" + Colors.RESET);
                System.out.println(Colors.RED + "Cannot add more patients. Current: " + 
                                  patientManagement.getTotalPatientCount() + "/" + QueueConfig.MAX_TOTAL_CAPACITY + Colors.RESET);
                return;
            }

            String name = getInput("Enter patient name: ");
            int age = getValidatedInput("Enter patient age: ", Integer::parseInt, 
                a -> a >= 0 && a <= 150, "Enter age between 0 and 150.");
            LocalDate birthday = getDateInput("Enter birthday (yyyy-MM-dd): ");
            boolean isEmergency = getBooleanInput("Is this an emergency patient? (y/n): ");
            String notes = getInput("Enter optional notes (press Enter to skip): ");

            // Determine patient type and check capacity
            PatientType targetType;
            if (isEmergency) {
                targetType = PatientType.EMERGENCY;
                if (QueueConfig.isAtCapacity(patientManagement.getEmergencyCount(), QueueConfig.MAX_EMERGENCY_CAPACITY)) {
                    System.out.println(Colors.RED + "Emergency queue is at capacity!" + Colors.RESET);
                    if (!getBooleanInput("Add as Senior/Regular instead? (y/n): ")) {
                        return;
                    }
                    isEmergency = false;
                    targetType = age >= 75 ? PatientType.SENIOR : PatientType.REGULAR;
                }
            } else {
                targetType = age >= 75 ? PatientType.SENIOR : PatientType.REGULAR;
            }

            // Check capacity for determined type
            if (targetType == PatientType.SENIOR && 
                QueueConfig.isAtCapacity(patientManagement.getSeniorCount(), QueueConfig.MAX_SENIOR_CAPACITY)) {
                System.out.println(Colors.ORANGE + "Senior queue is at capacity!" + Colors.RESET);
                if (getBooleanInput("Add as Regular instead? (y/n): ")) {
                    targetType = PatientType.REGULAR;
                } else {
                    return;
                }
            }

            if (targetType == PatientType.REGULAR && 
                QueueConfig.isAtCapacity(patientManagement.getRegularCount(), QueueConfig.MAX_REGULAR_CAPACITY)) {
                System.out.println(Colors.BLUE + "Regular queue is at capacity!" + Colors.RESET);
                System.out.println(Colors.RED + "Cannot add patient - all applicable queues are full." + Colors.RESET);
                return;
            }

            Patient patient = Patient.createByType(name, age, birthday, notes, isEmergency);
            patientManagement.queuePatient(patient);
            System.out.println(Colors.GREEN + "Patient added successfully!" + Colors.RESET);
            displayQueueSummary();
            displayCapacityWarnings();

        } catch (Exception e) {
            System.out.println(Colors.RED + "Error adding patient: " + e.getMessage() + Colors.RESET);
        }
    }

    private void showQueueManagement() {
        System.out.println("\n" + Colors.ORANGE + Colors.BOLD + "=== QUEUE MANAGEMENT ===" + Colors.RESET);
        String[][] menuItems = {
            {"1", "View Queue Capacities", Colors.CYAN},
            {"2", "Optimize Queue Distribution", Colors.YELLOW},
            {"3", "Emergency Override", Colors.RED},
            {"4", "Queue Health Check", Colors.GREEN},
            {"5", "Back to Main Menu", Colors.WHITE}
        };
        
        for (String[] item : menuItems) {
            System.out.println(item[2] + "[" + item[0] + "] " + item[1] + Colors.RESET);
        }
        System.out.println(Colors.ORANGE + Colors.BOLD + "==================" + Colors.RESET);
        
        int choice = getValidatedInput("Enter choice (1-5): ", Integer::parseInt,
            c -> c >= 1 && c <= 5, "Please enter a number between 1 and 5.");
        
        switch (choice) {
            case 1: viewQueueCapacities(); break;
            case 2: optimizeQueueDistribution(); break;
            case 3: emergencyOverride(); break;
            case 4: queueHealthCheck(); break;
            case 5: return;
        }
    }

    private void viewQueueCapacities() {
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + "=== QUEUE CAPACITIES ===" + Colors.RESET);
        
        int emergencyCount = patientManagement.getEmergencyCount();
        int seniorCount = patientManagement.getSeniorCount();
        int regularCount = patientManagement.getRegularCount();
        int totalCount = patientManagement.getTotalPatientCount();
        
        System.out.println(Colors.RED + "Emergency Queue: " + emergencyCount + "/" + 
                          QueueConfig.MAX_EMERGENCY_CAPACITY + " - " + 
                          QueueConfig.getCapacityStatus(emergencyCount, QueueConfig.MAX_EMERGENCY_CAPACITY) + Colors.RESET);
        
        System.out.println(Colors.ORANGE + "Senior Queue: " + seniorCount + "/" + 
                          QueueConfig.MAX_SENIOR_CAPACITY + " - " + 
                          QueueConfig.getCapacityStatus(seniorCount, QueueConfig.MAX_SENIOR_CAPACITY) + Colors.RESET);
        
        System.out.println(Colors.BLUE + "Regular Queue: " + regularCount + "/" + 
                          QueueConfig.MAX_REGULAR_CAPACITY + " - " + 
                          QueueConfig.getCapacityStatus(regularCount, QueueConfig.MAX_REGULAR_CAPACITY) + Colors.RESET);
        
        System.out.println(Colors.CYAN + Colors.BOLD + "Total System: " + totalCount + "/" + 
                          QueueConfig.MAX_TOTAL_CAPACITY + " - " + 
                          QueueConfig.getCapacityStatus(totalCount, QueueConfig.MAX_TOTAL_CAPACITY) + Colors.RESET);
        
        System.out.println("\n" + Colors.WHITE + "Available Capacity:" + Colors.RESET);
        System.out.println(Colors.GREEN + "  Emergency: " + (QueueConfig.MAX_EMERGENCY_CAPACITY - emergencyCount) + " slots" + Colors.RESET);
        System.out.println(Colors.GREEN + "  Senior: " + (QueueConfig.MAX_SENIOR_CAPACITY - seniorCount) + " slots" + Colors.RESET);
        System.out.println(Colors.GREEN + "  Regular: " + (QueueConfig.MAX_REGULAR_CAPACITY - regularCount) + " slots" + Colors.RESET);
        System.out.println(Colors.GREEN + "  Total: " + (QueueConfig.MAX_TOTAL_CAPACITY - totalCount) + " slots" + Colors.RESET);
    }

    private void optimizeQueueDistribution() {
        System.out.println("\n" + Colors.YELLOW + Colors.BOLD + "=== QUEUE OPTIMIZATION ===" + Colors.RESET);
        System.out.println(Colors.YELLOW + "This feature analyzes current queue distribution and suggests improvements." + Colors.RESET);
        
        int emergencyCount = patientManagement.getEmergencyCount();
        int seniorCount = patientManagement.getSeniorCount();
        int regularCount = patientManagement.getRegularCount();
        
        boolean hasOptimizations = false;
        
        if (emergencyCount == 0 && (seniorCount > 0 || regularCount > 0)) {
            System.out.println(Colors.GREEN + "✓ Emergency queue is clear - good for urgent cases" + Colors.RESET);
        }
        
        if (QueueConfig.isNearCapacity(seniorCount, QueueConfig.MAX_SENIOR_CAPACITY)) {
            System.out.println(Colors.ORANGE + "⚠ Senior queue is near capacity - consider prioritizing senior patients" + Colors.RESET);
            hasOptimizations = true;
        }
        
        if (QueueConfig.isNearCapacity(regularCount, QueueConfig.MAX_REGULAR_CAPACITY)) {
            System.out.println(Colors.BLUE + "⚠ Regular queue is near capacity - consider calling more regular patients" + Colors.RESET);
            hasOptimizations = true;
        }
        
        if (!hasOptimizations) {
            System.out.println(Colors.GREEN + "✓ Queue distribution is optimal" + Colors.RESET);
        }
    }

    private void emergencyOverride() {
        System.out.println("\n" + Colors.RED + Colors.BOLD + "=== EMERGENCY OVERRIDE ===" + Colors.RESET);
        System.out.println(Colors.RED + "WARNING: This allows adding emergency patients even at capacity!" + Colors.RESET);
        
        if (!getBooleanInput("Are you sure you want to proceed? (y/n): ")) {
            return;
        }
        
        System.out.println(Colors.YELLOW + "Emergency override activated. Adding emergency patient..." + Colors.RESET);
        
        String name = getInput("Enter emergency patient name: ");
        int age = getValidatedInput("Enter patient age: ", Integer::parseInt, 
            a -> a >= 0 && a <= 150, "Enter age between 0 and 150.");
        LocalDate birthday = getDateInput("Enter birthday (yyyy-MM-dd): ");
        String notes = getInput("Enter emergency notes: ");
        
        Patient patient = Patient.createEmergency(name, age, birthday, notes);
        patientManagement.queuePatient(patient);
        System.out.println(Colors.GREEN + "Emergency patient added with override!" + Colors.RESET);
        displayQueueSummary();
    }

    private void queueHealthCheck() {
        System.out.println("\n" + Colors.GREEN + Colors.BOLD + "=== QUEUE HEALTH CHECK ===" + Colors.RESET);
        
        int totalCount = patientManagement.getTotalPatientCount();
        if (totalCount == 0) {
            System.out.println(Colors.GREEN + "✓ All queues are empty - system ready for patients" + Colors.RESET);
            return;
        }
        
        System.out.println(Colors.CYAN + "Analyzing queue health..." + Colors.RESET);
        
        // Check for capacity issues
        boolean healthy = true;
        int emergencyCount = patientManagement.getEmergencyCount();
        int seniorCount = patientManagement.getSeniorCount();
        int regularCount = patientManagement.getRegularCount();
        
        if (QueueConfig.isAtCapacity(emergencyCount, QueueConfig.MAX_EMERGENCY_CAPACITY)) {
            System.out.println(Colors.RED + "✗ Emergency queue at capacity" + Colors.RESET);
            healthy = false;
        } else if (QueueConfig.isNearCapacity(emergencyCount, QueueConfig.MAX_EMERGENCY_CAPACITY)) {
            System.out.println(Colors.YELLOW + "⚠ Emergency queue near capacity" + Colors.RESET);
        } else {
            System.out.println(Colors.GREEN + "✓ Emergency queue healthy" + Colors.RESET);
        }
        
        if (QueueConfig.isAtCapacity(seniorCount, QueueConfig.MAX_SENIOR_CAPACITY)) {
            System.out.println(Colors.RED + "✗ Senior queue at capacity" + Colors.RESET);
            healthy = false;
        } else if (QueueConfig.isNearCapacity(seniorCount, QueueConfig.MAX_SENIOR_CAPACITY)) {
            System.out.println(Colors.YELLOW + "⚠ Senior queue near capacity" + Colors.RESET);
        } else {
            System.out.println(Colors.GREEN + "✓ Senior queue healthy" + Colors.RESET);
        }
        
        if (QueueConfig.isAtCapacity(regularCount, QueueConfig.MAX_REGULAR_CAPACITY)) {
            System.out.println(Colors.RED + "✗ Regular queue at capacity" + Colors.RESET);
            healthy = false;
        } else if (QueueConfig.isNearCapacity(regularCount, QueueConfig.MAX_REGULAR_CAPACITY)) {
            System.out.println(Colors.YELLOW + "⚠ Regular queue near capacity" + Colors.RESET);
        } else {
            System.out.println(Colors.GREEN + "✓ Regular queue healthy" + Colors.RESET);
        }
        
        if (QueueConfig.isAtCapacity(totalCount, QueueConfig.MAX_TOTAL_CAPACITY)) {
            System.out.println(Colors.RED + Colors.BOLD + "✗ SYSTEM AT MAXIMUM CAPACITY" + Colors.RESET);
            healthy = false;
        }
        
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + "Overall Status: " + 
                          (healthy ? Colors.GREEN + "HEALTHY" : Colors.RED + "NEEDS ATTENTION") + Colors.RESET);
    }

    private void showSystemStatus() {
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + "=== SYSTEM STATUS ===" + Colors.RESET);
        
        // Current time and uptime info
        System.out.println(Colors.WHITE + "System Information:" + Colors.RESET);
        System.out.println(Colors.WHITE + "  Current Time: " + java.time.LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + Colors.RESET);
        
        // Queue status
        System.out.println(Colors.WHITE + "\nQueue Status:" + Colors.RESET);
        viewQueueCapacities();
        
        // Statistics
        System.out.println(Colors.WHITE + "\nDaily Statistics:" + Colors.RESET);
        System.out.println(Colors.GREEN + "  Total Patients Today: " + patientManagement.getTotalPatientsToday() + Colors.RESET);
        System.out.println(Colors.RED + "  Total Emergencies Today: " + patientManagement.getTotalEmergenciesToday() + Colors.RESET);
        
        // System limits
        System.out.println(Colors.WHITE + "\nSystem Configuration:" + Colors.RESET);
        System.out.println(Colors.WHITE + "  Max Emergency Capacity: " + QueueConfig.MAX_EMERGENCY_CAPACITY + Colors.RESET);
        System.out.println(Colors.WHITE + "  Max Senior Capacity: " + QueueConfig.MAX_SENIOR_CAPACITY + Colors.RESET);
        System.out.println(Colors.WHITE + "  Max Regular Capacity: " + QueueConfig.MAX_REGULAR_CAPACITY + Colors.RESET);
        System.out.println(Colors.WHITE + "  Max Total Capacity: " + QueueConfig.MAX_TOTAL_CAPACITY + Colors.RESET);
        System.out.println(Colors.WHITE + "  Warning Threshold: " + (QueueConfig.WARNING_THRESHOLD * 100) + "%" + Colors.RESET);
        System.out.println(Colors.WHITE + "  Critical Threshold: " + (QueueConfig.CRITICAL_THRESHOLD * 100) + "%" + Colors.RESET);
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

        // Check capacity for new type
        PatientType targetType = PatientType.valueOf(newType.toUpperCase());
        int currentCount = patientManagement.getCountByType(targetType);
        int maxCapacity = QueueConfig.getMaxCapacityForType(targetType);
        
        if (patient.getType() != targetType && QueueConfig.isAtCapacity(currentCount, maxCapacity)) {
            System.out.println(Colors.RED + newType + " queue is at capacity!" + Colors.RESET);
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
        int totalCount = patientManagement.getTotalPatientCount();
        System.out.println(Colors.CYAN + "Queue: " + totalCount + "/" + QueueConfig.MAX_TOTAL_CAPACITY + 
                          " patients (" + QueueConfig.getCapacityStatus(totalCount, QueueConfig.MAX_TOTAL_CAPACITY) + ")" + Colors.RESET);
    }
}