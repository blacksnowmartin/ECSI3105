import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PhoneHomeScreen extends JFrame {

    public PhoneHomeScreen() {
        setTitle("Java Phone Simulator");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Main Phone Panel with dark background
        JPanel phonePanel = new JPanel(new BorderLayout());
        phonePanel.setBackground(new Color(25, 25, 25));

        // 1. Status Bar Panel
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(15, 15, 15));
        statusBar.setPreferredSize(new Dimension(360, 30));

        JLabel timeLabel = new JLabel(" 12:00", JLabel.LEFT);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel statusIconsLabel = new JLabel("📶 🔋 100% ", JLabel.RIGHT);
        statusIconsLabel.setForeground(Color.WHITE);
        statusIconsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        statusBar.add(timeLabel, BorderLayout.WEST);
        statusBar.add(statusIconsLabel, BorderLayout.EAST);

        // Update time dynamically every second
        Timer clockTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                timeLabel.setText(" " + currentTime);
            }
        });
        clockTimer.start();

        // 2. App Grid Panel (Home Screen Icons)
        JPanel appGridPanel = new JPanel(new GridLayout(4, 3, 15, 25));
        appGridPanel.setBackground(new Color(30, 35, 45));
        appGridPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        String[][] apps = {
            {"📞", "Phone"},
            {"💬", "Messages"},
            {"🌐", "Browser"},
            {"📷", "Camera"},
            {"🎵", "Music"},
            {"🗺️", "Maps"},
            {"⚙️", "Settings"},
            {"📁", "Files"},
            {"☁️", "Weather"},
            {"⏰", "Clock"},
            {"calculator", "Calculator"},
            {"🛒", "Store"}
        };

        for (String[] app : apps) {
            JButton appButton = createAppIcon(app[0], app[1]);
            appGridPanel.add(appButton);
        }

        // 3. Dock Panel (Bottom apps)
        JPanel dockPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        dockPanel.setBackground(new Color(20, 20, 20));
        dockPanel.setPreferredSize(new Dimension(360, 90));

        String[][] dockApps = {
            {"📞", "Phone"},
            {"💬", "Messages"},
            {"🌐", "Browser"},
            {"📷", "Camera"}
        };

        for (String[] app : dockApps) {
            JButton dockButton = createAppIcon(app[0], app[1]);
            dockPanel.add(dockButton);
        }

        // Assembly
        phonePanel.add(statusBar, BorderLayout.NORTH);
        phonePanel.add(appGridPanel, BorderLayout.CENTER);
        phonePanel.add(dockPanel, BorderLayout.SOUTH);

        add(phonePanel);
    }

    private JButton createAppIcon(String emoji, String name) {
        JButton button = new JButton("<html><center><span style='font-size:24px;'>" + emoji + "</span><br/><span style='font-size:10px; color:white;'>" + name + "</span></center></html>");
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Opening " + name + "..."));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneHomeScreen frame = new PhoneHomeScreen();
            frame.setVisible(true);
        });
    }
}
