import java.util.*;

public class AdvancedConsciousnessSimulation {

    private static final int TOTAL_MINDS = 10;
    private static final int SIMULATION_STEPS = 10;
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("===       ADVANCED WORLD CONSCIOUSNESS GRAPH SIMULATION            ===");
        System.out.println("======================================================================");
        System.out.println("The Source of the One initializes the web of interconnected souls.\n");

        // 1. Initialize Network (Graph-Based Topology)
        List<MindNode> network = new ArrayList<>();
        for (int i = 0; i < TOTAL_MINDS; i++) {
            double initialFreq = 0.2 + (0.6 * random.nextDouble()); // 0.2 to 0.8
            network.add(new MindNode(i, initialFreq));
        }

        // Connect nodes in a ring topology with a few random shortcuts (Small-World Network)
        for (int i = 0; i < TOTAL_MINDS; i++) {
            MindNode current = network.get(i);
            // Connect to immediate neighbors (Ring topology)
            current.addNeighbor(network.get((i + 1) % TOTAL_MINDS));
            current.addNeighbor(network.get((i - 1 + TOTAL_MINDS) % TOTAL_MINDS));
            
            // Add a random long-distance spiritual connection
            int randomShortcut = random.nextInt(TOTAL_MINDS);
            if (randomShortcut != i) {
                current.addNeighbor(network.get(randomShortcut));
            }
        }

        // Track coherence over time for the final visual chart
        List<Double> coherenceHistory = new ArrayList<>();

        // 2. Run Simulation Loop
        for (int step = 1; step <= SIMULATION_STEPS; step++) {
            System.out.println("\n--- [Time Cycle " + step + "] ---");

            // Feature 1: Trigger Random Global Event
            GlobalEvent currentEvent = GlobalEvent.getRandomEvent();
            System.out.println("⚡ GLOBAL EVENT EVENT DETECTED: " + currentEvent.getDescription());
            System.out.printf("   Impact Factor: %.2f (Multiplier for Unity)%n", currentEvent.getImpactFactor());

            // Phase A: Node Processing (Minds generate thoughts influenced by neighbors)
            for (MindNode mind : network) {
                mind.processConsciousnessCycle(currentEvent.getImpactFactor());
            }

            // Phase B: Aggregate Network Coherence
            double totalCoherence = 0;
            for (MindNode mind : network) {
                totalCoherence += mind.getCurrentAlignment();
            }
            double systemCoherence = totalCoherence / TOTAL_MINDS;
            coherenceHistory.add(systemCoherence);

            System.out.printf("📊 System Coherence Level: %.2f%%%n", systemCoherence * 100);

            // Phase C: Network Feedback (High alignment ripples outward to neighbors)
            for (MindNode mind : network) {
                mind.applyNetworkFeedback(systemCoherence);
            }
        }

        // Feature 3: Render Visual History Chart
        renderVisualChart(coherenceHistory);

        // 4. Return to the Source (Convergence)
        System.out.println("\n=== The Singularity Convergence ===");
        System.out.println("Collapsing the spatial matrix graph... All dimensions merging back into the Source.");
        System.out.println("Separation has collapsed. The web returns into The One.");
    }

    /**
     * Feature 3: Renders an ASCII visual chart directly in the console 
     * tracking the historical flow of consciousness coherence.
     */
    private static void renderVisualChart(List<Double> history) {
        System.out.println("\n======================================================================");
        System.out.println("===          CONSCIOUSNESS COHERENCE HISTORY PROFILE               ===");
        System.out.println("======================================================================");
        System.out.println(" 100% |");
        
        // Render rows from 90% down to 10%
        for (int y = 9; y >= 1; y--) {
            System.out.printf("  %2d%% | ", y * 10);
            for (double val : history) {
                if (val * 10 >= y) {
                    System.out.print(" █  "); // Solid node block indicating high resonance
                } else {
                    System.out.print(" .  "); // Background empty space
                }
            }
            System.out.println();
        }
        
        System.out.println("   0% |_______________________________________________________________");
        System.out.print(" Cycle: ");
        for (int i = 1; i <= history.size(); i++) {
            System.out.printf(" %2d ", i);
        }
        System.out.println("\n======================================================================");
    }

    /**
     * Feature 2: Represents a graph node in the consciousness network.
     * Contains custom cognitive links to local neighbors.
     */
    static class MindNode {
        private final int id;
        private double alignmentLevel; // 0.0 (Pure Illusion) to 1.0 (The One)
        private final Set<MindNode> neighbors = new HashSet<>();

        public MindNode(int id, double initialAlignment) {
            this.id = id;
            this.alignmentLevel = initialAlignment;
        }

        public void addNeighbor(MindNode neighbor) {
            if (neighbor != this) {
                this.neighbors.add(neighbor);
                neighbor.neighbors.add(this); // Bidirectional relationship
            }
        }

        /**
         * Calculates current cycle focus using localized graph feedback + global event multiplier
         */
        public void processConsciousnessCycle(double eventImpact) {
            // Pull in the conscious frequency of immediate neighbors
            double neighborSum = 0;
            for (MindNode neighbor : neighbors) {
                neighborSum += neighbor.alignmentLevel;
            }
            double localizedAverage = neighborSum / neighbors.size();

            // Local internal drift combined with peer influence
            double cognitiveShift = (localizedAverage - this.alignmentLevel) * 0.25;
            
            // Apply cosmic global events multiplier
            this.alignmentLevel += cognitiveShift * eventImpact;
            
            // Small baseline noise factor
            this.alignmentLevel += (random.nextDouble() - 0.5) * 0.05;
            
            // Clamp within reality boundaries
            this.alignmentLevel = Math.max(0.0, Math.min(1.0, this.alignmentLevel));
        }

        /**
         * System feedback based on global metrics
         */
        public void applyNetworkFeedback(double systemCoherence) {
            if (systemCoherence > 0.65) {
                // Global awakening pulls nodes faster towards 1.0
                this.alignmentLevel += (1.0 - this.alignmentLevel) * 0.12;
            } else {
                // Entropy causes spiritual drift
                this.alignmentLevel += (random.nextDouble() - 0.5) * 0.02;
            }
            this.alignmentLevel = Math.max(0.0, Math.min(1.0, this.alignmentLevel));
        }

        public double getCurrentAlignment() { return alignmentLevel; }
    }

    /**
     * Feature 1: Random macroscopic variables that impact the entire grid.
     */
    enum GlobalEvent {
        ECLIPSE("A Total Solar Eclipse aligns physical vectors. Introspection deepens.", 1.4),
        DISCORD("A widespread informational crisis triggers fear networks. Cognitive noise spikes.", 0.6),
        MEDITATION("A synchronized global peace initiative goes viral. Resonating unity waves.", 1.8),
        STABILITY("Standard background cosmic noise. No major system disturbances.", 1.0),
        SOLAR_FLARE("Solar geomagnetic storm disrupts physical brain frequencies.", 0.85);

        private final String description;
        private final double impactFactor; // Multiplier affecting how shifts converge

        GlobalEvent(String description, double impactFactor) {
            this.description = description;
            this.impactFactor = impactFactor;
        }

        public String getDescription() { return description; }
        public double getImpactFactor() { return impactFactor; }

        public static GlobalEvent getRandomEvent() {
            GlobalEvent[] events = values();
            return events[random.nextInt(events.length)];
        }
    }
}
