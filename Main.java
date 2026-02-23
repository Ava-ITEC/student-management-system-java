import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        StudentService service = new StudentService(new CsvStore("students.csv"));

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addStudent(service);
                case "2" -> listStudents(service);
                case "3" -> search(service);
                case "4" -> remove(service);
                case "5" -> showAvg(service);
                case "0" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
            System.out.println();
        }
    }

    static void printMenu() {
        System.out.println("=== Student Management ===");
        System.out.println("1) Add student");
        System.out.println("2) List students");
        System.out.println("3) Search (name or ID)");
        System.out.println("4) Remove by ID");
        System.out.println("5) Class average");
        System.out.println("0) Exit");
        System.out.print("Select: ");
    }

    static void addStudent(StudentService service) {
        System.out.print("ID: ");
        String id = sc.nextLine().trim();

        System.out.print("First name: ");
        String first = sc.nextLine().trim();

        System.out.print("Last name: ");
        String last = sc.nextLine().trim();

        System.out.print("Grade (0-100): ");
        String gradeStr = sc.nextLine().trim();

        double grade;
        try {
            grade = Double.parseDouble(gradeStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade.");
            return;
        }
        if (grade < 0 || grade > 100) {
            System.out.println("Grade must be 0..100");
            return;
        }

        boolean ok = service.add(new Student(id, first, last, grade));
        System.out.println(ok ? "Student added." : "ID already exists.");
    }

    static void listStudents(StudentService service) {
        List<Student> list = service.list();
        if (list.isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        list.sort(Comparator.comparing(Student::getId));
        for (Student s : list) System.out.println(s);
    }

    static void search(StudentService service) {
        System.out.print("Enter ID or name keyword: ");
        String q = sc.nextLine().trim();
        if (q.isEmpty()) return;

        var byId = service.findById(q);
        if (byId.isPresent()) {
            System.out.println("Found: " + byId.get());
            return;
        }

        var results = service.searchByName(q);
        if (results.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        for (Student s : results) System.out.println(s);
    }

    static void remove(StudentService service) {
        System.out.print("Enter ID to remove: ");
        String id = sc.nextLine().trim();
        boolean ok = service.removeById(id);
        System.out.println(ok ? "Removed." : "Not found.");
    }

    static void showAvg(StudentService service) {
        System.out.printf("Class average: %.2f%n", service.classAverage());
    }
}