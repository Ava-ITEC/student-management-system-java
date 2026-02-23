import java.util.*;

public class StudentService {
    private final CsvStore store;
    private final List<Student> students;

    public StudentService(CsvStore store) {
        this.store = store;
        this.students = new ArrayList<>(store.load());
    }

    public boolean add(Student s) {
        if (findById(s.getId()).isPresent()) return false;
        students.add(s);
        store.save(students);
        return true;
    }

    public List<Student> list() {
        return new ArrayList<>(students);
    }

    public Optional<Student> findById(String id) {
        String target = id.trim();
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(target)).findFirst();
    }

    public List<Student> searchByName(String keyword) {
        String k = keyword.trim().toLowerCase();
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getFirstName().toLowerCase().contains(k) || s.getLastName().toLowerCase().contains(k)) {
                result.add(s);
            }
        }
        return result;
    }

    public boolean removeById(String id) {
        Optional<Student> found = findById(id);
        if (found.isEmpty()) return false;
        students.remove(found.get());
        store.save(students);
        return true;
    }

    public double classAverage() {
        if (students.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Student s : students) sum += s.getGrade();
        return sum / students.size();
    }
}