import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

// Represents a Book entity
class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isBorrowed;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isBorrowed = false;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public boolean isBorrowed() { return isBorrowed; }

    public void setBorrowed(boolean borrowed) { this.isBorrowed = borrowed; }

    @Override
    public String toString() {
        return "'" + title + "' by " + author + " (ISBN: " + isbn + ") - Status: " + (isBorrowed ? "Borrowed" : "Available");
    }
}

// Represents a User entity
class User {
    private String name;
    private String userId;
    // A list of ISBNs of books currently borrowed by the user
    private List<String> borrowedBooks;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters and other methods
    public String getUserId() { return userId; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }

    public void borrowBook(String isbn) { borrowedBooks.add(isbn); }
    public void returnBook(String isbn) { borrowedBooks.remove(isbn); }

    @Override
    public String toString() {
        return "User: " + name + " (ID: " + userId + ") - Borrowed Books: " + borrowedBooks.size();
    }
}

// Main class to manage the library operations
public class LibraryManager {
    private List<Book> books = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
        manager.seedData(); // Add some initial data
        manager.runMenu();
    }

    private void seedData() {
        books.add(new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "978-0345391803"));
        books.add(new Book("1984", "George Orwell", "978-0451524935"));
        users.add(new User("Alice", "U001"));
        users.add(new User("Bob", "U002"));
    }

    private void runMenu() {
        while (true) {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Display all books");
            System.out.println("2. Borrow a book");
            System.out.println("3. Return a book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayBooks();
                    break;
                case "2":
                    borrowBook();
                    break;
                case "3":
                    returnBook();
                    break;
                case "4":
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.println("--- Current Books ---");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private void borrowBook() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        User user = findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter Book ISBN to borrow: ");
        String isbn = scanner.nextLine();
        Book book = findBook(isbn);

        if (book == null) {
            System.out.println("Book not found.");
        } else if (book.isBorrowed()) {
            System.out.println("Book is already borrowed.");
        } else {
            book.setBorrowed(true);
            user.borrowBook(isbn);
            System.out.println("User " + user.getUserId() + " successfully borrowed " + book.getTitle());
        }
    }

    private void returnBook() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        User user = findUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter Book ISBN to return: ");
        String isbn = scanner.nextLine();
        Book book = findBook(isbn);

        if (book == null) {
            System.out.println("Book not found.");
        } else if (!book.isBorrowed()) {
            System.out.println("Book was not borrowed (or is already returned).");
        } else {
            book.setBorrowed(false);
            user.returnBook(isbn);
            System.out.println("User " + user.getUserId() + " successfully returned " + book.getTitle());
        }
    }

    // Helper methods to find objects within the lists
    private Book findBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    private User findUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}

