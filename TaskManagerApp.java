import java.util.ArrayList;
import java.util.Scanner;

public class TaskManagerApp {

    // Inner class representing a Task entity
    static class Task {
        private final String description;
        private boolean isCompleted;

        public Task(String description) {
            this.description = description;
            this.isCompleted = false;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void markAsCompleted() {
            this.isCompleted = true;
        }

        @Override
        public String toString() {
            String statusSymbol = isCompleted ? "[X]" : "[ ]";
            return statusSymbol + " " + description;
        }
    }

    public static void main(String[] args) {
        ArrayList<Task> taskList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to the Java Task Manager ===");

        while (running) {
            System.out.println("\nAvailable Options:");
            System.out.println("1. View tasks");
            System.out.println("2. Add a task");
            System.out.println("3. Complete a task");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    viewTasks(taskList);
                    break;
                case "2":
                    addTask(taskList, scanner);
                    break;
                case "3":
                    completeTask(taskList, scanner);
                    break;
                case "4":
                    running = false;
                    System.out.println("Goodbye! Have a productive day.");
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a valid option (1-4).");
            }
        }
        scanner.close();
    }

    private static void viewTasks(ArrayList<Task> taskList) {
        System.out.println("\n--- Your Task List ---");
        if (taskList.isEmpty()) {
            System.out.println("No tasks found. Your list is empty!");
            return;
        }
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + ". " + taskList.get(i));
        }
    }

    private static void addTask(ArrayList<Task> taskList, Scanner scanner) {
        System.out.print("\nEnter the task description: ");
        String description = scanner.nextLine().trim();
        
        if (description.isEmpty()) {
            System.out.println("Task description cannot be empty!");
        } else {
            taskList.add(new Task(description));
            System.out.println("Task \"" + description + "\" added successfully.");
        }
    }

    private static void completeTask(ArrayList<Task> taskList, Scanner scanner) {
        if (taskList.isEmpty()) {
            System.out.println("\nThere are no tasks to mark complete.");
            return;
        }

        viewTasks(taskList);
        System.out.print("\nEnter the number of the task to mark as complete: ");
        String input = scanner.nextLine();

        try {
            int taskNumber = Integer.parseInt(input);
            int targetIndex = taskNumber - 1;

            if (targetIndex >= 0 && targetIndex < taskList.size()) {
                Task task = taskList.get(targetIndex);
                if (task.isCompleted()) {
                    System.out.println("This task is already completed!");
                } else {
                    task.markAsCompleted();
                    System.out.println("Success! Task marked as completed: " + task.getDescription());
                }
            } else {
                System.out.println("Invalid task number. Please select a number from the list.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric value.");
        }
    }
}
