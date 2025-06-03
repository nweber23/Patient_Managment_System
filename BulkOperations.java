import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BulkOperations {
    private PatientManagement patientManagement;
    private Scanner scanner;
    
    public BulkOperations(PatientManagement patientManagement, Scanner scanner) {
        this.patientManagement = patientManagement;
        this.scanner = scanner;
    }
    
    public void showBulkOperationsMenu() {
        System.out.println("\n" + Colors.CYAN + Colors.BOLD + "=== BULK OPERATIONS ===" + Colors.RESET);
        String[][] menuItems = {
            {"1", "Clear Emergency Queue", Colors.RED},
            {"2", "Clear Senior Queue", Colors.ORANGE},
            {"3", "Clear Regular Queue", Colors.BLUE},
            {"4", "Clear All Queues", Colors.RED + Colors.BOLD},
            {"5", "Clear by Age Range", Colors.YELLOW},
            {"6", "Back to Main Menu", Colors.CYAN}
        };
        
        for (String[] item : menuItems) {
            System.out.println(item[2] + "[" + item[0] + "] " + item[1] + Colors.RESET);
        }
        System.out.println(Colors.CYAN + Colors.BOLD + "==================" + Colors.RESET);
        
        int choice = getBulkChoice();
        executeBulkChoice(choice);
    }
    
    private int getBulkChoice() {
        int choice = 0;
        while (choice < 1 || choice > 6) {
            try {
                System.out.print(Colors.YELLOW + Colors.BOLD + "Enter choice (1-6): " + Colors.RESET);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(Colors.RED + "Input cannot be empty." + Colors.RESET);
                    continue;
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > 6) {
                    System.out.println(Colors.RED + "Please enter a number between 1 and 6." + Colors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Invalid input! Please enter a number between 1 and 6." + Colors.RESET);
            }
        }
        return choice;
    }
    
    private void executeBulkChoice(int choice) {
        switch (choice) {
            case 1: clearQueueByType(PatientType.EMERGENCY); break;
            case 2: clearQueueByType(PatientType.SENIOR); break;
            case 3: clearQueueByType(PatientType.REGULAR); break;
            case 4: clearAllQueues(); break;
            case 5: clearByAgeRange(); break;
            case 6: return; // Back to main menu
        }
    }
    
    private void clearQueueByType(PatientType type) {
        int count = patientManagement.getCountByType(type);
        if (count == 0) {
            System.out.println(Colors.YELLOW + "No " + type.name().toLowerCase() + 
                              " patients to clear." + Colors.RESET);
            return;
        }
        
        System.out.println(Colors.YELLOW + "This will remove " + count + " " + 
                          type.name().toLowerCase() + " patient(s)." + Colors.RESET);
        
        if (confirmAction("clear " + type.name().toLowerCase() + " queue")) {
            List<String> removedPatients = patientManagement.clearQueueByType(type);
            System.out.println(Colors.GREEN + "Cleared " + removedPatients.size() + 
                              " patients from " + type.name().toLowerCase() + " queue:" + Colors.RESET);
            
            for (String patientName : removedPatients) {
                System.out.println(Colors.WHITE + "  - " + patientName + Colors.RESET);
            }
        } else {
            System.out.println(Colors.YELLOW + "Operation cancelled." + Colors.RESET);
        }
    }
    
    private void clearAllQueues() {
        int totalCount = patientManagement.getTotalPatientCount();
        if (totalCount == 0) {
            System.out.println(Colors.YELLOW + "No patients to clear." + Colors.RESET);
            return;
        }
        
        System.out.println(Colors.RED + Colors.BOLD + "WARNING: This will remove ALL " + 
                          totalCount + " patients from ALL queues!" + Colors.RESET);
        
        if (confirmAction("clear ALL queues")) {
            List<String> removedPatients = patientManagement.clearAllQueues();
            System.out.println(Colors.GREEN + "Cleared all queues. Removed " + 
                              removedPatients.size() + " patients:" + Colors.RESET);
            
            for (String patientName : removedPatients) {
                System.out.println(Colors.WHITE + "  - " + patientName + Colors.RESET);
            }
        } else {
            System.out.println(Colors.YELLOW + "Operation cancelled." + Colors.RESET);
        }
    }
    
    private void clearByAgeRange() {
        System.out.println(Colors.CYAN + "Clear patients by age range:" + Colors.RESET);
        
        int minAge = getAgeInput("Enter minimum age: ");
        int maxAge = getAgeInput("Enter maximum age: ");
        
        if (minAge > maxAge) {
            System.out.println(Colors.RED + "Minimum age cannot be greater than maximum age." + Colors.RESET);
            return;
        }
        
        List<String> affectedPatients = patientManagement.getPatientsInAgeRange(minAge, maxAge);
        
        if (affectedPatients.isEmpty()) {
            System.out.println(Colors.YELLOW + "No patients found in age range " + 
                              minAge + "-" + maxAge + "." + Colors.RESET);
            return;
        }
        
        System.out.println(Colors.YELLOW + "This will remove " + affectedPatients.size() + 
                          " patient(s) aged " + minAge + "-" + maxAge + ":" + Colors.RESET);
        
        for (String patientInfo : affectedPatients) {
            System.out.println(Colors.WHITE + "  - " + patientInfo + Colors.RESET);
        }
        
        if (confirmAction("clear patients in age range " + minAge + "-" + maxAge)) {
            List<String> removedPatients = patientManagement.clearByAgeRange(minAge, maxAge);
            System.out.println(Colors.GREEN + "Removed " + removedPatients.size() + 
                              " patients in age range " + minAge + "-" + maxAge + "." + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "Operation cancelled." + Colors.RESET);
        }
    }
    
    private boolean confirmAction(String action) {
        while (true) {
            System.out.print(Colors.RED + Colors.BOLD + "Are you sure you want to " + 
                           action + "? Type 'CONFIRM' to proceed or 'CANCEL' to abort: " + Colors.RESET);
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("confirm")) {
                return true;
            } else if (input.equals("cancel")) {
                return false;
            } else {
                System.out.println(Colors.RED + "Please type 'CONFIRM' or 'CANCEL'." + Colors.RESET);
            }
        }
    }
    
    private int getAgeInput(String prompt) {
        int age = -1;
        while (age < 0 || age > 150) {
            try {
                System.out.print(Colors.YELLOW + Colors.BOLD + prompt + Colors.RESET);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(Colors.RED + "Input cannot be empty." + Colors.RESET);
                    continue;
                }
                age = Integer.parseInt(input);
                if (age < 0 || age > 150) {
                    System.out.println(Colors.RED + "Age must be between 0 and 150." + Colors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Invalid input! Please enter a valid age." + Colors.RESET);
            }
        }
        return age;
    }
}