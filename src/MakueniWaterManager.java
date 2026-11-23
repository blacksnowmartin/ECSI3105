import java.util.ArrayList;
import java.util.List;

public class MakueniWaterManager {

    // Core entity representing a sub-county or community cluster
    static class Community {
        String name;
        int population;
        double dailyDemandPerCapita; // In liters
        boolean hasCriticalFacility; // e.g., hospital, major school

        public Community(String name, int population, double dailyDemandPerCapita, boolean hasCriticalFacility) {
            this.name = name;
            this.population = population;
            this.dailyDemandPerCapita = dailyDemandPerCapita;
            this.hasCriticalFacility = hasCriticalFacility;
        }

        public double getTotalRequiredWater() {
            return population * dailyDemandPerCapita;
        }
    }

    public static void main(String[] args) {
        // 1. Initialize available water resource (e.g., Wote Reservoir capacity in liters)
        double availableWaterLiters = 250000.0; 
        System.out.println("=== MAKUENI COUNTY SMART WATER MANAGEMENT SYSTEM ===");
        System.out.printf("Current Reservoir Level: %,.2f Liters%n%n", availableWaterLiters);

        // 2. Define the communities in need
        List<Community> communities = new ArrayList<>();
        communities.add(new Community("Wote Town", 15000, 30.0, true));   // Has a major referral hospital
        communities.add(new Community("Kibwezi", 12000, 25.0, false));
        communities.add(new Community("Makindu", 9000, 25.0, true));     // Has a sub-county hospital
        communities.add(new Community("Kathonzweni", 8000, 20.0, false));

        // 3. Calculate total demand
        double totalDemand = 0;
        for (Community c : communities) {
            totalDemand += c.getTotalRequiredWater();
        }
        System.out.printf("Total County Daily Demand: %,.2f Liters%n", totalDemand);

        // 4. Run the Allocation Logic
        if (availableWaterLiters >= totalDemand) {
            allocateNormal(communities, availableWaterLiters);
        } else {
            allocateRationed(communities, availableWaterLiters);
        }
    }

    // Normal operation allocation
    private static void allocateNormal(List<Community> communities, double availableWater) {
        System.out.println("Status: Normal Operation. Sufficient water available.");
        System.out.println("---------------------------------------------------------");
        for (Community c : communities) {
            double demand = c.getTotalRequiredWater();
            System.out.printf("Allocated to %-15s: %,.2f Liters (100%% of demand)%n", c.name, demand);
        }
    }

    // Emergency rationing allocation algorithm
    private static void allocateRationed(List<Community> communities, double availableWater) {
        System.out.println("Status: CRITICAL SHORTAGE DETECTED! Activating priority rationing...");
        System.out.println("---------------------------------------------------------");

        double remainingWater = availableWater;

        // Step 1: Serve critical facilities first (100% of their base demand)
        System.out.println(">> Phase 1: Allocating to Critical Facilities (Hospitals/Schools)");
        for (Community c : communities) {
            if (c.hasCriticalFacility) {
                double demand = c.getTotalRequiredWater();
                // If demand exceeds remaining water, give whatever is left
                double allocation = Math.min(demand, remainingWater); 
                remainingWater -= allocation;
                System.out.printf("Priority Allocated to %-15s: %,.2f Liters%n", c.name, allocation);
            }
        }

        // Step 2: Distribute remaining water proportionally to non-critical/remaining demand
        System.out.println("\n>> Phase 2: Distributing remaining water proportionally to remaining sectors");
        
        double remainingDemand = 0;
        for (Community c : communities) {
            if (!c.hasCriticalFacility) {
                remainingDemand += c.getTotalRequiredWater();
            }
        }

        if (remainingWater <= 0) {
            System.out.println("CRITICAL FAILURE: No water left for residential areas!");
            return;
        }

        // Calculate rationing factor (e.g., if factor is 0.4, everyone gets 40% of their ask)
        double rationingFactor = Math.min(1.0, remainingWater / remainingDemand);

        for (Community c : communities) {
            if (!c.hasCriticalFacility) {
                double baseDemand = c.getTotalRequiredWater();
                double allocation = baseDemand * rationingFactor;
                System.out.printf("Rationed Allocation to %-15s: %,.2f Liters (%.1f%% of normal demand)%n", 
                                  c.name, allocation, (rationingFactor * 100));
            }
        }
        
        System.out.println("---------------------------------------------------------");
        System.out.printf("Unallocated Reserve Remaining: %.2f Liters%n", Math.max(0, remainingWater - (remainingDemand * rationingFactor)));
    }
}
