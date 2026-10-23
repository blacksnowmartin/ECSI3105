import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.sound.midi.*;

public class NokiaComposerGUI extends JFrame {

    // Screen display for text notation
    private JTextArea screenDisplay;
    
    // Internal tracking arrays for playback
    private ArrayList<String> noteSequence = new ArrayList<>();
    
    // Current defaults for note-entry state
    private int currentDuration = 8; // Default to eighth note
    private int currentOctave = 1;   // Default Nokia Octave 1
    private boolean isSharp = false;

    public NokiaComposerGUI() {
        // 1. Setup the Outer Frame window
        setTitle("Retro Nokia Composer");
        setSize(360, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main phone body layout
        JPanel phoneBody = new JPanel();
        phoneBody.setLayout(new BorderLayout(10, 10));
        phoneBody.setBackground(new Color(40, 55, 71)); // Dark Retro Gray Plastic
        phoneBody.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 2. The Phone Screen Area
        JPanel screenContainer = new JPanel(new BorderLayout());
        screenContainer.setBackground(new Color(145, 175, 130)); // Classic Nokia Green LCD Matrix tint
        screenContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        screenContainer.setPreferredSize(new Dimension(300, 150));

        screenDisplay = new JTextArea("\n  --- NOKIA COMPOSER ---\n  Press numbers to play!\n");
        screenDisplay.setFont(new Font("Monospaced", Font.BOLD, 15));
        screenDisplay.setBackground(new Color(145, 175, 130));
        screenDisplay.setForeground(Color.BLACK);
        screenDisplay.setEditable(false);
        screenDisplay.setLineWrap(true);
        screenDisplay.setWrapStyleWord(true);
        screenContainer.add(screenDisplay, BorderLayout.CENTER);
        
        phoneBody.add(screenContainer, BorderLayout.NORTH);

        // 3. The 12-Key Keypad Layout
        JPanel keypadPanel = new JPanel();
        keypadPanel.setLayout(new GridLayout(5, 3, 10, 10)); // 5 rows to fit control tools
        keypadPanel.setBackground(new Color(40, 55, 71));

        // Define our custom buttons
        JButton btn1 = createKey("1 (C)", "c");
        JButton btn2 = createKey("2 (D)", "d");
        JButton btn3 = createKey("3 (E)", "e");
        JButton btn4 = createKey("4 (F)", "f");
        JButton btn5 = createKey("5 (G)", "g");
        JButton btn6 = createKey("6 (A)", "a");
        JButton btn7 = createKey("7 (B)", "b");
        
        // Rests and length modifiers
        JButton btn8 = new JButton("8 (Len-)");
        JButton btn9 = new JButton("9 (Len+)");
        JButton btn0 = new JButton("0 (Rest)");
        JButton btnStar = new JButton("* (Oct)");
        JButton btnHash = new JButton("# (Sharp)");

        // Command Buttons (Play and Clear)
        JButton btnPlay = new JButton("▶ PLAY");
        JButton btnClear = new JButton("🗑 CLEAR");
        
        // Style Functional modifier buttons
        styleControlBtn(btn8); styleControlBtn(btn9); styleControlBtn(btn0);
        styleControlBtn(btnStar); styleControlBtn(btnHash);
        
        btnPlay.setBackground(new Color(46, 204, 113)); // Bright green
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnClear.setBackground(new Color(231, 76, 60)); // Crimson red
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));

        // 4. Hook up actions for modifiers
        btn8.addActionListener(e -> cycleDuration(false));
        btn9.addActionListener(e -> cycleDuration(true));
        btn0.addActionListener(e -> addRest());
        btnStar.addActionListener(e -> toggleOctave());
        btnHash.addActionListener(e -> toggleSharp());
        
        btnPlay.addActionListener(e -> playMelody());
        btnClear.addActionListener(e -> clearComposer());

        // 5. Populate Keypad Matrix
        keypadPanel.add(btn1); keypadPanel.add(btn2); keypadPanel.add(btn3);
        keypadPanel.add(btn4); keypadPanel.add(btn5); keypadPanel.add(btn6);
        keypadPanel.add(btn7); keypadPanel.add(btn8); keypadPanel.add(btn9);
        keypadPanel.add(btnStar); keypadPanel.add(btn0); keypadPanel.add(btnHash);
        
        // Empty placeholder, Play, and Clear row at the bottom
        keypadPanel.add(btnClear);
        keypadPanel.add(new JLabel("")); // empty middle cell
        keypadPanel.add(btnPlay);

        phoneBody.add(keypadPanel, BorderLayout.CENTER);
        add(phoneBody);
    }

    // Generates standard note buttons (1-7)
    private JButton createKey(String label, String noteName) {
        JButton button = new JButton(label);
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Build RTTTL block format: [Duration][Note][Sharp?][Octave]
                String formattedNote = currentDuration + noteName + (isSharp ? "#" : "") + currentOctave;
                noteSequence.add(formattedNote);
                
                // Instantly preview a short audio burst of the tone when tapped
                playIndividualTone(noteName, isSharp, currentOctave);
                
                // Clear out initial splash text if first note
                if(screenDisplay.getText().contains("NOKIA COMPOSER")) {
                    screenDisplay.setText("");
                }
                
                screenDisplay.append(formattedNote + " ");
                isSharp = false; // Sharp only persists for one key entry
            }
        });
        return button;
    }

    private void styleControlBtn(JButton btn) {
        btn.setBackground(new Color(180, 190, 200));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    // Logic for 8 & 9 keys (Lengths: 2 = Half, 4 = Quarter, 8 = Eighth, 16 = Sixteenth)
    private void cycleDuration(boolean increase) {
        if (increase && currentDuration > 2) currentDuration /= 2;
        else if (!increase && currentDuration < 16) currentDuration *= 2;
        
        JOptionPane.showMessageFactory(null); // Simple HUD alert
        screenDisplay.append("\n[Set Length: " + currentDuration + "]\n");
    }

    private void toggleOctave() {
        currentOctave = (currentOctave == 1) ? 2 : (currentOctave == 2) ? 0 : 1; // loop through 0, 1, 2
        screenDisplay.append("\n[Set Octave: " + currentOctave + "]\n");
    }

    private void toggleSharp() {
        isSharp = !isSharp;
        screenDisplay.append("#");
    }

    private void addRest() {
        noteSequence.add(".");
        screenDisplay.append(". ");
    }

    private void clearComposer() {
        noteSequence.clear();
        screenDisplay.setText("\n  --- SCREEN RESET ---\n");
        currentDuration = 8;
        currentOctave = 1;
        isSharp = false;
    }

    // Instantly previews single tone inside UI
    private void playIndividualTone(String note, boolean sharp, int octave) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            
            // Re-offset Nokia scale context to MIDI numbers
            int midiKey = getMidiKeyNumber(note, sharp, octave);
            
            // Fire short thread to not freeze UI click action
            new Thread(() -> {
                channels[0].programChange(80); // Retro Square Wave blip instrument
                channels[0].noteOn(midiKey, 90);
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                channels[0].noteOff(midiKey);
            }).start();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Full playback chain for the whole typed string sequence
    private void playMelody() {
        if(noteSequence.isEmpty()) return;
        
        // Execute background thread playback so UI interface doesn't freeze up completely
        new Thread(() -> {
            try {
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                Sequence seq = new Sequence(Sequence.PPQ, 4);
                Track track = seq.createTrack();

                // Instrument config
                ShortMessage inst = new ShortMessage();
                inst.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 80, 0);
                track.add(new MidiEvent(inst, 0));

                long currentTick = 1;

                for (String token : noteSequence) {
                    if (token.equals(".")) {
                        currentTick += 4;
                        continue;
                    }

                    // Parse duration numerical digit values
                    String durationStr = token.replaceAll("[^0-9].*", "");
                    int duration = durationStr.isEmpty() ? 4 : Integer.parseInt(durationStr);
                    long noteTicks = 16 / duration;

                    // Parse raw body attributes
                    String body = token.replaceAll("^[0-9]+", "");
                    int targetOctave = Character.getNumericValue(body.charAt(body.length() - 1));
                    boolean sharpCheck = body.contains("#");
                    String noteName = body.replaceAll("[0-9#]", "");

