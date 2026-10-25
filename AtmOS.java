import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AtmOS {

    // Simple Database to hold user account details
    private static class Account {
        private String pin;
        private double balance;

        public Account(String pin, double initialBalance) {
            this.pin = pin;
            this.balance = initialBalance;
            
        }

        public boolean verifyPin(String enteredPin) {
            return this.pin.equals(enteredPin);
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.printf(" Successfully deposited $%.2f%n", amount);
            } else {
                System.out.println("❌ Invalid deposit amount.");
            }
        }

        public boolean withdraw(double amount) {
            if (amount <= 0) {
                System.out.println("❌ Invalid withdrawal amount.");
                return false;
            }
            if (amount > balance) {
                System.out.println("❌ Insufficient funds.");
                return false;
            }
            balance -= amount;
            System.out.printf(" Successfully withdrew $%.2f%n", amount);
            return true;
        }
    }

    // ATM System state variables
    private static final Map<String, Account> database = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static Account currentAccount = null;
    private static String currentCardNumber = null;

    public static void main(String[] args) {
        initializeMockData();
        runATM();
    }

    // Seed data for the simulation
    private static void initializeMockData() {
        database.put("123456789", new Account("1111", 1500.50));
        database.put("987654321", new Account("2222", 500.00));
        database.put("111122223", new Account("3333", 50000.00));
    }

    // Core execution loop
    private static void runATM() {
        System.out.println("========================================");
        System.out.println("     WELCOME TO APEX BANK ATM OS       ");
        System.out.println("========================================");

        while (true) {
            if (currentAccount == null) {
                handleAuthentication();
            } else {
                handleMainMenu();
            }
        }
    }

    // Screen 1: Card and PIN Authentication
    private static void handleAuthentication() {
        System.out.print("\nPlease insert/enter your card number (or 'exit' to shut down): ");
        String cardNumber = scanner.nextLine().trim();

        if (cardNumber.equalsIgnoreCase("exit")) {
            System.out.println("Shutting down ATM OS. Goodbye.");
            System.exit(0);
        }

        if (!database.containsKey(cardNumber)) {
            System.out.println("❌ Card unrecognized. Please try again.");
            return;
        }

        System.out.print("Enter your 4-digit PIN: ");
        String pin = scanner.nextLine().trim();

        Account acc = database.get(cardNumber);
        if (acc.verifyPin(pin)) {
            currentAccount = acc;
            currentCardNumber = cardNumber;
            System.out.println("\n Access Granted.");
        } else {
            System.out.println("❌ Incorrect PIN. Access Denied.");
        }
    }

    // Screen 2: Operation Menu
    private static void handleMainMenu() {
        System.out.println("\n----------------------------------------");
        System.out.println("               MAIN MENU                ");
        System.out.println("----------------------------------------");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Exit / Return Card");
        System.out.print("Please select an option (1-4): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                System.out.printf("%nCurrent Balance: $%.2f%n", currentAccount.getBalance());
                break;
            case "2":
                System.out.print("Enter deposit amount: $");
                double depAmount = getValidAmountInput();
                currentAccount.deposit(depAmount);
                break;
            case "3":
                System.out.print("Enter withdrawal amount: $");
                double drawAmount = getValidAmountInput();
                currentAccount.withdraw(drawAmount);
                break;
            case "4":
                System.out.println("\n Thank you for banking with us. Ejecting card...");
                currentAccount = null;
                currentCardNumber = null;
                break;
            default:
                System.out.println("❌ Invalid choice. Please pick options 1 to 4.");
        }
    }

    // Input validation utility helper
    private static double getValidAmountInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Triggers "invalid amount" logic downstream
        }
    }
}
