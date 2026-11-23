import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PhoneEightApps extends JFrame {
    private final JPanel mainScreen;
    private final CardLayout cardLayout;

    public PhoneEightApps() {
        setTitle("Smartphone Simulator");
        setSize(380, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainScreen = new JPanel(cardLayout);

        JPanel homeScreenView = createHomeScreen();
        mainScreen.add(homeScreenView, "Home");
        mainScreen.add(createCalculatorApp(), "Calculator");
        mainScreen.add(createClockApp(), "Clock");
        mainScreen.add(createNotesApp(), "Notes");
        mainScreen.add(createWeatherApp(), "Weather");
        mainScreen.add(createContactsApp(), "Contacts");
        mainScreen.add(createFlashlightApp(), "Flashlight");
        mainScreen.add(createDiceApp(), "Dice Roller");
        mainScreen.add(createSettingsApp(), "Settings");

        add(mainScreen);
        cardLayout.show(mainScreen, "Home");
    }

    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(28, 28, 30));

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(20, 20, 20));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel carrier = new JLabel("SimNetwork 5G");
        carrier.setForeground(Color.WHITE);
        JLabel battery = new JLabel("🔋 88%");
        battery.setForeground(Color.WHITE);
        statusBar.add(carrier, BorderLayout.WEST);
        statusBar.add(battery, BorderLayout.EAST);
        panel.add(statusBar, BorderLayout.NORTH);

        JPanel appGrid = new JPanel(new GridLayout(4, 2, 20, 20));
        appGrid.setBackground(new Color(28, 28, 30));
        appGrid.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        appGrid.add(createAppIcon("🧮 Calculator", "Calculator", new Color(255, 159, 10)));
        appGrid.add(createAppIcon("⏰ Clock", "Clock", new Color(94, 92, 230)));
        appGrid.add(createAppIcon("📝 Notes", "Notes", new Color(255, 214, 10)));
        appGrid.add(createAppIcon("☀️ Weather", "Weather", new Color(100, 210, 255)));
        appGrid.add(createAppIcon("👥 Contacts", "Contacts", new Color(52, 199, 89)));
        appGrid.add(createAppIcon("🔦 Flashlight", "Flashlight", new Color(255, 69, 58)));
        appGrid.add(createAppIcon("🎲 Dice Roller", "Dice Roller", new Color(175, 82, 222)));
        appGrid.add(createAppIcon("⚙️ Settings", "Settings", new Color(142, 142, 147)));

        panel.add(appGrid, BorderLayout.CENTER);
        return panel;
    }

    private JButton createAppIcon(String name, String screenName, Color bgColor) {
        JButton button = new JButton(name);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        button.addActionListener(e -> cardLayout.show(mainScreen, screenName));
        return button;
    }

    private JPanel createBottomNavBar() {
        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(20, 20, 20));
        JButton homeButton = new JButton("● Home");
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(Color.DARK_GRAY);
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> cardLayout.show(mainScreen, "Home"));
        navBar.add(homeButton);
        return navBar;
    }

    private JPanel buildAppContainer(String title, JPanel appContent) {
        JPanel container = new JPanel(new BorderLayout());
        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(44, 44, 46));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        container.add(header, BorderLayout.NORTH);
        container.add(appContent, BorderLayout.CENTER);
        container.add(createBottomNavBar(), BorderLayout.SOUTH);
        return container;
    }

    private JPanel createCalculatorApp() {
        JPanel content = new JPanel(new BorderLayout());
        JTextField display = new JTextField("0");
        display.setFont(new Font("SansSerif", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        content.add(display, BorderLayout.NORTH);

        JPanel keys = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton b1 = new JButton("7 + 8");
        JButton b2 = new JButton("Clear");
        b1.addActionListener(e -> display.setText("15"));
        b2.addActionListener(e -> display.setText("0"));
        keys.add(b1); keys.add(b2);
        content.add(keys, BorderLayout.CENTER);
        return buildAppContainer("Calculator", content);
    }

    private JPanel createClockApp() {
        JPanel content = new JPanel(new GridBagLayout());
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));

        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        timer.start();

        content.add(timeLabel);
        return buildAppContainer("Live Clock", content);
    }

    private JPanel createNotesApp() {
        JPanel content = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea("Type your quick thoughts here...");
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        content.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return buildAppContainer("Quick Notes", content);
    }

    private JPanel createWeatherApp() {
        JPanel content = new JPanel(new GridBagLayout());
        JLabel report = new JLabel("☀️ Sunny, 24°C");
        report.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(report);
        return buildAppContainer("Weather Forecast", content);
    }

    private JPanel createContactsApp() {
        JPanel content = new JPanel(new BorderLayout());
        String[] people = {"Mom 📞", "Dad 📞", "Best Friend 📞", "Work Boss 📞"};
        JList<String> list = new JList<>(people);
        list.setFont(new Font("SansSerif", Font.PLAIN, 16));
        content.add(new JScrollPane(list), BorderLayout.CENTER);
        return buildAppContainer("Contacts List", content);
    }

    private JPanel createFlashlightApp() {
        JPanel content = new JPanel(new GridBagLayout());
        JButton toggle = new JButton("OFF");
        toggle.setFont(new Font("SansSerif", Font.BOLD, 24));
        toggle.setBackground(Color.BLACK);
        toggle.setForeground(Color.WHITE);

        toggle.addActionListener(e -> {
            if (toggle.getText().equals("OFF")) {
                toggle.setText("ON");
                toggle.setBackground(Color.YELLOW);
                toggle.setForeground(Color.BLACK);
                content.setBackground(Color.WHITE);
            } else {
                toggle.setText("OFF");
                toggle.setBackground(Color.BLACK);
                toggle.setForeground(Color.WHITE);
                content.setBackground(null);
            }
        });

        content.add(toggle);
        return buildAppContainer("Flashlight Tool", content);
    }

    private JPanel createDiceApp() {
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel outcome = new JLabel("🎲 Press Roll");
        outcome.setFont(new Font("SansSerif", Font.BOLD, 28));
        JButton rollBtn = new JButton("Roll Die");

        rollBtn.addActionListener(e -> {
            int roll = new Random().nextInt(6) + 1;
            outcome.setText("🎲 Result: " + roll);
        });

        content.add(outcome, gbc);
        content.add(rollBtn, gbc);
        return buildAppContainer("Dice Roller", content);
    }

    private JPanel createSettingsApp() {
        JPanel content = new JPanel(new FlowLayout());
        JCheckBox modeToggle = new JCheckBox("Airplane Mode Enabled");
        modeToggle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        content.add(modeToggle);
        return buildAppContainer("System Settings", content);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PhoneEightApps().setVisible(true);
        });
    }
}
