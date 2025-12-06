import java.util.ArrayList;
import java.util.Scanner;

public class StudentTracker {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== STUDENT TRACKER SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Summary Report");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    showSummaryReport();
                    break;
                case 3:
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.print("Enter student grade: ");
        double grade = scanner.nextDouble();

        students.add(new Student(name, grade));

        System.out.println("Student added successfully!");
    }

    private static void showSummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        double total = 0;
        double highest = students.get(0).getGrade();
        double lowest = students.get(0).getGrade();

        System.out.println("\n===== Summary Report =====");
        for (Student s : students) {
            System.out.println("Name: " + s.getName() + " | Grade: " + s.getGrade());
            double g = s.getGrade();
            total += g;

            if (g > highest) highest = g;
            if (g < lowest) lowest = g;
        }

        double average = total / students.size();

        System.out.println("\n--> Average Grade: " + average);
        System.out.println("--> Highest Grade: " + highest);
        System.out.println("--> Lowest Grade: " + lowest);
    }
}
public static void main(String[]args)
