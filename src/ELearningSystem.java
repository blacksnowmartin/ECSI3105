import java.util.ArrayList;
import java.util.Scanner;

class Course {
    private final String title;
    private final String description;

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

public class ELearningSystem {
    private final ArrayList<Course> courses;

    public ELearningSystem() {
        courses = new ArrayList<>();
    }

    public void addCourse(String title, String description) {
        courses.add(new Course(title, description));
    }

    public void displayCourses() {
        for (Course course : courses) {
            System.out.println("Title: " + course.getTitle());
            System.out.println("Description: " + course.getDescription());
            System.out.println();
        }
    }

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
    }
}
