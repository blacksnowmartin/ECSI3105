import java.util.ArrayList;
import java.util.List;

public class MultiAgentGovernance {

    // Represents a proposed law
    static class PolicyBill {
        double taxRate;       // Percentage (e.g., 0.15 to 0.45)
        double publicSpending; // In billions

        public PolicyBill(double taxRate, double publicSpending) {
            this.taxRate = taxRate;
            this.publicSpending = publicSpending;
        }

        @Override
        public String toString() {
            return String.format("Bill [Tax Rate: %.1f%%, Public Spending: $%.1fB]", taxRate * 100, publicSpending);
        }
    }

    // Represents an AI Agent programmed to protect a specific group's interests
    static class FactionAgent {
        String factionName;
        double idealTax;
        double idealSpending;
        double tolerance; // How far from its ideal it is willing to compromise

        public FactionAgent(String name, double idealTax, double idealSpending, double tolerance) {
            this.factionName = name;
            this.idealTax = idealTax;
            this.idealSpending = idealSpending;
            this.tolerance = tolerance;
        }

        // Calculates dissatisfaction (lower is better)
        public double calculateDissatisfaction(PolicyBill bill) {
            double taxDiff = Math.abs(bill.taxRate - idealTax) * 100; // Scale to whole numbers
            double spendDiff = Math.abs(bill.publicSpending - idealSpending);
            return (taxDiff + spendDiff) / 2.0;
        }

        // Votes YES if the bill is within the agent's compromise tolerance
        public boolean vote(PolicyBill bill) {
            double dissatisfaction = calculateDissatisfaction(bill);
            boolean approves = dissatisfaction <= tolerance;
            System.out.printf("  - %-18s | Dissatisfaction: %5.2f | Vote: %s%n", 
                              factionName, dissatisfaction, approves ? "YES" : "NO");
            return approves;
        }
    }

    public static void main(String[] args) {
        // Create AI agents representing competing human interests
        List<FactionAgent> parliament = new ArrayList<>();
        parliament.add(new FactionAgent("Business AI", 0.15, 20.0, 12.0));     // Wants low taxes, low spending
        parliament.add(new FactionAgent("Labor AI", 0.35, 50.0, 12.0));        // Wants high taxes, high spending
        parliament.add(new FactionAgent("Green Future AI", 0.30, 45.0, 14.0)); // Wants moderate-high taxes for green tech

        System.out.println("=== AI Parliament Negotiating a New National Budget ===\n");
        negotiatePolicy(parliament);
    }

    public static void negotiatePolicy(List<FactionAgent> agents) {
        // Start with an extreme proposal (favors Business)
        PolicyBill currentBill = new PolicyBill(0.15, 20.0);
        int maxRounds = 10;

        for (int round = 1; round <= maxRounds; round++) {
            System.out.println("Round " + round + ": Testing " + currentBill);
            int yesVotes = 0;

            for (FactionAgent agent : agents) {
                if (agent.vote(currentBill)) {
                    yesVotes++;
                }
            }

            // Require a majority (more than half) to pass
            if (yesVotes > agents.size() / 2) {
                System.out.println("\n>>> CONSENSUS REACHED! Passed with " + yesVotes + "/" + agents.size() + " votes.");
                System.out.println(">>> Final Enacted Policy: " + currentBill);
                return;
            }

            // If failed, the mediator AI adjusts the bill toward the center
            System.out.println("  -> Bill failed. Adjusting parameters for next round...\n");
            currentBill.taxRate += 0.025;      // Raise tax slightly
            currentBill.publicSpending += 4.0; // Increase spending slightly
        }

        System.out.println("\n>>> DEADLOCK: AI Agents failed to reach a consensus.");
    }
}
