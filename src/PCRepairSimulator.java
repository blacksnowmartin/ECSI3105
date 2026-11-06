import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PCRepairSimulator {

    // --- Component Classes ---
    static abstract class HardwarePart {
        protected String name;
        protected boolean isBroken;
        protected String issueDescription;
        protected String fixAction;

        public HardwarePart(String name, String issueDescription, String fixAction) {
            this.name = name;
            this.isBroken = Math.random() < 0.5; // 50% chance a part starts broken
            this.issueDescription = issueDescription;
            this.fixAction = fixAction;
        }

        public String getName() { return name; }
        public boolean isBroken() { return isBroken; }
        public String getIssueDescription() { return issueDescription; }
        
        public void fix() {
            this.isBroken = false;
        }
    }

    static class CPU extends HardwarePart {
        public CPU() {
            super("Processor (CPU)", "PC turns on but immediately shuts down due to overheating.", "Reapply thermal paste and reseat the CPU cooler.");
        }
    }

    static class RAM extends HardwarePart {
        public RAM() {
            super("Memory (RAM)", "PC turns on, fans spin, but there is no display and it beeps repeatedly.", "Reseat the RAM sticks into their slots or try a different slot.");
        }
    }

    static class GPU extends HardwarePart {
        public GPU() {
            super("Graphics Card (GPU)", "Screen shows visual artifacts, weird lines, or no signal while the PC stays running.", "Ensure the PCI-e power cables are firmly plugged in, or replace the GPU if fried.");
        }
    }

    static class Storage extends HardwarePart {
        public Storage() {
            super("Storage Drive (SSD/HDD)", "PC boots directly into the BIOS or displays 'No Boot Device Found'.", "Check the SATA/NVMe connections, or replace the corrupted drive.");
        }
    }

    static class PSU extends HardwarePart {
        public PSU() {
            super("Power Supply (PSU)", "PC is completely dead. No lights, no fans, no sound when pushing the power button.", "Flip the PSU switch to 'I', verify power outlet, or swap out the dead power supply unit.");
        }
    }

    // --- Main Simulator Engine ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<HardwarePart> components = new ArrayList<>();
        
        // Initialize PC components
        components.add(new CPU());
        components.add(new RAM());
        components.add(new GPU());
        components.add(new Storage());
        components.add(new PSU());

        System.out.println("=========================================");
        System.out.println("   WELCOME TO THE PC REPAIR SIMULATOR    ");
        System.out.println("=========================================\n");
        System.out.println("Booting up the computer system diagnostics...\n");

        // Check if anything is actually broken
        boolean brokenSystem = false;
        for (HardwarePart part : components) {
            if (part.isBroken()) {
                brokenSystem = true;
                break;
            }
        }

        if (!brokenSystem) {
            System.out.println("🎉 System diagnostics passed! Everything is running perfectly.");
            scanner.close();
            return;
        }

        // Repair loop
        while (true) {
            System.out.println("--- System Status ---");
            int brokenCount = 0;
            for (int i = 0; i < components.size(); i++) {
                HardwarePart part = components.i(i);
                if (part.isBroken()) {
                    System.out.println("[" + (i + 1) + "] " + part.getName() + ": ❌ ERROR DETECTED");
                    System.out.println("    Symptoms: " + part.getIssueDescription());
                    brokenCount++;
                } else {
                    System.out.println("[" + (i + 1) + "] " + part.getName() + ": ✅ Operational");
                }
            }

            if (brokenCount == 0) {
                System.out.println("\n=========================================");
                System.out.println(" 🌟 SUCCESS! All parts are fully fixed! 🌟");
                System.out.println("      The PC boots safely into OS.       ");
                System.out.println("=========================================");
                break;
            }

            System.out.print("\nSelect a component number to repair (or 0 to quit): ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid input. Please enter a valid number.");
                continue;
            }

            if (choice == 0) {
                System.out.println("Exiting simulator. Good luck with your tech repairs!");
                break;
            }

            if (choice < 1 || choice > components.size()) {
                System.out.println("⚠️ Invalid selection. Please choose a number between 1 and " + components.size());
                continue;
            }

            HardwarePart selectedPart = components.get(choice - 1);
            if (!selectedPart.isBroken()) {
                System.out.println("\n🛠️ " + selectedPart.getName() + " is already working perfectly. No fix needed!\n");
            } else {
                System.out.println("\n🔧 Attempting Fix on " + selectedPart.getName() + "...");
                System.out.println("👉 Action taken: " + selectedPart.fixAction);
                selectedPart.fix();
                System.out.println("✨ Success! The component status has updated.\n");
            }
        }

        scanner.close();
    }
}
