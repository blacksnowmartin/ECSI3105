import java.util.Scanner;
import java.util.Random;

public class KenyaPoliticsGame {

    // Player State
    private static String candidateName;
    private static String politicalParty;
    private static int funds = 500000;       // Campaign Budget in KSh
    private static int trust = 50;           // Public Trust percentage (0-100)
    private static int coalitionPower = 40;  // Alliance strength (0-100)
    private static int currentWeek = 1;
    private static final int TOTAL_WEEKS = 6;

    // Regional Vote Targets (Targeting majority out of 100 points per region)
    private static int votesNairobi = 30;
    private static int votesMountKenya = 20;
    private static int votesRiftValley = 25;
    private static int votesNyanzaCoast = 15;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================");
        System.out.println("      WELCOME TO THE KENYA ELECTION SIMULATOR    ");
        System.out.println("=================================================");
        System.out.print("Enter your Candidate Name: ");
        candidateName = scanner.nextLine();
        System.out.print("Enter your Party/Coalition Name: ");
        politicalParty = scanner.nextLine();

        System.out.println("\nHabari " + candidateName + "! You are the flagbearer for " + politicalParty + ".");
        System.out.println("You have " + TOTAL_WEEKS + " weeks to campaign across the regions before Election Day.");
        System.out.println("Balance your funds, build alliances, and win the hearts of the voters.\n");

        while (currentWeek <= TOTAL_WEEKS) {
            showStatus();
            mainMenu(scanner);
            currentWeek++;
        }

        triggerElectionDay();
        scanner.close();
    }

    private static void showStatus() {
        System.out.println("-------------------------------------------------");
        System.out.println(" WEEK " + currentWeek + " OF " + TOTAL_WEEKS + " | Campaign Status");
        System.out.println("-------------------------------------------------");
        System.out.println(" Funds         : KSh " + funds);
        System.out.println(" Public Trust  : " + trust + "%");
        System.out.println(" Coalition Alignment: " + coalitionPower + "%");
        System.out.println("\n Current Projected Regional Support (Target: >50% to win):");
        System.out.println(" [1] Nairobi County      : " + votesNairobi + "%");
        System.out.println(" [2] Mount Kenya Region  : " + votesMountKenya + "%");
        System.out.println(" [3] Rift Valley Region  : " + votesRiftValley + "%");
        System.out.println(" [4] Nyanza & Coast Base : " + votesNyanzaCoast + "%");
        System.out.println("-------------------------------------------------");
    }

    private static void mainMenu(Scanner scanner) {
        System.out.println("Choose your primary strategy for this week:");
        System.out.println("1. Hold a Mega Rally (Boosts votes, costs heavy funds)");
        System.out.println("2. Town Hall Meeting / Inter-Community Dialogue (Boosts trust, costs low funds)");
        System.out.println("3. Negotiate with Regional Kingpins (Boosts coalition strength)");
        System.out.println("4. Launch a Social Media & Radio Campaign (Broad impact)");
        System.out.print("Select an action (1-4): ");
        
        int choice = getValidInput(scanner, 1, 4);
        System.out.println();

        switch (choice) {
            case 1:
                holdMegaRally(scanner);
                break;
            case 2:
                holdTownHall();
                break;
            case 3:
                negotiateCoalition();
                break;
            case 4:
                mediaCampaign();
                break;
        }
        
        // Random political event at the end of each week
        triggerRandomEvent();
    }

    private static void holdMegaRally(Scanner scanner) {
        if (funds < 150000) {
            System.out.println("❌ Not enough funds for a Mega Rally! Choose a cheaper strategy.");
            holdTownHall();
            return;
        }

        System.out.println("Where would you like to host the Mega Rally?");
        System.out.println("1. Nairobi (Kamukunji Grounds)\n2. Mount Kenya\n3. Rift Valley\n4. Nyanza & Coast");
        System.out.print("Select region (1-4): ");
        int region = getValidInput(scanner, 1, 4);

        funds -= 150000;
        int boost = 15 + new Random().nextInt(11); // 15-25% boost

        switch (region) {
            case 1:
                votesNairobi = Math.min(100, votesNairobi + boost);
                System.out.println("🏟️ Earthshaking rally in Nairobi! Turnout was massive.");
                break;
            case 2:
                votesMountKenya = Math.min(100, votesMountKenya + boost);
                System.out.println("🏔️ Successful rally at the mountain. Local elders gave their blessings.");
                break;
            case 3:
                votesRiftValley = Math.min(100, votesRiftValley + boost);
                System.out.println("🌾 Large rallies across the Rift. Ground looks solid.");
                break;
            case 4:
                votesNyanzaCoast = Math.min(100, votesNyanzaCoast + boost);
                System.out.println("🌊 Coastal and lakeside turnouts exceeded expectations!");
                break;
        }
        trust = Math.min(100, trust + 5);
    }

    private static void holdTownHall() {
        if (funds < 30000) {
            System.out.println("⚠️ Extremely low on funds! Borrowing small local contributions.");
            funds += 20000;
        } else {
            funds -= 30000;
        }
        trust = Math.min(100, trust + 15);
        votesNairobi = Math.min(100, votesNairobi + 3);
        System.out.println("🤝 You held grassroots town halls. Citizens appreciate your direct listening approach.");
    }

    private static void negotiateCoalition() {
        if (funds < 50000) {
            System.out.println("❌ You need at least KSh 50,000 for logistics and stakeholder dinners.");
            return;
        }
        funds -= 50000;
        coalitionPower = Math.min(100, coalitionPower + 20);
        votesMountKenya = Math.min(100, votesMountKenya + 8);
        votesRiftValley = Math.min(100, votesRiftValley + 8);
        System.out.println("👔 Closed-door negotiations successful! Key regional kingpins endorsed your coalition.");
    }

    private static void mediaCampaign() {
        funds -= 60000;
        votesNairobi = Math.min(100, votesNairobi + 6);
        votesMountKenya = Math.min(100, votesMountKenya + 6);
        votesRiftValley = Math.min(100, votesRiftValley + 6);
        votesNyanzaCoast = Math.min(100, votesNyanzaCoast + 6);
        System.out.println("📻 Your voice is everywhere! Vernacular radio stations and TikTok hashtags are trending.");
    }

    private static void triggerRandomEvent() {
        System.out.println("\n📢 [BREAKING NEWS EVENT] 📢");
        int eventType = new Random().nextInt(3);

        switch (eventType) {
            case 0:
                System.out.println("An unverified fake news scandal emerges online about your running mate!");
                trust = Math.max(0, trust - 12);
                coalitionPower = Math.max(0, coalitionPower - 10);
                break;
            case 1:
                System.out.println("A rival coalition principal defects to your side, declaring 'Unbwogable' support!");
                coalitionPower = Math.min(100, coalitionPower + 15);
                votesNyanzaCoast = Math.min(100, votesNyanzaCoast + 10);
                break;
            case 2:
                System.out.println("Well-wishers from the diaspora organize a massive fundraising dinner for your party.");
                funds += 250000;
                System.out.println("+ KSh 250,000 added to your campaign chest.");
                break;
        }
        System.out.println("=================================================\n");
    }

    private static void triggerElectionDay() {
        System.out.println("=================================================");
        System.out.println("            🗳️ ELECTION DAY HAS ARRIVED 🗳️        ");
        System.out.println("=================================================");
        System.out.println("The IEBC is tallying final votes. Processing data...");
        
        try { Thread.sleep(2000); } catch (InterruptedException e) { }

        // Factor in Coalition Strength and Public Trust into the final vote weights
        double multiplier = (trust + coalitionPower) / 100.0;
        
        double finalNairobi = votesNairobi * multiplier;
        double finalMountKenya = votesMountKenya * multiplier;
        double finalRiftValley = votesRiftValley * multiplier;
        double finalNyanzaCoast = votesNyanzaCoast * multiplier;

        double overallVotePercentage = (finalNairobi + finalMountKenya + finalRiftValley + finalNyanzaCoast) / 4.0;
        overallVotePercentage = Math.min(100.0, Math.max(15.0, overallVotePercentage + 15)); // baseline adjustment

        System.out.printf("\nOfficial IEBC Certified Presidential Results:\n");
        System.out.printf("Candidate: %s (%s)\n", candidateName, politicalParty);
        System.out.printf("Total Valid Votes Secured: %.2f%%\n\n", overallVotePercentage);

        if (overallVotePercentage >= 50.1) {
            System.out.println("🎉 CONGRATULATIONS! You have hit the '50% + 1' constitutional threshold!");
            System.out.println("You are officially declared the 6th Elected President of the Republic of Kenya! 🇰🇪");
        } else {
            System.out.println("❌ You did not secure the 50% + 1 constitutional absolute majority.");
