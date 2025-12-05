import java.util.ArrayList;
import java.util.Scanner;

class Course {
    private String title;
    private String description;

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

class ELearningSystem {
    private ArrayList<Course> courses;

    public ELearningSystem() {
        courses = new ArrayList<>();
    }

    public void addCourse(String title, String description) {
        Course course = new Course(title, description);
        courses.add(course);
    }

    public void displayCourses() {
        for (Course course : courses) {
            System.out.println("Title: " + course.getTitle());
            System.out.println("Description: " + course.getDescription());
            System.out.println();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ELearningSystem system = new ELearningSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter course title (or 'exit' to quit): ");
            String title = scanner.nextLine();
            if (title.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.println("Enter course description: ");
            String description = scanner.nextLine();

            system.addCourse(title, description);
        }

        System.out.println("Courses added:");
        system.displayCourses();

        // Save this file as ELearningSystem.java
    }
}
