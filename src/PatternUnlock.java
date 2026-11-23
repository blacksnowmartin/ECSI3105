import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PatternUnlock extends JFrame {
    
    // The correct pattern sequence (0 to 8 corresponding to the 3x3 grid)
    // 0 1 2
    // 3 4 5
    // 6 7 8
    private final List<Integer> CORRECT_PATTERN = List.of(0, 1, 2, 5); 

    public PatternUnlock() {
        setTitle("Phone Pattern Unlock Simulation");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main layout setup
        setLayout(new BorderLayout());
        
        JLabel statusLabel = new JLabel("Draw your pattern to unlock", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(statusLabel, BorderLayout.NORTH);

        PatternPanel patternPanel = new PatternPanel(CORRECT_PATTERN, statusLabel);
        add(patternPanel, BorderLayout.CENTER);
    }

    public static void main(String[] academ) {
        // Run the GUI code on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new PatternUnlock().setVisible(true);
        });
    }
}

class PatternPanel extends JPanel implements MouseListener, MouseMotionListener {
    private final int GRID_SIZE = 3;
    private final int DOT_RADIUS = 15;
    private final int HIT_RADIUS = 30; // Radius to trigger a dot connection

    private Point[] dots = new Point[9];
    private List<Integer> currentPattern = new ArrayList<>();
    private List<Integer> correctPattern;
    
    private Point currentMousePos = null;
    private Color lineColor = new Color(0, 122, 255); // Default iOS-like blue
    private JLabel statusLabel;
    private boolean isDrawing = false;
    private boolean inputLocked = false;

    public PatternPanel(List<Integer> correctPattern, JLabel statusLabel) {
        this.correctPattern = correctPattern;
        this.statusLabel = statusLabel;
        
        setBackground(new Color(28, 28, 30)); // Dark background theme
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Initialize dot coordinates dynamically based on panel dimensions
        int cellWidth = getWidth() / GRID_SIZE;
        int cellHeight = getHeight() / GRID_SIZE;
        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int x = col * cellWidth + cellWidth / 2;
                int y = row * cellHeight + cellHeight / 2;
                dots[index++] = new Point(x, y);
            }
        }

        // 2. Draw connecting lines
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(lineColor);
        for (int i = 0; i < currentPattern.size() - 1; i++) {
            Point p1 = dots[currentPattern.get(i)];
            Point p2 = dots[currentPattern.get(i + 1)];
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // 3. Draw live line to the cursor if drawing is in progress
        if (isDrawing && currentMousePos != null && !currentPattern.isEmpty()) {
            Point lastDot = dots[currentPattern.get(currentPattern.size() - 1)];
            g2.drawLine(lastDot.x, lastDot.y, currentMousePos.x, currentMousePos.y);
        }

        // 4. Draw the 9 grid dots
        for (int i = 0; i < dots.length; i++) {
            Point d = dots[i];
            if (currentPattern.contains(i)) {
                // Highlighted state
                g2.setColor(lineColor);
                g2.fillOval(d.x - DOT_RADIUS, d.y - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);
                
                // Outer ring effect
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(d.x - (DOT_RADIUS + 8), d.y - (DOT_RADIUS + 8), (DOT_RADIUS + 8) * 2, (DOT_RADIUS + 8) * 2);
            } else {
                // Default idle state
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(d.x - DOT_RADIUS, d.y - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);
            }
        }
    }

    private void checkHit(Point p) {
        if (inputLocked) return;

        for (int i = 0; i < dots.length; i++) {
            if (dots[i] != null && !currentPattern.contains(i)) {
                // Determine if mouse pointer is inside the acceptable hit radius of a dot
                if (p.distance(dots[i]) <= HIT_RADIUS) {
                    currentPattern.add(i);
                    repaint();
                    break;
                }
            }
        }
    }

    // --- Mouse Event Implementations ---

    @Override
    public void mousePressed(MouseEvent e) {
        if (inputLocked) return;
        
        isDrawing = true;
        currentPattern.clear();
        lineColor = new Color(0, 122, 255);
        statusLabel.setText("Drawing pattern...");
        statusLabel.setForeground(Color.BLACK);
        checkHit(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isDrawing || inputLocked) return;
        
        currentMousePos = e.getPoint();
        checkHit(e.getPoint());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isDrawing || inputLocked) return;
        
        isDrawing = false;
        currentMousePos = null;

        if (currentPattern.isEmpty()) {
            statusLabel.setText("Draw your pattern to unlock");
            return;
        }

        // Validate pattern matches pre-set rule
        if (currentPattern.equals(correctPattern)) {
            lineColor = new Color(52, 199, 89); // Success Green
            statusLabel.setText("✔ Unlocked Successfully!");
            statusLabel.setForeground(lineColor);
            inputLocked = true;
        } else {
            lineColor = new Color(255, 59, 48); // Error Red
            statusLabel.setText("❌ Wrong Pattern. Try again.");
            statusLabel.setForeground(lineColor);
            inputLocked = true;
            
            // Automatically reset the grid after 1 second if wrong
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    currentPattern.clear();
                    lineColor = new Color(0, 122, 255);
                    statusLabel.setText("Draw your pattern to unlock");
                    statusLabel.setForeground(Color.BLACK);
                    inputLocked = false;
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        repaint();
    }

    // Unused mouse listener requirements
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
