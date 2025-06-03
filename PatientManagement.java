import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class PatientManagement {
    private PriorityQueue<Patient> patientQueue;
    private int totalPatientsToday = 0;
    private int totalEmergenciesToday = 0;

    public PatientManagement() {
        this.patientQueue = new PriorityQueue<>(
            Comparator.comparing((Patient p) -> p.getType().getPriority())
                     .thenComparing(Patient::getArrivalTime)
        );
    }

    public void queuePatient(Patient patient) {
        patientQueue.offer(patient);
        totalPatientsToday++;
        
        if (patient.getType() == PatientType.EMERGENCY) {
            totalEmergenciesToday++;
        }
        
        System.out.println(patient.getType().getColor() + 
                          patient.getType().name() + " patient " + 
                          patient.getName() + " added to queue." + Colors.RESET);
    }

    public List<Patient> searchPatientsByName(String searchName) {
        String searchLower = searchName.toLowerCase().trim();
        return patientQueue.stream()
            .filter(p -> p.getName().toLowerCase().contains(searchLower))
            .collect(Collectors.toList());
    }

    public void printSearchResults(String searchName) {
        List<Patient> foundPatients = searchPatientsByName(searchName);
        
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + 
                          "=== SEARCH RESULTS FOR: \"" + searchName + "\" ===" + Colors.RESET);
        
        if (foundPatients.isEmpty()) {
            System.out.println(Colors.YELLOW + "No patients found matching \"" + searchName + "\"." + Colors.RESET);
            return;
        }
        
        System.out.println(Colors.GREEN + "Found " + foundPatients.size() + " patient(s):" + Colors.RESET);
        
        for (int i = 0; i < foundPatients.size(); i++) {
            Patient patient = foundPatients.get(i);
            PatientType type = patient.getType();
            
            System.out.println(type.getColor() + "\n" + (i + 1) + ". " + 
                              patient.getTypeIcon() + " " + patient.getName() + Colors.RESET);
            System.out.println(Colors.WHITE + "   Age: " + patient.getAge() + 
                              " | Birthday: " + patient.getBirthday() + Colors.RESET);
            System.out.println(Colors.WHITE + "   Queue: " + type.getQueueName() + Colors.RESET);
            System.out.println(Colors.WHITE + "   Priority Level: " + patient.getPriorityLevel() + Colors.RESET);
            
            if (patient.hasNotes()) {
                System.out.println(Colors.YELLOW + "   Latest Note: " + patient.getLatestNote() + Colors.RESET);
            } else {
                System.out.println(Colors.YELLOW + "   No notes recorded." + Colors.RESET);
            }
        }
    }

    public Patient findPatientByExactName(String patientName) {
        return patientQueue.stream()
            .filter(p -> p.getName().equalsIgnoreCase(patientName))
            .findFirst()
            .orElse(null);
    }

    public boolean changePatientType(String patientName, String newType) {
        Patient patient = findPatientByExactName(patientName);
        if (patient == null) return false;
        
        PatientType newPatientType;
        try {
            newPatientType = PatientType.valueOf(newType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        
        if (patient.getType() == newPatientType) {
            System.out.println(Colors.YELLOW + "Patient " + patientName + 
                              " is already of type " + newPatientType.name() + "." + Colors.RESET);
            return true;
        }
        
        PatientType oldType = patient.getType();
        patient.changeType(newPatientType);
        
        // Re-add to queue to update priority
        patientQueue.remove(patient);
        patientQueue.offer(patient);
        
        System.out.println(Colors.GREEN + "Patient " + patientName + " moved from " + 
                          oldType.getQueueName() + " to " + newPatientType.getQueueName() + "." + Colors.RESET);
        
        return true;
    }

    public boolean removePatient(String patientName) {
        return patientQueue.removeIf(p -> p.getName().equalsIgnoreCase(patientName));
    }

    public void printNextPatient() {
        Patient nextPatient = patientQueue.peek();
        if (nextPatient != null) {
            PatientType type = nextPatient.getType();
            System.out.println(type.getColor() + "Next patient: " + 
                              nextPatient.getTypeIcon() + " " + nextPatient.getName() + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "No patients in line." + Colors.RESET);
        }
    }

    public void printPatients() {
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + "=== PATIENT QUEUES ===" + Colors.RESET);

        Map<PatientType, List<Patient>> patientsByType = patientQueue.stream()
            .collect(Collectors.groupingBy(Patient::getType));

        for (PatientType type : PatientType.values()) {
            List<Patient> patients = patientsByType.getOrDefault(type, new ArrayList<>());
            
            System.out.println(type.getColor() + Colors.BOLD + type.getQueueName() + ":" + Colors.RESET);
            
            if (patients.isEmpty()) {
                System.out.println(Colors.YELLOW + "  No " + type.name().toLowerCase() + " patients." + Colors.RESET);
            } else {
                for (int i = 0; i < patients.size(); i++) {
                    Patient patient = patients.get(i);
                    String notesDisplay = patient.hasNotes() ? " - " + patient.getLatestNote() : "";
                    System.out.println(type.getColor() + "  " + (i + 1) + ". " + 
                                      patient.getName() + " (Age: " + patient.getAge() + ")" + 
                                      notesDisplay + Colors.RESET);
                }
            }
            
            if (type != PatientType.REGULAR) {
                System.out.println();
            }
        }

        System.out.println();
        printNextPatient();
    }

    public void printStatistics() {
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + "=== PATIENT STATISTICS ===" + Colors.RESET);
        
        Map<PatientType, Long> countsByType = patientQueue.stream()
            .collect(Collectors.groupingBy(Patient::getType, Collectors.counting()));

        System.out.println(Colors.WHITE + "Current Patients in Queue:" + Colors.RESET);
        for (PatientType type : PatientType.values()) {
            long count = countsByType.getOrDefault(type, 0L);
            System.out.println(type.getColor() + "  " + type.name() + ": " + count + Colors.RESET);
        }
        System.out.println(Colors.CYAN + "  Total Waiting: " + getTotalPatientCount() + Colors.RESET);
        
        System.out.println(Colors.WHITE + "\nToday's Statistics:" + Colors.RESET);
        System.out.println(Colors.GREEN + "  Total Patients Today: " + totalPatientsToday + Colors.RESET);
        System.out.println(Colors.RED + "  Emergencies Today: " + totalEmergenciesToday + Colors.RESET);
        
        double averageAge = calculateAverageAge();
        if (averageAge > 0) {
            System.out.println(Colors.PURPLE + "  Average Age: " + 
                              String.format("%.1f", averageAge) + " years" + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "  Average Age: No patients to calculate" + Colors.RESET);
        }
        
        if (getTotalPatientCount() > 0) {
            System.out.println(Colors.WHITE + "\nQueue Composition:" + Colors.RESET);
            for (PatientType type : PatientType.values()) {
                long count = countsByType.getOrDefault(type, 0L);
                double percentage = (double) count / getTotalPatientCount() * 100;
                System.out.println(type.getColor() + "  " + type.name() + ": " + 
                                  String.format("%.1f", percentage) + "%" + Colors.RESET);
            }
        }
    }

    private double calculateAverageAge() {
        return patientQueue.stream()
            .mapToInt(Patient::getAge)
            .average()
            .orElse(0.0);
    }

    public Patient dequeuePatient() {
        Patient patient = patientQueue.poll();
        if (patient != null) {
            System.out.println(Colors.GREEN + "Called " + patient.getType().name().toLowerCase() + 
                              " patient: " + patient.getName() + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "No patients to call - all queues are empty." + Colors.RESET);
        }
        return patient;
    }

    // Method to get count by patient type
    public int getCountByType(PatientType type) {
        return (int) patientQueue.stream()
            .filter(p -> p.getType() == type)
            .count();
    }

    // Method to clear queue by type
    public List<String> clearQueueByType(PatientType type) {
        List<String> removedPatients = patientQueue.stream()
            .filter(p -> p.getType() == type)
            .map(Patient::getName)
            .collect(Collectors.toList());
        
        patientQueue.removeIf(p -> p.getType() == type);
        return removedPatients;
    }

    // Method to clear all queues
    public List<String> clearAllQueues() {
        List<String> removedPatients = patientQueue.stream()
            .map(Patient::getName)
            .collect(Collectors.toList());
        
        patientQueue.clear();
        return removedPatients;
    }

    // Method to get patients in age range
    public List<String> getPatientsInAgeRange(int minAge, int maxAge) {
        return patientQueue.stream()
            .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
            .map(p -> p.getName() + " (Age: " + p.getAge() + ", Type: " + p.getType().name() + ")")
            .collect(Collectors.toList());
    }

    // Method to clear patients by age range
    public List<String> clearByAgeRange(int minAge, int maxAge) {
        List<String> removedPatients = patientQueue.stream()
            .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
            .map(Patient::getName)
            .collect(Collectors.toList());
        
        patientQueue.removeIf(p -> p.getAge() >= minAge && p.getAge() <= maxAge);
        return removedPatients;
    }

    // Getter methods
    public int getTotalPatientCount() { return patientQueue.size(); }
    public int getEmergencyCount() { return (int) patientQueue.stream().filter(p -> p.getType() == PatientType.EMERGENCY).count(); }
    public int getSeniorCount() { return (int) patientQueue.stream().filter(p -> p.getType() == PatientType.SENIOR).count(); }
    public int getRegularCount() { return (int) patientQueue.stream().filter(p -> p.getType() == PatientType.REGULAR).count(); }
    public int getTotalPatientsToday() { return totalPatientsToday; }
    public int getTotalEmergenciesToday() { return totalEmergenciesToday; }
}