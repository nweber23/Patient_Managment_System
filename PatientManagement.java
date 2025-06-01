public class PatientManagement {
    private Patient[] emergencyPatients = new Patient[100];
    private Patient[] regularPatients = new Patient[100];
    private int emergencyCount = 0;
    private int regularCount = 0;

    public void queuePatient(Patient patient) {
        if (patient.getPatientType().equalsIgnoreCase("emergency")) {
            emergencyPatients[emergencyCount] = patient;
            emergencyCount++;
        } else {
            regularPatients[regularCount] = patient;
            regularCount++;
        }
    }

    public void printNextPatient() {
        if (emergencyCount > 0) {
            System.out.println("Next patient in line: " + emergencyPatients[0].getName());
        } else if (regularCount > 0) {
            System.out.println("Next patient in line: " + regularPatients[0].getName());
        } else {
            System.out.println("No patients in line.");
        }
    }

    public void printPatients() {
        System.out.println("Emergency Queue:");
        if (emergencyCount == 0) {
            System.out.println("No patients in emergency queue.");
        } else {
            for (int i = 0; i < emergencyCount; i++) {
                System.out.println((i + 1) + ". " + emergencyPatients[i].getName());
            }
        }

        System.out.println("\nRegular Queue:");
        if (regularCount == 0) {
            System.out.println("No patients in regular queue.");
        } else {
            for (int i = 0; i < regularCount; i++) {
                System.out.println((i + 1) + ". " + regularPatients[i].getName());
            }
        }

        printNextPatient();
    }

    public Patient dequeuePatient() {
        if (emergencyCount > 0) {
            Patient patient = emergencyPatients[0];
            for (int i = 1; i < emergencyCount; i++) {
                emergencyPatients[i - 1] = emergencyPatients[i];
            }
            emergencyCount--;
            return patient;
        } else if (regularCount > 0) {
            Patient patient = regularPatients[0];
            for (int i = 1; i < regularCount; i++) {
                regularPatients[i - 1] = regularPatients[i];
            }
            regularCount--;
            return patient;
        } else {
            return null;
        }
    }
}
