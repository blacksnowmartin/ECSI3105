import javax.sound.midi.*;

public class NokiaComposer {

    // Standard BPM (Beats Per Minute) for the tempo
    private static final int BPM = 180; 

    public static void main(String[] args) {
        System.out.println("📱 Nokia Composer Booting Up...");
        
        // Example Sequence: The iconic Nokia Tune (Gran Vals snippet)
        // Format: [Duration][Note][Octave] 
        // 8 = eighth note, 16 = sixteenth note, 4 = quarter note
        // . = rest, # = sharp
        String[] nokiaTune = {
            "8e2", "8d2", "4f#1", "4g#1", 
            "8b1", "8a1", "4c#1", "4e1", 
            "8a1", "8g1", "4b0", "4d1", 
            "2a1"
        };

        System.out.println("🎶 Playing: Nokia Tune...");
        playMelody(nokiaTune);
    }

    public static void playMelody(String[] notes) {
        try {
            // Initialize the Java MIDI Sequencer and Synthesizer
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            
            Sequence sequence = new Sequence(Sequence.PPQ, 4); // 4 ticks per quarter note
            Track track = sequence.createTrack();

            // Set the instrument to a "Square Wave" or "Blip" sound to mimic retro hardware
            // MIDI Instrument 81 is a Lead 2 (sawtooth) or 80 is Lead 1 (square)
            ShortMessage instrumentChange = new ShortMessage();
            instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 80, 0);
            track.add(new MidiEvent(instrumentChange, 0));

            long currentTick = 1;

            for (String noteToken : notes) {
                if (noteToken.equals(".")) {
                    // It's a rest, just skip forward in time
                    currentTick += 4; 
                    continue;
                }

                // Parse the token (e.g., "8f#1")
                int duration = parseDuration(noteToken);
                int midiKey = parseMidiKey(noteToken);

                // Calculate note length in MIDI ticks based on duration modifier
                // 4 = quarter note (4 ticks), 8 = eighth note (2 ticks), etc.
                long noteTicks = 16 / duration;

                // Note On Event
                ShortMessage noteOn = new ShortMessage();
                noteOn.setMessage(ShortMessage.NOTE_ON, 0, midiKey, 93); // 93 is volume/velocity
                track.add(new MidiEvent(noteOn, currentTick));

                // Note Off Event
                ShortMessage noteOff = new ShortMessage();
                noteOff.setMessage(ShortMessage.NOTE_OFF, 0, midiKey, 0);
                track.add(new MidiEvent(noteOff, currentTick + noteTicks - 1)); // stop just before next note

                currentTick += noteTicks; // Move time forward
            }

            // Feed sequence to the player and start
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(BPM);
            sequencer.start();

            // Keep the program alive while the song plays
            while (sequencer.isRunning()) {
                Thread.sleep(100);
            }

            sequencer.close();
            System.out.println("⏹️ Playback finished.");

        } catch (Exception e) {
            System.out.println("❌ Error playing audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Extracts the duration scale (e.g., "16b1" -> 16)
    private static int parseDuration(String token) {
        String digitPrefix = token.replaceAll("[^0-9].*", "");
        if (digitPrefix.isEmpty()) return 4; // Default to quarter note if omitted
        return Integer.parseInt(digitPrefix);
    }

    // Translates text notes (c, d, e, f, g, a, b) and octaves into MIDI key numbers
    private static int parseMidiKey(String token) {
        // Strip out the duration numbers to look at the note body
        String noteBody = token.replaceAll("^[0-9]+", "");
        
        // Find octave at the end
        int octave = 1; // Default octave scale
        if (Character.isDigit(noteBody.charAt(noteBody.length() - 1))) {
            octave = Character.getNumericValue(noteBody.charAt(noteBody.length() - 1)) + 4; // MIDI offset
        }

        // Isolate note letter and sharp modifier
        String note = noteBody.replaceAll("[0-9]", "");
        
        int baseKey = 0;
        switch (note.toLowerCase()) {
            case "c":  baseKey = 0;  break;
            case "c#": baseKey = 1;  break;
            case "d":  baseKey = 2;  break;
            case "d#": baseKey = 3;  break;
            case "e":  baseKey = 4;  break;
            case "f":  baseKey = 5;  break;
            case "f#": baseKey = 6;  break;
            case "g":  baseKey = 7;  break;
            case "g#": baseKey = 8;  break;
            case "a":  baseKey = 9;  break;
            case "a#": baseKey = 10; break;
            case "b":  baseKey = 11; break;
        }

        return (octave * 12) + baseKey;
    }
}
