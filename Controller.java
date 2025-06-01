import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
public class Controller {
    private PatientManagement patientManagement;
    private Scanner scanner;

    public Controller() {
        patientManagement = new PatientManagement();
        scanner = new Scanner(System.in);
    }

    public void run() {
        int choice;
        do {
            printMenu();
            choice = getChoice();
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
                    patientManagement.dequeuePatient();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
        scanner.close();
    }

    private void printMenu() {
        System.out.println("====================");
        System.out.println("Patient Management System");
        System.out.println("====================");
        System.out.println("[1] Add Patient");
        System.out.println("[2] Print Waiting Room");
        System.out.println("[3] Print next Patient");
        System.out.println("[4] Call up next Patient");
        System.out.println("[5] Exit");
        System.out.println("====================");
    }

    private int getChoice() {
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline
        return choice;
    }

    private void addPatient() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline

        System.out.print("Enter birthday (yyyy-MM-dd): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(scanner.nextLine(), formatter);

        System.out.print("Is patient an emergency patient? (y/n): ");
        boolean isEmergency = scanner.nextLine().equalsIgnoreCase("y");

        Patient patient;
        if (isEmergency) {
            patient = new EmergencyPatient(name, age, birthday);
        } else {
            patient = new RegularPatient(name, age, birthday);
        }

        patientManagement.queuePatient(patient);
    }
}
