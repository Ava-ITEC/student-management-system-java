import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class CsvStore {
    private final Path filePath;

    public CsvStore(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    public List<Student> load() {
        if (!Files.exists(filePath)) return new ArrayList<>();

        List<Student> students = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // id,first,last,grade
                String[] parts = line.split(",", -1);
                if (parts.length != 4) continue;

                String id = parts[0];
                String first = parts[1];
                String last = parts[2];
                double grade;
                try {
                    grade = Double.parseDouble(parts[3]);
                } catch (NumberFormatException e) {
                    continue;
                }
                students.add(new Student(id, first, last, grade));
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
        return students;
    }

    public void save(List<Student> students) {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Student s : students) {
                bw.write(String.format("%s,%s,%s,%.2f",
                        escape(s.getId()), escape(s.getFirstName()), escape(s.getLastName()), s.getGrade()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

    private String escape(String v) {
        // simple: remove commas to keep CSV clean
        return v.replace(",", " ");
    }
}