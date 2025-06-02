/**
 * PatientManagement class - Manages patient queues and operations
 * Handles both emergency and regular patient queues with priority system
 * Emergency patients are always processed before regular patients
 * Now includes remove patient functionality and colored output
 */
public class PatientManagement {
    // Arrays to store emergency and regular patients separately
    private Patient[] emergencyPatients = new Patient[100];
    private Patient[] regularPatients = new Patient[100];

    // Counters to track number of patients in each queue
    private int emergencyCount = 0;
    private int regularCount = 0;

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
     * Adds a patient to the appropriate queue based on patient type
     * Emergency patients go to emergency queue, others go to regular queue
     *
     * @param patient Patient object to be added to queue
     * @throws RuntimeException if queue is full (100 patients limit)
     */
    public void queuePatient(Patient patient) {
        if (patient.getPatientType().equalsIgnoreCase("emergency")) {
            // Check if emergency queue is full
            if (emergencyCount >= emergencyPatients.length) {
                throw new RuntimeException("Emergency queue is full! Cannot add more emergency patients.");
            }
            emergencyPatients[emergencyCount] = patient;
            emergencyCount++;
            System.out.println(RED + "Emergency patient " + patient.getName() + " added to queue." + RESET);
        } else {
            // Check if regular queue is full
            if (regularCount >= regularPatients.length) {
                throw new RuntimeException("Regular queue is full! Cannot add more regular patients.");
            }
            regularPatients[regularCount] = patient;
            regularCount++;
            System.out.println(BLUE + "Regular patient " + patient.getName() + " added to queue." + RESET);
        }
    }

    /**
     * Removes a patient from either queue by name
     * Searches emergency queue first, then regular queue
     *
     * @param patientName Name of patient to remove
     * @return true if patient was found and removed, false otherwise
     */
    public boolean removePatient(String patientName) {
        // First check emergency queue
        for (int i = 0; i < emergencyCount; i++) {
            if (emergencyPatients[i].getName().equalsIgnoreCase(patientName)) {
                // Shift all patients after this one forward
                for (int j = i; j < emergencyCount - 1; j++) {
                    emergencyPatients[j] = emergencyPatients[j + 1];
                }
                // Clear the last position and decrement counter
                emergencyPatients[emergencyCount - 1] = null;
                emergencyCount--;
                return true;
            }
        }

        // Then check regular queue
        for (int i = 0; i < regularCount; i++) {
            if (regularPatients[i].getName().equalsIgnoreCase(patientName)) {
                // Shift all patients after this one forward
                for (int j = i; j < regularCount - 1; j++) {
                    regularPatients[j] = regularPatients[j + 1];
                }
                // Clear the last position and decrement counter
                regularPatients[regularCount - 1] = null;
                regularCount--;
                return true;
            }
        }

        // Patient not found in either queue
        return false;
    }

    /**
     * Displays the name of the next patient to be called
     * Emergency patients have priority over regular patients
     */
    public void printNextPatient() {
        if (emergencyCount > 0) {
            System.out.println(RED + "Next patient in line: " + emergencyPatients[0].getName() + " (Emergency)" + RESET);
        } else if (regularCount > 0) {
            System.out.println(BLUE + "Next patient in line: " + regularPatients[0].getName() + " (Regular)" + RESET);
        } else {
            System.out.println(YELLOW + "No patients in line." + RESET);
        }
    }

    /**
     * Displays all patients in both queues with colors
     * Shows emergency queue first, then regular queue
     * Also displays who the next patient will be
     */
    public void printPatients() {
        System.out.println("\n" + CYAN + BOLD + "=== PATIENT QUEUES ===" + RESET);

        // Display emergency queue
        System.out.println(RED + BOLD + "Emergency Queue:" + RESET);
        if (emergencyCount == 0) {
            System.out.println(YELLOW + "  No patients in emergency queue." + RESET);
        } else {
            for (int i = 0; i < emergencyCount; i++) {
                System.out.println(RED + "  " + (i + 1) + ". " + emergencyPatients[i].getName() + RESET);
            }
        }

        // Display regular queue
        System.out.println(BLUE + BOLD + "\nRegular Queue:" + RESET);
        if (regularCount == 0) {
            System.out.println(YELLOW + "  No patients in regular queue." + RESET);
        } else {
            for (int i = 0; i < regularCount; i++) {
                System.out.println(BLUE + "  " + (i + 1) + ". " + regularPatients[i].getName() + RESET);
            }
        }

        System.out.println(); // Empty line for readability
        printNextPatient();
    }

    /**
     * Removes and returns the next patient from the queue
     * Emergency patients are processed first (FIFO within each priority)
     *
     * @return Patient object that was removed from queue, null if no patients
     */
    public Patient dequeuePatient() {
        // Process emergency patients first
        if (emergencyCount > 0) {
            Patient patient = emergencyPatients[0];

            // Shift all remaining emergency patients forward
            for (int i = 1; i < emergencyCount; i++) {
                emergencyPatients[i - 1] = emergencyPatients[i];
            }

            // Clear the last position and decrement counter
            emergencyPatients[emergencyCount - 1] = null;
            emergencyCount--;

            System.out.println(GREEN + "Called emergency patient: " + patient.getName() + RESET);
            return patient;

        } else if (regularCount > 0) {
            Patient patient = regularPatients[0];

            // Shift all remaining regular patients forward
            for (int i = 1; i < regularCount; i++) {
                regularPatients[i - 1] = regularPatients[i];
            }

            // Clear the last position and decrement counter
            regularPatients[regularCount - 1] = null;
            regularCount--;

            System.out.println(GREEN + "Called regular patient: " + patient.getName() + RESET);
            return patient;

        } else {
            System.out.println(YELLOW + "No patients to call - queues are empty." + RESET);
            return null;
        }
    }

    /**
     * Returns the total number of patients in both queues
     *
     * @return Total number of patients waiting
     */
    public int getTotalPatientCount() {
        return emergencyCount + regularCount;
    }

    /**
     * Returns the number of emergency patients waiting
     *
     * @return Number of emergency patients in queue
     */
    public int getEmergencyCount() {
        return emergencyCount;
    }

    /**
     * Returns the number of regular patients waiting
     *
     * @return Number of regular patients in queue
     */
    public int getRegularCount() {
        return regularCount;
    }
}