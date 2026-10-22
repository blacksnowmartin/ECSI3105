import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MotorolaKeypadSimulator {
    // Mapping of keypad digits to their respective characters
    private static final Map<Character, String> KEYPAD = new HashMap<>();

    static {
        KEYPAD.put('1', ".");     // Standard retro mapping or punctuation
        KEYPAD.put('2', "ABC");
        KEYPAD.put('3', "DEF");
        KEYPAD.put('4', "GHI");
        KEYPAD.put('5', "JKL");
        KEYPAD.put('6', "MNO");
        KEYPAD.put('7', "PQRS");
        KEYPAD.put('8', "TUV");
        KEYPAD.put('9', "WXYZ");
        KEYPAD.put('0', " ");     // Space bar
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder screenText = new StringBuilder();

        System.out.println("=========================================");
        System.out.println("   MOTOROLA KEYPAD SIMULATOR (Multi-Tap) ");
        System.out.println("=========================================");
        System.out.println("Instructions:");
        System.out.println("- Type a sequence of numbers to form a word.");
        System.out.println("  Example: '777786667' gives 'STOP'");
        System.out.println("- To type adjacent letters on the SAME key, separate them with a space or '.'");
        System.out.println("  Example: '44 444' or '44.444' gives 'HI'");
        System.out.println("- Type 'B' to backspace.");
        System.out.println("- Type 'EXIT' to turn off the phone.");
        System.out.println("=========================================\n");

        while (true) {
            System.out.println("Screen Display: [" + screenText.toString() + "]");
            System.out.print("Press Keys: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("EXIT")) {
                System.out.println("\nPowering off... Goodbye!");
                break;
            }

            if (input.equals("B")) {
                if (screenText.length() > 0) {
                    screenText.deleteCharAt(screenText.length() - 1);
                } else {
                    System.out.println("! Screen is already empty.");
                }
                continue;
            }

            if (input.isEmpty()) {
                continue;
            }

            // Process the keystroke sequence
            String translatedText = parseMultiTap(input);
            screenText.append(translatedText);
            System.out.println("-----------------------------------------");
        }
        scanner.close();
    }

    /**
     * Translates a string of keypad presses into actual text.
     */
    private static String parseMultiTap(String input) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int n = input.length();

        while (i < n) {
            char currentKey = input.charAt(i);

            // Skip pause separators
            if (currentKey == ' ' || currentKey == '.') {
                i++;
                continue;
            }

            // If the key is not in our keypad mapping, skip it
            if (!KEYPAD.containsKey(currentKey)) {
                System.out.println("! Invalid key ignored: " + currentKey);
                i++;
                continue;
            }

            // Count how many times this same key is consecutively pressed
            int count = 0;
            while (i < n && input.charAt(i) == currentKey) {
                count++;
                i++;
            }

            // Determine the character based on the multi-tap index loop
            String letters = KEYPAD.get(currentKey);
            int index = (count - 1) % letters.length();
            result.append(letters.charAt(index));
        }

        return result.toString();
    }
}
