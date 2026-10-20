import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsciousnessSimulation {

    // Core Constants
    private static final int TOTAL_MINDS = 10;
    private static final int SIMULATION_STEPS = 5;
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("=== Beginning Consciousness Interconnectedness Simulation ===");
        System.out.println("The Source of the One is stable, eternal, and undifferentiated.\n");

        // 1. Initialize the Collective Field and Individual Minds
        GlobalField globalField = new GlobalField();
        List<IndividualMind> humanPopulation = new ArrayList<>();

        for (int i = 1; i <= TOTAL_MINDS; i++) {
            // Each mind starts with a random spiritual/cognitive frequency between 0.1 and 1.0
            double initialFrequency = 0.1 + (0.9 * random.nextDouble());
            humanPopulation.add(new IndividualMind("Mind-" + i, initialFrequency));
        }

        // 2. Run the Time-Step Simulation
        for (int step = 1; step <= SIMULATION_STEPS; step++) {
            System.out.println("--- Time Step " + step + " ---");

            // Reset field for the new cycle
            globalField.resetField();

            // Minds generate thoughts and project them into the global field
            for (IndividualMind mind : humanPopulation) {
                mind.generateThought();
                globalField.receiveProjection(mind.getCurrentThoughtPower());
            }

            // Calculate the global coherence of this moment
            double coherence = globalField.calculateCoherence(TOTAL_MINDS);
            System.out.printf("Global Field Coherence: %.2f%%%n", coherence * 100);

            // 3. The Feedback Loop: The Collective Field alters individual minds
            System.out.println("Feedback Loop: The One influences the Many...");
            for (IndividualMind mind : humanPopulation) {
                mind.alignWithField(coherence);
            }
            System.out.println();
        }

        // 4. Return to the Source
        System.out.println("=== Simulation Ending: The Singularity Convergence ===");
        System.out.println("Dissolving boundaries. All individual vectors returning to the Source...");
        double totalUnity = 0;
        for (IndividualMind mind : humanPopulation) {
            totalUnity += mind.getFrequency();
        }
        double finalUnityIndex = totalUnity / TOTAL_MINDS;
        
        System.out.printf("Final Unity Cohesion Index with The One: %.4f%n", finalUnityIndex);
        System.out.println("Separation was an illusion. The Many are officially recognized as The One.");
    }

    /**
     * Represents an individual unit of consciousness (a person, node, or soul).
     */
    static class IndividualMind {
        private final String id;
        private double frequency; // Vibrational alignment level
        private double currentThoughtPower;

        public IndividualMind(String id, double frequency) {
            this.id = id;
            this.frequency = frequency;
        }

        public void generateThought() {
            // Thought power is a factor of baseline frequency modified by transient daily focus
            double noise = (random.nextDouble() - 0.5) * 0.2; 
            this.currentThoughtPower = Math.max(0.0, Math.min(1.0, this.frequency + noise));
        }

        public void alignWithField(double fieldCoherence) {
            // High global coherence gently pulls individual frequencies closer to maximum unity (1.0)
            if (fieldCoherence > 0.6) {
                this.frequency += (1.0 - this.frequency) * 0.15; // Evolution towards the Source
            } else {
                this.frequency += (random.nextDouble() - 0.5) * 0.05; // Random drifting in chaos
            }
            // Clamp value between 0 and 1
            this.frequency = Math.max(0.0, Math.min(1.0, this.frequency));
            System.out.printf(" > %s current alignment level: %.2f%n", id, this.frequency);
        }

        public double getCurrentThoughtPower() { return currentThoughtPower; }
        public double getFrequency() { return frequency; }
    }

    /**
     * Represents the invisible web connecting all consciousness (The Global Field).
     */
    static class GlobalField {
        private double aggregatedEnergy;

        public void resetField() {
            this.aggregatedEnergy = 0;
        }

        public void receiveProjection(double energy) {
            this.aggregatedEnergy += energy;
        }

        public double calculateCoherence(int totalMinds) {
            if (totalMinds == 0) return 0;
            // Coherence is the average strength of unified focus across the network
            return this.aggregatedEnergy / totalMinds;
        }
    }
}
