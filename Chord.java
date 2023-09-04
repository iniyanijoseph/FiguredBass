import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Chord {
    Note[] notes;
    int number;

    int inversion;
    boolean isSeventh;

    int doubled;

    HashSet<int[]> riskyIntervals;

    HashSet<Integer> forbiddenParallels;

    public Chord(Note[] notes, int number, int inversion, boolean isSeventh) {
        this.notes = new Note[4];
        // Double Root to prevent null index
        notes[3] = (notes[3] == null) ? notes[0] : notes[3];
        // Set Inversion
        for (int i = 0; i < 4; i++) {
            this.notes[i] = notes[(i + inversion) % notes.length];
            this.notes[i].octave = i + 2;
        }
        this.number = number;
        this.inversion = inversion;
        this.isSeventh = isSeventh;

        // List out Forbidden Parallels
        forbiddenParallels = new HashSet<>();
        forbiddenParallels.add(2);
        forbiddenParallels.add(5);
        forbiddenParallels.add(7);
        forbiddenParallels.add(1);

        riskyIntervals = new HashSet<>();
    }

    public void updateChord() {
        // Create markers for parallels
        for (int i = 0; i < notes.length; i++) {
            for (int j = i + 1; j < notes.length; j++) {
                if (i < j) {
                    int interval = Note.pureInterval(notes[i], notes[j]);
                    if (forbiddenParallels.contains(interval)) {
                        riskyIntervals.add(new int[] { i, j });
                    }
                }
            }
        }
    }

    public boolean scoreChord() {
        updateChord();

        // Too Few Notes
        for (Note n : notes) {
            if (n == null|| n.octave == -1)
                return false;
        }

        // Incorrect Doubling
        HashMap<String, Integer> noteHash = new HashMap<>();
        for (Note note : notes) {
            if (noteHash.containsKey(note.toString()))
                noteHash.put(note.name, noteHash.get(note.toString()) + 1);
            else
                noteHash.put(note.name, 1);
        }

        // Update the doubled note index
        doubled = -1;

        for (String key : noteHash.keySet()) {
            if (noteHash.get(key) > 2) {
                System.out.println("Note Overly Duplicated+");
                return false;
            }
            if (noteHash.get(key) == 2) {
                for (int i = 0; i < notes.length; i++) {
                    if (notes[i].name.equals(key)) {
                        if (doubled == -1) {
                            doubled = i;
                        }else{
                            return false;
                        }
                    }
                }
            }
        }

        // Mandatory Doubling as Neccessary for I 6/4
        if (number == 1 && inversion == 2) {
            if (notes[doubled].name != Configs.keyScale[0].name) {
                // System.out.println("Not Doubled");
                return false;
            }
        }

        // Check for Objectionable intervals
        for (int i = 1; i < notes.length - 1; i++) {
            // Add more intervals
            if (notes[i].compareTo(notes[i + 1]) < 0) {
                // System.out.println("Voices Crossed");
                return false;
            } else {
                int size = notes[i].compareTo(notes[i + 1]);
                if (size > 8) {
                    // System.out.println("Chord has Forbidden Interval");
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean scoreChordMovement(Chord a, Chord b) {
        // Improper Parallel
        for (int[] i : a.riskyIntervals) {
            if (b.riskyIntervals.contains(i)) { // Doesn't work because .contains uses .equals, which must be overriden
                                                // for arrays
                // System.out.println("Improper Parallel");
                return false;
            }
        }

        // Check for Forbidden Leaps
        for (int i = 0; i < a.notes.length; i++) {
            int interval;
            int intervalQuality;

            interval = a.notes[i].compareTo(b.notes[i]);
            intervalQuality = Note.intervalQuality(a.notes[i], b.notes[i]);

            // Tritone Jump
            if (interval == 4 && intervalQuality == 1 || interval == 5 && intervalQuality == -1) {
                // System.out.println("Tritone Jump");
                return false;
            }
            // Large Leaps
            if (i != 0) {
                if (interval >= 5) {
                    // System.out.println("Large Leap");
                }
            }

            // 7th Scale Degree Resolves to Tonic
            if (a.notes[i].name == Configs.keyScale[6].name) {
                if (b.notes[i].name != Configs.keyScale[0].name) {
                    // System.out.println("7th Scale Degree Doesn't Resolve");
                    return false;
                }
            }

            // 4th Scale Degree should resolve to 3rd Scale Degree
            if (a.notes[i].name == Configs.keyScale[3].name) {
                if (b.notes[i].name != Configs.keyScale[2].name) {
                    // System.out.println("4th Scale Degree Doesn't Resolve");
                    return false;
                }
            }

            // Check if Chordal 7th resolves down by step
            if (a.isSeventh && a.notes[i].isChordalSeventh) {
                if (a.notes[i].compareTo(b.notes[i]) > 0) {
                    // System.out.println("Chordal 7th Resolving Up");
                    return false;
                }

                if (interval > 2) {
                    // System.out.println("Chordal 7th Not Resolving by Step");
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(notes);
    }
}
