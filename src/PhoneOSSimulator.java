import java.util.*;
import java.util.concurrent.*;

// ==========================================
// CORE OS COMPONENTS
// ==========================================

enum Permission {
    CAMERA, LOCATION, INTERNET, CONTACTS
}

enum ProcessState {
    RUNNING, SUSPENDED, TERMINATED
}

// Represents a running application instance (Process)
abstract class App {
    protected String appName;
    protected Set<Permission> requiredPermissions = new HashSet<>();

    public App(String appName) {
        this.appName = appName;
    }

    public String getAppName() { return appName; }
    public Set<Permission> getRequiredPermissions() { return requiredPermissions; }

    public abstract void launch(Scanner scanner, NotificationManager nm);
}

// Background task runner mimicking an OS event loop
class NotificationManager implements Runnable {
    private final BlockingQueue<String> notificationQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public void postNotification(String sender, String message) {
        notificationQueue.offer("[" + sender + "] " + message);
    }

    public void shutdown() {
        running = false;
    }

    @Override
    public void run() {
        while (running || !notificationQueue.isEmpty()) {
            try {
                String notification = notificationQueue.poll(500, TimeUnit.MILLISECONDS);
                if (notification != null) {
                    System.out.println("\n🔔 [NOTIFICATION SYSTEM] " + notification);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Security and Access Management
class SecurityManager {
    private final Map<String, Set<Permission>> grantedPermissions = new HashMap<>();

    public void grantPermission(String appName, Permission perm) {
        grantedPermissions.computeIfAbsent(appName, k -> new HashSet<>()).add(perm);
    }

    public boolean hasPermission(String appName, Permission perm) {
        return grantedPermissions.containsKey(appName) && grantedPermissions.get(appName).contains(perm);
    }

    public void requestPermissions(Scanner scanner, App app) {
        for (Permission perm : app.getRequiredPermissions()) {
            if (!hasPermission(app.getAppName(), perm)) {
                System.out.print("🛡️  [SECURITY] Grant " + app.getAppName() + " access to " + perm + "? (yes/no): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("yes")) {
                    grantPermission(app.getAppName(), perm);
                    System.out.println("✅ Permission Granted.");
                } else {
                    System.out.println("❌ Permission Denied.");
                }
            }
        }
    }
}

// Kernel managing RAM and Processes
class Kernel {
    private final Map<String, App> installedApps = new HashMap<>();
    private final Map<String, ProcessState> activeProcesses = new HashMap<>();
    private final SecurityManager securityManager = new SecurityManager();
    private final NotificationManager notificationManager = new NotificationManager();
    private final ExecutorService osThreadExecutor = Executors.newSingleThreadExecutor();

    public Kernel() {
        // Start background notification daemon
        osThreadExecutor.submit(notificationManager);
    }

    public void installApp(App app) {
        installedApps.put(app.getAppName().toLowerCase(), app);
    }

    public void launchApp(String name, Scanner scanner) {
        App app = installedApps.get(name.toLowerCase());
        if (app == null) {
            System.out.println("⚠️ App not found.");
            return;
        }

        // Security Checkpoint
        securityManager.requestPermissions(scanner, app);

        // Process State Transition
        activeProcesses.put(app.getAppName(), ProcessState.RUNNING);
        System.out.println("\n🚀 Launching " + app.getAppName() + "...");
        
        try {
            app.launch(scanner, notificationManager);
        } catch (Exception e) {
            System.out.println("💥 " + app.getAppName() + " crashed unexpectedly!");
        } finally {
            activeProcesses.put(app.getAppName(), ProcessState.TERMINATED);
            System.out.println("⬇️ " + app.getAppName() + " closed.");
        }
    }

    public void listApps() {
        System.out.println("\n📱 Installed Apps:");
        installedApps.keySet().forEach(name -> System.out.println(" - " + name.toUpperCase()));
    }

    public void showSystemStatus() {
        System.out.println("\n📊 --- SYSTEM STATUS ---");
        System.out.println("RAM Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " MB / " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " MB");
        System.out.println("Active Tasks:");
        activeProcesses.forEach((app, state) -> System.out.println(" 🟢 " + app + " -> State: " + state));
    }

    public void shutdown() {
        System.out.println("\n🔌 Shutting down OS Kernel...");
        notificationManager.shutdown();
        osThreadExecutor.shutdown();
    }
}

// ==========================================
// SYSTEM APPLICATIONS (CONCRETE IMPLEMENTATIONS)
// ==========================================

class CameraApp extends App {
    public CameraApp() {
        super("Camera");
        requiredPermissions.add(Permission.CAMERA);
        requiredPermissions.add(Permission.LOCATION);
    }

    @Override
    public void launch(Scanner scanner, NotificationManager nm) {
        System.out.println("[Camera] Viewfinder active. Type 'snap' to take a picture, or 'exit' to close.");
        while (true) {
            System.out.print("📸 Camera > ");
            String cmd = scanner.nextLine().trim();
            if (cmd.equalsIgnoreCase("exit")) break;
            if (cmd.equalsIgnoreCase("snap")) {
                System.out.println("[Camera] *Click* Photo captured at 41.40338, 2.17403 (Location Metadata Attached).");
                nm.postNotification("Camera", "New photo saved to Gallery.");
            } else {
                System.out.println("[Camera] Unknown command.");
            }
        }
    }
}

class BrowserApp extends App {
    public BrowserApp() {
        super("Browser");
        requiredPermissions.add(Permission.INTERNET);
    }

    @Override
    public void launch(Scanner scanner, NotificationManager nm) {
        System.out.println("[Browser] Connected to World Wide Web. Enter a URL to visit, or 'exit' to close.");
        while (true) {
            System.out.print("🌐 Browser > ");
            String cmd = scanner.nextLine().trim();
            if (cmd.equalsIgnoreCase("exit")) break;
            
            System.out.println("[Browser] Fetching data from: " + cmd + " ...");
            // Simulating background data download network delay
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            System.out.println("[Browser] Page loaded successfully.");
            nm.postNotification("Browser", "Finished downloading assets from " + cmd);
        }
    }
}

// ==========================================
// MAIN ENTRY POINT
// ==========================================

public class PhoneOSSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.out.println("🔋 OS Initialized. Booting UI...").toString() == null ? "" : "yes");
        scanner = new Scanner(System.in); 
        Kernel kernel = new Kernel();

        // Register apps natively built for the device
        kernel.installApp(new CameraApp());
        kernel.installApp(new BrowserApp());

        System.out.println("\n=================================");
        System.out.println("   WELCOME TO J-OS v2.6 SMARTPHONE ");
        System.out.println("=================================");

        boolean running = true;
        while (running) {
            System.out.println("\n🏠 [Home Screen] Commands: 'apps', 'open [app_name]', 'status', 'exit'");
            System.out.print("🤖 OS Prompt > ");
            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("apps")) {
                kernel.listApps();
            } else if (choice.toLowerCase().startsWith("open ")) {
                String appName = choice.substring(5);
                kernel.launchApp(appName, scanner);
            } else if (choice.equalsIgnoreCase("status")) {
                kernel.showSystemStatus();
            } else if (choice.equalsIgnoreCase("exit")) {
                running = false;
            } else {
                System.out.println("⚠️ Unknown system command.");
            }
        }

        kernel.shutdown();
        System.out.println("👋 Goodbye. Phone powered down.");
        scanner.close();
    }
}
