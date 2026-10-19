import java.util.Scanner;

public class MeditationHelper {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(String.class.cast("").getClass() == String.class ? new Scanner(System.in) : null);
        System.out.println("========================================");
        System.out.println("   Welcome to Your Personal Zen Space   ");
        System.out.println("========================================");

        // Step 1: Choose Meditation Style
        System.out.println("\nSelect your meditation style:");
        System.out.println("1. Silent Timer (Self-guided)");
        System.out.println("2. Guided Box Breathing (4-4-4-4 Technique)");
        System.out.print("Enter choice (1 or 2): ");
        int choice = scanner.nextInt();

        // Step 2: Choose Ambient Environment
        System.out.println("\nSelect an ambient background sound to imagine:");
        System.out.println("1. Ocean Waves");
        System.out.println("2. Gentle Rain");
        System.out.println("3. Forest Birds");
        System.out.println("4. Absolute Silence");
        System.out.print("Enter choice (1-4): ");
        int soundChoice = scanner.nextInt();
        String ambientSound = getAmbientSound(soundChoice);

        // Step 3: Choose Duration
        System.out.print("\nHow many minutes would you like to meditate? ");
        int minutes = scanner.nextInt();
        
        System.out.println("\nPreparing your space...");
        System.out.println("[Ambient Setting: " + ambientSound + "]");
        System.out.println("Sit comfortably. Close your eyes. Soften your shoulders.");
        countdown(3); // 3-second preparation countdown

        if (choice == 2) {
            runBoxBreathing(minutes);
        } else {
            runSilentTimer(minutes);
        }

        // Session Wrap-up
        System.out.println("\n========================================");
        System.out.println("   ✨ Namaste. Session Complete. ✨   ");
        System.out.println("   Take this peace with you into your day.   ");
        System.out.println("========================================");
        scanner.close();
    }

    // Helper: Map sound choices
    private static String getAmbientSound(int choice) {
        switch (choice) {
            case 1: return "🌊 Distant Ocean Waves";
            case 2: return "🌧️ Soft Rain on a Window";
            case 3: return "🌳 Morning Forest Birds";
            default: return "🤫 Deep Silence";
        }
    }

    // Helper: Preparation countdown
    private static void countdown(int seconds) {
        try {
            for (int i = seconds; i > 0; i--) {
                System.out.print("Starting in " + i + "... \r");
                Thread.sleep(1000);
            }
            System.out.println("Begin now.              \n");
        } catch (InterruptedException e) {
            System.out.println("Ready!");
        }
    }

    // Feature 1: Silent Meditation Timer
    private static void runSilentTimer(int totalMinutes) {
        int totalSeconds = totalMinutes * 60;
        System.out.println("Timer started for " + totalMinutes + " minute(s). Focus on your breath.");
        
        try {
            for (int remaining = totalSeconds; remaining > 0; remaining--) {
                int mins = remaining / 60;
                int secs = remaining % 60;
                // Formats output nicely as MM:SS on a single updating line
                System.out.printf("Time remaining: %02d:%02d \r", mins, secs);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("\nSession interrupted.");
        }
    }

    // Feature 2: Guided Box Breathing Pacer
    private static void runBoxBreathing(int totalMinutes) {
        int totalSeconds = totalMinutes * 60;
        int cycleTime = 16; // 4 seconds per action * 4 actions = 16 seconds per loop
        int cycles = totalSeconds / cycleTime;

        System.out.println("Starting Box Breathing (" + totalMinutes + " minutes (~" + cycles + " cycles)).");
        System.out.println("Follow the prompts on screen.");

        try {
            for (int i = 1; i <= cycles; i++) {
                System.out.println("\n--- Cycle " + i + " ---");
                
                printBreathingPrompt("😤 Inhale deeply...", 4);
                printBreathingPrompt("🛑 Hold your breath...", 4);
                printBreathingPrompt("😮 Exhale fully...", 4);
                printBreathingPrompt("🛑 Hold empty...", 4);
            }
        } catch (InterruptedException e) {
            System.out.println("\nSession interrupted.");
        }
    }

    // Helper: Prints the live counter for breathing steps
    private static void printBreathingPrompt(String message, int seconds) throws InterruptedException {
        for (int i = 1; i <= seconds; i++) {
            System.out.print(message + " (" + i + "s)\r");
            Thread.sleep(1000);
        }
    }
}
