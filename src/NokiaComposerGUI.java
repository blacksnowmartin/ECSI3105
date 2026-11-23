import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.sound.midi.*;

public class NokiaComposerGUI extends JFrame {

    private final JTextArea screenDisplay;
    private final ArrayList<String> noteSequence = new ArrayList<>();
    private int currentDuration = 8;
    private int currentOctave = 1;
    private boolean isSharp = false;

    public NokiaComposerGUI() {
        super("Retro Nokia Composer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel phoneBody = new JPanel(new BorderLayout(10, 10));
        phoneBody.setBackground(new Color(40, 55, 71));
        phoneBody.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel screenContainer = new JPanel(new BorderLayout());
        screenContainer.setBackground(new Color(145, 175, 130));
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

        JPanel keypadPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        keypadPanel.setBackground(new Color(40, 55, 71));

        JButton btn1 = createKey("1 (C)", "c");
        JButton btn2 = createKey("2 (D)", "d");
        JButton btn3 = createKey("3 (E)", "e");
        JButton btn4 = createKey("4 (F)", "f");
        JButton btn5 = createKey("5 (G)", "g");
        JButton btn6 = createKey("6 (A)", "a");
        JButton btn7 = createKey("7 (B)", "b");

        JButton btn8 = new JButton("8 (Len-)");
        JButton btn9 = new JButton("9 (Len+)");
        JButton btn0 = new JButton("0 (Rest)");
        JButton btnStar = new JButton("* (Oct)");
        JButton btnHash = new JButton("# (Sharp)");
        JButton btnPlay = new JButton("▶ PLAY");
        JButton btnClear = new JButton("🗑 CLEAR");

        styleControlBtn(btn8);
        styleControlBtn(btn9);
        styleControlBtn(btn0);
        styleControlBtn(btnStar);
        styleControlBtn(btnHash);

        btnPlay.setBackground(new Color(46, 204, 113));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFont(new Font("Arial", Font.BOLD, 14));

        btnClear.setBackground(new Color(231, 76, 60));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));

        btn8.addActionListener(e -> cycleDuration(false));
        btn9.addActionListener(e -> cycleDuration(true));
        btn0.addActionListener(e -> addRest());
        btnStar.addActionListener(e -> toggleOctave());
        btnHash.addActionListener(e -> toggleSharp());
        btnPlay.addActionListener(e -> playMelody());
        btnClear.addActionListener(e -> clearComposer());

        keypadPanel.add(btn1); keypadPanel.add(btn2); keypadPanel.add(btn3);
        keypadPanel.add(btn4); keypadPanel.add(btn5); keypadPanel.add(btn6);
        keypadPanel.add(btn7); keypadPanel.add(btn8); keypadPanel.add(btn9);
        keypadPanel.add(btnStar); keypadPanel.add(btn0); keypadPanel.add(btnHash);
        keypadPanel.add(btnClear); keypadPanel.add(new JLabel("")); keypadPanel.add(btnPlay);

        phoneBody.add(keypadPanel, BorderLayout.CENTER);
        add(phoneBody);
        pack();
        setLocationRelativeTo(null);
    }

    private JButton createKey(String label, String noteName) {
        JButton button = new JButton(label);
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);

        button.addActionListener((ActionEvent e) -> {
            String formattedNote = currentDuration + noteName + (isSharp ? "#" : "") + currentOctave;
            noteSequence.add(formattedNote);
            playIndividualTone(noteName, isSharp, currentOctave);

            if (screenDisplay.getText().contains("NOKIA COMPOSER")) {
                screenDisplay.setText("");
            }

            screenDisplay.append(formattedNote + " ");
            isSharp = false;
        });
        return button;
    }

    private void styleControlBtn(JButton btn) {
        btn.setBackground(new Color(180, 190, 200));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    private void cycleDuration(boolean increase) {
        if (increase) {
            currentDuration = (currentDuration > 2) ? currentDuration / 2 : currentDuration;
        } else {
            currentDuration = (currentDuration < 16) ? currentDuration * 2 : currentDuration;
        }
        screenDisplay.append("\n[Set Length: " + currentDuration + "]\n");
    }

    private void toggleOctave() {
        if (currentOctave == 1) {
            currentOctave = 2;
        } else if (currentOctave == 2) {
            currentOctave = 0;
        } else {
            currentOctave = 1;
        }
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

    private void playIndividualTone(String note, boolean sharp, int octave) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            int midiKey = getMidiKeyNumber(note, sharp, octave);

            new Thread(() -> {
                channels[0].programChange(80);
                channels[0].noteOn(midiKey, 90);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                channels[0].noteOff(midiKey);
                synth.close();
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playMelody() {
        if (noteSequence.isEmpty()) {
            return;
        }

        new Thread(() -> {
            try {
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                Sequence seq = new Sequence(Sequence.PPQ, 4);
                Track track = seq.createTrack();

                ShortMessage instrument = new ShortMessage();
                instrument.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 80, 0);
                track.add(new MidiEvent(instrument, 0));

                long currentTick = 1;

                for (String token : noteSequence) {
                    if (token.equals(".")) {
                        currentTick += 4;
                        continue;
                    }

                    String durationStr = token.replaceAll("[^0-9].*", "");
                    int duration = durationStr.isEmpty() ? 4 : Integer.parseInt(durationStr);
                    long noteTicks = 16 / duration;

                    String body = token.replaceAll("^[0-9]+", "");
                    int targetOctave = Character.isDigit(body.charAt(body.length() - 1))
                            ? Character.getNumericValue(body.charAt(body.length() - 1))
                            : currentOctave;
                    boolean sharpCheck = body.contains("#");
                    String noteName = body.replaceAll("[0-9#]", "");
                    int midiKey = getMidiKeyNumber(noteName, sharpCheck, targetOctave);

                    ShortMessage noteOn = new ShortMessage();
                    noteOn.setMessage(ShortMessage.NOTE_ON, 0, midiKey, 90);
                    track.add(new MidiEvent(noteOn, currentTick));

                    ShortMessage noteOff = new ShortMessage();
                    noteOff.setMessage(ShortMessage.NOTE_OFF, 0, midiKey, 0);
                    track.add(new MidiEvent(noteOff, currentTick + noteTicks - 1));

                    currentTick += noteTicks;
                }

                sequencer.setSequence(seq);
                sequencer.setTempoInBPM(120);
                sequencer.start();

                while (sequencer.isRunning()) {
                    Thread.sleep(50);
                }

                sequencer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private int getMidiKeyNumber(String note, boolean sharp, int octave) {
        int base = 0;
        switch (note.toLowerCase()) {
            case "c": base = 0; break;
            case "d": base = 2; break;
            case "e": base = 4; break;
            case "f": base = 5; break;
            case "g": base = 7; break;
            case "a": base = 9; break;
            case "b": base = 11; break;
            default: base = 0; break;
        }

        if (sharp) {
            base += 1;
        }

        return (octave + 4) * 12 + base;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NokiaComposerGUI gui = new NokiaComposerGUI();
            gui.setVisible(true);
        });
    }
}

