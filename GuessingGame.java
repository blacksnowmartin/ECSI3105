import java.util.Scanner;
import java.util.Random;

public class GuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.util.System.in);
        Random random = new Random();
        
        int numberToGuess = random.nextInt(100) + 1;
        int numberOfTries = 0;
        int guess = 0;
        boolean win = false;
        
        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("I'm thinking of a number between 1 and 100. Can you guess it?");
        
        while (!win) {
            System.out.print("Enter your guess: ");
            
            // Check if the user input is actually an integer
            if (!scanner.hasNextInt()) {
                System.out.println("That's not a valid number! Please try again.");
                scanner.next(); // Clear the invalid input
                continue;
            }
            
            guess = scanner.nextInt();
            numberOfTries++;
            
            if (guess < 1 || guess > 100) {
                System.out.println("Please keep your guess between 1 and 100!");
            } else if (guess < numberToGuess) {
                System.out.println("Too low! Try a higher number.");
            } else if (guess > numberToGuess) {
                System.out.println("Too high! Try a lower number.");
            } else {
                win = true;
            }
        }
        
        System.out.println("\nCongratulations! You found the number " + numberToGuess + "!");
        System.out.println("It took you " + numberOfTries + " attempts.");
        
        scanner.close();
    }
}
