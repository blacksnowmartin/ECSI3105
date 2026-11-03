import java.util.ArrayList;
import java.util.Scanner;

public class TodoApp {
    public static void main(String[] args) {
        ArrayList<String> todoList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to Your Java To-Do List App ===");

        while (running) {
            System.out.println("\n--- Options ---");
            System.out.println("1. View Tasks");
            System.out.println("2. Add Task");
            System.out.println("3. Remove Task");
            System.out.println("4. Exit");
            System.out.print("Choose an option (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the input buffer

            switch (choice) {
                case 1:
                    System.out.println("\n--- Your Tasks ---");
                    if (todoList.isEmpty()) {
                        System.out.println("Your list is empty!");
                    } else {
                        for (int i = 0; i < todoList.size(); i++) {
                            System.out.println((i + 1) + ". " + todoList.get(i));
                        }
                    }
                    break;

                case 2:
                    System.out.print("\nEnter the task description: ");
                    String newTask = scanner.nextLine();
                    todoList.add(newTask);
                    System.out.println("Task added successfully!");
                    break;

                case 3:
                    System.out.println("\n--- Remove a Task ---");
                    if (todoList.isEmpty()) {
                        System.out.println("Nothing to remove!");
                    } else {
                        for (int i = 0; i < todoList.size(); i++) {
                            System.out.println((i + 1) + ". " + todoList.get(i));
                        }
                        System.out.print("Enter the task number to remove: ");
                        int taskNum = scanner.nextInt();
                        
                        if (taskNum > 0 && taskNum <= todoList.size()) {
                            todoList.remove(taskNum - 1);
                            System.out.println("Task removed!");
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    }
                    break;

                case 4:
                    running = false;
                    System.out.println("\nGoodbye! Have a productive day.");
                    break;

                default:
                    System.out.println("Invalid option. Please choose between 1 and 4.");
            }
        }
        scanner.close();
    }
}
