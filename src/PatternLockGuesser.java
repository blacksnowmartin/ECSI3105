import java.util.*;

public class PatternLockGuesser {

    // A 3x3 grid representation:
    // 1  2  3
    // 4  5  6
    // 7  8  9
    // skip[i][j] stores the node that lies directly between node i and node j.
    // If skip[i][j] == 0, there is no node blocking a direct line between them.
    private static final int[][] skip = new int[10][10];

    static {
        // Horizontal skips
        skip[1][3] = skip[3][1] = 2;
        skip[4][6] = skip[6][4] = 5;
        skip[7][9] = skip[9][7] = 8;
        
        // Vertical skips
        skip[1][7] = skip[7][1] = 4;
        skip[2][8] = skip[8][2] = 5;
        skip[3][9] = skip[9][3] = 6;
        
        // Diagonal skips
        skip[1][9] = skip[9][1] = 5;
        skip[3][7] = skip[7][3] = 5;
    }

    public static void main(String[] args) {
        // Define the secret phone pattern to guess (e.g., 1 -> 2 -> 3 -> 6 -> 9)
        List<Integer> secretPattern = Arrays.asList(1, 2, 3, 6, 9);
        System.out.println("Secret Pattern to crack: " + secretPattern);

        System.out.println("\nStarting brute-force pattern guesser...");
        List<Integer> foundPattern = bruteForcePattern(secretPattern);

        if (foundPattern != null) {
            System.out.println("\n🎉 SUCCESS! Pattern guessed successfully: " + foundPattern);
        } else {
            System.out.println("\n❌ Pattern could not be guessed (it might violate hardware rules).");
        }
    }

    /**
     * Tries to find the secret pattern by generating all valid hardware patterns.
     */
    public static List<Integer> bruteForcePattern(List<Integer> secretPattern) {
        boolean[] visited = new boolean[10];
        List<Integer> currentPath = new ArrayList<>();
        long[] attempts = {0}; // Using an array to pass by reference inside recursive calls

        // Phone patterns typically must be between 4 and 9 nodes long
        for (int length = 4; length <= 9; length++) {
            System.out.println("Testing all valid patterns of length: " + length);
            
            // Try starting the pattern from each node (1 through 9)
            for (int startNode = 1; startNode <= 9; startNode++) {
                visited[startNode] = true;
                currentPath.add(startNode);

                List<Integer> result = searchPatterns(startNode, length, visited, currentPath, secretPattern, attempts);
                if (result != null) {
                    return result; // Found it!
                }

                // Backtrack
                currentPath.remove(currentPath.size() - 1);
                visited[startNode] = false;
            }
        }
        return null;
    }

    /**
     * Recursively generates valid movements across the grid using backtracking.
     */
    private static List<Integer> searchPatterns(int lastNode, int targetLength, boolean[] visited, 
                                                List<Integer> currentPath, List<Integer> secretPattern, long[] attempts) {
        
        // Base Case: If the path reaches the desired length, verify if it's correct
        if (currentPath.size() == targetLength) {
            attempts[0]++;
            if (currentPath.equals(secretPattern)) {
                System.out.println("Total valid combinations checked: " + attempts[0]);
                return new ArrayList<>(currentPath);
            }
            return null;
        }

        // Try moving to any next node from 1 to 9
        for (int nextNode = 1; nextNode <= 9; nextNode++) {
            if (!visited[nextNode]) {
                int middleNode = skip[lastNode][nextNode];
                
                // Rule validation: The jump is legal if there is no intermediate node (middleNode == 0)
                // OR if the intermediate node has already been visited.
                if (middleNode == 0 || visited[middleNode]) {
                    visited[nextNode] = true;
                    currentPath.add(nextNode);

                    List<Integer> found = searchPatterns(nextNode, targetLength, visited, currentPath, secretPattern, attempts);
                    if (found != null) {
                        return found; // Propagate the correct guess upwards
                    }

                    // Backtrack
                    currentPath.remove(currentPath.size() - 1);
                    visited[nextNode] = false;
                }
            }
        }
        return null;
    }
}
