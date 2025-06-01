/**
 * PatientManagement class - Manages patient queues and operations
 * Handles both emergency and regular patient queues with priority system
 * Emergency patients are always processed before regular patients
 */
public class PatientManagement {
    // Arrays to store emergency and regular patients separately
    private Patient[] emergencyPatients = new Patient[100];
    private Patient[] regularPatients = new Patient[100];

    // Counters to track number of patients in each queue
    private int emergencyCount = 0;
    private int regularCount = 0;

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
            System.out.println("Emergency patient " + patient.getName() + " added to queue.");
        } else {
            // Check if regular queue is full
            if (regularCount >= regularPatients.length) {
                throw new RuntimeException("Regular queue is full! Cannot add more regular patients.");
            }
            regularPatients[regularCount] = patient;
            regularCount++;
            System.out.println("Regular patient " + patient.getName() + " added to queue.");
        }
    }

    /**
     * Displays the name of the next patient to be called
     * Emergency patients have priority over regular patients
     */
    public void printNextPatient() {
        if (emergencyCount > 0) {
            System.out.println("Next patient in line: " + emergencyPatients[0].getName() + " (Emergency)");
        } else if (regularCount > 0) {
            System.out.println("Next patient in line: " + regularPatients[0].getName() + " (Regular)");
        } else {
            System.out.println("No patients in line.");
        }
    }

    /**
     * Displays all patients in both queues
     * Shows emergency queue first, then regular queue
     * Also displays who the next patient will be
     */
    public void printPatients() {
        System.out.println("\n=== PATIENT QUEUES ===");

        // Display emergency queue
        System.out.println("Emergency Queue:");
        if (emergencyCount == 0) {
            System.out.println("  No patients in emergency queue.");
        } else {
            for (int i = 0; i < emergencyCount; i++) {
                System.out.println("  " + (i + 1) + ". " + emergencyPatients[i].getName());
            }
        }

        // Display regular queue
        System.out.println("\nRegular Queue:");
        if (regularCount == 0) {
            System.out.println("  No patients in regular queue.");
        } else {
            for (int i = 0; i < regularCount; i++) {
                System.out.println("  " + (i + 1) + ". " + regularPatients[i].getName());
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

            System.out.println("Called emergency patient: " + patient.getName());
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

            System.out.println("Called regular patient: " + patient.getName());
            return patient;

        } else {
            System.out.println("No patients to call - queues are empty.");
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