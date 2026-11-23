import java.util.HashMap;
import java.util.Map;

public class AIGovernanceSimulation {

    // Represents competing societal priorities (The "Human Politics" input)
    static class SocietalValues {
        double healthWeight;
        double economyWeight;
        double environmentWeight;

        public SocietalValues(double health, double economy, double environment) {
            this.healthWeight = health;
            this.economyWeight = economy;
            this.environmentWeight = environment;
        }
    }

    // Represents a policy choice and its projected impact
    static class PolicyOption {
        String name;
        double cost;
        double healthImpact;
        double economyImpact;
        double environmentImpact;

        public PolicyOption(String name, double cost, double health, double economy, double environment) {
            this.name = name;
            this.cost = cost;
            this.healthImpact = health;
            this.economyImpact = economy;
            this.environmentImpact = environment;
        }

        // The AI calculates a score based ONLY on the human-defined weights
        public double calculateUtilityScore(SocietalValues values) {
            return (healthImpact * values.healthWeight) +
                   (economyImpact * values.economyWeight) +
                   (environmentImpact * values.environmentWeight);
        }
    }

    public static void main(String[] args) {
        // Available policy options
        PolicyOption[] policies = {
            new PolicyOption("Build New Public Hospital", 50.0, 9.0, 2.0, -1.0),
            new PolicyOption("Subsidize Green Energy Grid", 50.0, 3.0, 4.0, 9.5),
            new PolicyOption("Tax Cuts for Tech Startups", 50.0, 0.0, 9.5, -2.0)
        };

        // Scenario 1: A society that prioritizes the Economy
        SocietalValues proEconomyValues = new SocietalValues(0.2, 0.7, 0.1);
        System.out.println("--- AI Decision under Pro-Economy Values ---");
        runGovernanceAI(policies, proEconomyValues);

        // Scenario 2: A society that prioritizes Climate & Health
        SocietalValues greenHealthValues = new SocietalValues(0.4, 0.1, 0.5);
        System.out.println("\n--- AI Decision under Green & Health Values ---");
        runGovernanceAI(policies, greenHealthValues);
    }

    public static void runGovernanceAI(PolicyOption[] policies, SocietalValues values) {
        PolicyOption bestPolicy = null;
        double highestScore = Double.NEGATIVE_INFINITY;

        for (PolicyOption policy : policies) {
            double score = policy.calculateUtilityScore(values);
            System.out.printf("Evaluating '%s' -> Utility Score: %.2f%n", policy.name, score);
            
            if (score > highestScore) {
                highestScore = score;
                bestPolicy = policy;
            }
        }

        if (bestPolicy != null) {
            System.out.println("AI Recommended Policy: " + bestPolicy.name);
        }
    }
}
