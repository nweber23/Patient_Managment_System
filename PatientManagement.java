/**
 * PatientManagement class - Manages patient queues and operations
 * Handles emergency, senior, and regular patient queues with priority system
 * Emergency patients are processed first, then seniors, then regular patients
 * Now includes remove patient functionality, statistics, and colored output
 */
public class PatientManagement {
    // Arrays to store different types of patients separately
    private Patient[] emergencyPatients = new Patient[100];
    private Patient[] seniorPatients = new Patient[100];
    private Patient[] regularPatients = new Patient[100];

    // Counters to track number of patients in each queue
    private int emergencyCount = 0;
    private int seniorCount = 0;
    private int regularCount = 0;

    // Statistics tracking
    private int totalPatientsToday = 0;
    private int totalEmergenciesToday = 0;

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
     * Adds a patient to the appropriate queue based on patient type and age
     * Emergency patients go to emergency queue, seniors (75+) to senior queue, others to regular queue
     *
     * @param patient Patient object to be added to queue
     * @throws RuntimeException if queue is full (100 patients limit per type)
     */
    public void queuePatient(Patient patient) {
        totalPatientsToday++; // Increment total patient counter
        
        if (patient.getPatientType().equalsIgnoreCase("emergency")) {
            // Check if emergency queue is full
            if (emergencyCount >= emergencyPatients.length) {
                throw new RuntimeException("Emergency queue is full! Cannot add more emergency patients.");
            }
            emergencyPatients[emergencyCount] = patient;
            emergencyCount++;
            totalEmergenciesToday++; // Increment emergency counter
            System.out.println(RED + "🚨 Emergency patient " + patient.getName() + " added to queue." + RESET);
        } else if (patient.isSenior()) {
            // Check if senior queue is full
            if (seniorCount >= seniorPatients.length) {
                throw new RuntimeException("Senior queue is full! Cannot add more senior patients.");
            }
            seniorPatients[seniorCount] = patient;
            seniorCount++;
            System.out.println(ORANGE + "👴 Senior patient " + patient.getName() + " added to queue." + RESET);
        } else {
            // Check if regular queue is full
            if (regularCount >= regularPatients.length) {
                throw new RuntimeException("Regular queue is full! Cannot add more regular patients.");
            }
            regularPatients[regularCount] = patient;
            regularCount++;
            System.out.println(BLUE + "👥 Regular patient " + patient.getName() + " added to queue." + RESET);
        }
    }

    /**
     * Removes a patient from any queue by name
     * Searches emergency queue first, then senior queue, then regular queue
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
                emergencyPatients[emergencyCount - 1] = null;
                emergencyCount--;
                return true;
            }
        }

        // Then check senior queue
        for (int i = 0; i < seniorCount; i++) {
            if (seniorPatients[i].getName().equalsIgnoreCase(patientName)) {
                // Shift all patients after this one forward
                for (int j = i; j < seniorCount - 1; j++) {
                    seniorPatients[j] = seniorPatients[j + 1];
                }
                seniorPatients[seniorCount - 1] = null;
                seniorCount--;
                return true;
            }
        }

        // Finally check regular queue
        for (int i = 0; i < regularCount; i++) {
            if (regularPatients[i].getName().equalsIgnoreCase(patientName)) {
                // Shift all patients after this one forward
                for (int j = i; j < regularCount - 1; j++) {
                    regularPatients[j] = regularPatients[j + 1];
                }
                regularPatients[regularCount - 1] = null;
                regularCount--;
                return true;
            }
        }

        // Patient not found in any queue
        return false;
    }

    /**
     * Displays the name of the next patient to be called
     * Emergency patients have highest priority, then seniors, then regular patients
     */
    public void printNextPatient() {
        if (emergencyCount > 0) {
            Patient nextPatient = emergencyPatients[0];
            System.out.println(RED + "Next patient: " + nextPatient.getTypeIcon() + " " + nextPatient.getName() + RESET);
        } else if (seniorCount > 0) {
            Patient nextPatient = seniorPatients[0];
            System.out.println(ORANGE + "Next patient: " + nextPatient.getTypeIcon() + " " + nextPatient.getName() + RESET);
        } else if (regularCount > 0) {
            Patient nextPatient = regularPatients[0];
            System.out.println(BLUE + "Next patient: " + nextPatient.getTypeIcon() + " " + nextPatient.getName() + RESET);
        } else {
            System.out.println(YELLOW + "No patients in line." + RESET);
        }
    }

    /**
     * Displays all patients in all queues with colors and icons
     * Shows emergency queue first, then senior queue, then regular queue
     * Also displays who the next patient will be
     */
    public void printPatients() {
        System.out.println("\n" + CYAN + BOLD + "=== PATIENT QUEUES ===" + RESET);

        // Display emergency queue
        System.out.println(RED + BOLD + "🚨 Emergency Queue:" + RESET);
        if (emergencyCount == 0) {
            System.out.println(YELLOW + "  No emergency patients." + RESET);
        } else {
            for (int i = 0; i < emergencyCount; i++) {
                Patient patient = emergencyPatients[i];
                String notesDisplay = patient.getNotes().isEmpty() ? "" : " - " + patient.getNotes();
                System.out.println(RED + "  " + (i + 1) + ". " + patient.getName() + " (Age: " + patient.getAge() + ")" + notesDisplay + RESET);
            }
        }

        // Display senior queue
        System.out.println(ORANGE + BOLD + "\n👴 Senior Queue (75+ years):" + RESET);
        if (seniorCount == 0) {
            System.out.println(YELLOW + "  No senior patients." + RESET);
        } else {
            for (int i = 0; i < seniorCount; i++) {
                Patient patient = seniorPatients[i];
                String notesDisplay = patient.getNotes().isEmpty() ? "" : " - " + patient.getNotes();
                System.out.println(ORANGE + "  " + (i + 1) + ". " + patient.getName() + " (Age: " + patient.getAge() + ")" + notesDisplay + RESET);
            }
        }

        // Display regular queue
        System.out.println(BLUE + BOLD + "\n👥 Regular Queue:" + RESET);
        if (regularCount == 0) {
            System.out.println(YELLOW + "  No regular patients." + RESET);
        } else {
            for (int i = 0; i < regularCount; i++) {
                Patient patient = regularPatients[i];
                String notesDisplay = patient.getNotes().isEmpty() ? "" : " - " + patient.getNotes();
                System.out.println(BLUE + "  " + (i + 1) + ". " + patient.getName() + " (Age: " + patient.getAge() + ")" + notesDisplay + RESET);
            }
        }

        System.out.println(); // Empty line for readability
        printNextPatient();
    }

    /**
     * Displays comprehensive statistics about patients
     */
    public void printStatistics() {
        System.out.println("\n" + CYAN + BOLD + "=== PATIENT STATISTICS ===" + RESET);
        
        // Current queue counts
        System.out.println(WHITE + "Current Patients in Queue:" + RESET);
        System.out.println(RED + "  🚨 Emergency: " + emergencyCount + RESET);
        System.out.println(ORANGE + "  👴 Senior: " + seniorCount + RESET);
        System.out.println(BLUE + "  👥 Regular: " + regularCount + RESET);
        System.out.println(CYAN + "  Total Waiting: " + getTotalPatientCount() + RESET);
        
        // Daily statistics
        System.out.println(WHITE + "\nToday's Statistics:" + RESET);
        System.out.println(GREEN + "  Total Patients Today: " + totalPatientsToday + RESET);
        System.out.println(RED + "  Emergencies Today: " + totalEmergenciesToday + RESET);
        
        // Average age calculation
        double averageAge = calculateAverageAge();
        if (averageAge > 0) {
            System.out.println(PURPLE + "  Average Age: " + String.format("%.1f", averageAge) + " years" + RESET);
        } else {
            System.out.println(YELLOW + "  Average Age: No patients to calculate" + RESET);
        }
        
        // Additional insights
        if (getTotalPatientCount() > 0) {
            double emergencyPercentage = (double) emergencyCount / getTotalPatientCount() * 100;
            double seniorPercentage = (double) seniorCount / getTotalPatientCount() * 100;
            System.out.println(WHITE + "\nQueue Composition:" + RESET);
            System.out.println(RED + "  Emergency: " + String.format("%.1f", emergencyPercentage) + "%" + RESET);
            System.out.println(ORANGE + "  Senior: " + String.format("%.1f", seniorPercentage) + "%" + RESET);
            System.out.println(BLUE + "  Regular: " + String.format("%.1f", 100 - emergencyPercentage - seniorPercentage) + "%" + RESET);
        }
    }

    /**
     * Calculates the average age of all patients currently in queues
     *
     * @return Average age as double, or 0 if no patients
     */
    private double calculateAverageAge() {
        int totalAge = 0;
        int totalPatients = getTotalPatientCount();
        
        if (totalPatients == 0) {
            return 0;
        }
        
        // Sum ages from emergency queue
        for (int i = 0; i < emergencyCount; i++) {
            totalAge += emergencyPatients[i].getAge();
        }
        
        // Sum ages from senior queue
        for (int i = 0; i < seniorCount; i++) {
            totalAge += seniorPatients[i].getAge();
        }
        
        // Sum ages from regular queue
        for (int i = 0; i < regularCount; i++) {
            totalAge += regularPatients[i].getAge();
        }
        
        return (double) totalAge / totalPatients;
    }

    /**
     * Removes and returns the next patient from the queue
     * Emergency patients are processed first, then seniors, then regular patients (FIFO within each priority)
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
            emergencyPatients[emergencyCount - 1] = null;
            emergencyCount--;

            System.out.println(GREEN + "Called emergency patient: " + patient.getName() + RESET);
            return patient;

        } else if (seniorCount > 0) {
            Patient patient = seniorPatients[0];

            // Shift all remaining senior patients forward
            for (int i = 1; i < seniorCount; i++) {
                seniorPatients[i - 1] = seniorPatients[i];
            }
            seniorPatients[seniorCount - 1] = null;
            seniorCount--;

            System.out.println(GREEN + "Called senior patient: " + patient.getName() + RESET);
            return patient;

        } else if (regularCount > 0) {
            Patient patient = regularPatients[0];

            // Shift all remaining regular patients forward
            for (int i = 1; i < regularCount; i++) {
                regularPatients[i - 1] = regularPatients[i];
            }
            regularPatients[regularCount - 1] = null;
            regularCount--;

            System.out.println(GREEN + "Called regular patient: " + patient.getName() + RESET);
            return patient;

        } else {
            System.out.println(YELLOW + "No patients to call - all queues are empty." + RESET);
            return null;
        }
    }

    /**
     * Returns the total number of patients in all queues
     *
     * @return Total number of patients waiting
     */
    public int getTotalPatientCount() {
        return emergencyCount + seniorCount + regularCount;
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
     * Returns the number of senior patients waiting
     *
     * @return Number of senior patients in queue
     */
    public int getSeniorCount() {
        return seniorCount;
    }

    /**
     * Returns the number of regular patients waiting
     *
     * @return Number of regular patients in queue
     */
    public int getRegularCount() {
        return regularCount;
    }

    /**
     * Returns the total number of patients processed today
     *
     * @return Total patients today
     */
    public int getTotalPatientsToday() {
        return totalPatientsToday;
    }

    /**
     * Returns the total number of emergency patients processed today
     *
     * @return Total emergency patients today
     */
    public int getTotalEmergenciesToday() {
        return totalEmergenciesToday;
    }
}