import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhoneUnlockScreen extends JFrame {
    // Set your correct 4-digit unlock pin here
    private static final String CORRECT_PIN = "1234";
    
    private JPasswordField pinDisplay;
    private JLabel statusLabel;
    private StringBuilder enteredPin;

    public PhoneUnlockScreen() {
        enteredPin = new StringBuilder();

        // 1. Window setup
        setTitle("Phone Unlock Simulation");
        setSize(320, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(28, 28, 30)); // Modern dark theme

        // 2. Status Label (Top Message)
        statusLabel = new JLabel("Enter PIN to Unlock", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);

        // 3. PIN Display (Hidden Text Area)
        pinDisplay = new JPasswordField();
        pinDisplay.setEditable(false);
        pinDisplay.setHorizontalAlignment(JTextField.CENTER);
        pinDisplay.setFont(new Font("SansSerif", Font.BOLD, 24));
        pinDisplay.setBackground(new Color(44, 44, 46));
        pinDisplay.setForeground(Color.WHITE);
        pinDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Wrap display in a panel for comfortable margins
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(new Color(28, 28, 30));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        displayPanel.add(pinDisplay, BorderLayout.CENTER);
        
        // Combined Top view
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(28, 28, 30));
        topPanel.add(statusLabel);
        topPanel.add(displayPanel);
        add(topPanel, BorderLayout.NORTH);

        // 4. Button Grid Layout (4x3 Keypad)
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 15, 15));
        keypadPanel.setBackground(new Color(28, 28, 30));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // Keypad button definitions
        String[] buttons = {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "CLR", "0", "OK"
        };

        for (String label : buttons) {
            JButton button = createCircleButton(label);
            button.addActionListener(new ButtonClickListener());
            keypadPanel.add(button);
        }

        add(keypadPanel, BorderLayout.CENTER);
    }

    // Custom helper to make stylized phone UI buttons
    private JButton createCircleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Style depending on action or numeric code
        if (text.equals("CLR") || text.equals("OK")) {
            button.setBackground(new Color(72, 72, 74));
            button.setForeground(Color.LIGHT_GRAY);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
        } else {
            button.setBackground(new Color(44, 44, 46));
            button.setForeground(Color.WHITE);
        }
        
        return button;
    }

    // Handles button clicks
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "CLR":
                    // Reset input
                    enteredPin.setLength(0);
                    pinDisplay.setText("");
                    statusLabel.setText("Enter PIN to Unlock");
                    statusLabel.setForeground(Color.WHITE);
                    break;
                    
                case "OK":
                    // Validate input PIN
                    if (enteredPin.toString().equals(CORRECT_PIN)) {
                        statusLabel.setText("🔓 Phone Unlocked!");
                        statusLabel.setForeground(Color.GREEN);
                        JOptionPane.showMessageDialog(PhoneUnlockScreen.this, "Welcome back!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        statusLabel.setText("❌ Incorrect PIN. Try Again.");
                        statusLabel.setForeground(Color.RED);
                        // Automatically clear the wrong attempts
                        enteredPin.setLength(0);
                        pinDisplay.setText("");
                    }
                    break;
                    
                default:
                    // Stop appending if maximum common length is achieved before pressing OK
                    if (enteredPin.length() < 8) { 
                        enteredPin.append(command);
                        pinDisplay.setText(enteredPin.toString());
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        // Run UI initialization on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PhoneUnlockScreen app = new PhoneUnlockScreen();
            app.setVisible(true);
        });
    }
}
