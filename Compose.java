import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

public class Compose {
    Chord[] chords;
    PrintWriter out;

    public Compose(Chord[] chords, PrintWriter out) {
        this.chords = chords;
        this.out = out;
    }

    public boolean score() {
        boolean score = true;
        for (int i = 0; i < chords.length - 1; i++) {
            score = score && chords[i].scoreChord() && Chord.scoreChordMovement(chords[i], chords[i + 1]);
            if (!score)
                return false;
        }
        score = score && chords[chords.length - 1].scoreChord();
        return score;
    }

    public void recur(HashSet<String> seen, int depth, int maxDepth, HashSet<String> solutions) {
        if (depth >= maxDepth)
            return;

        String state = Arrays.toString(chords);
        if (chords[0].toString().equals("[c  1  3, a  0  3, a  0 4, e  0  5]"))
            out.println(state);

        if (score()) {
            seen.add(state);
            solutions.add(state);
            return;
        }
        if (seen.contains(state)) {
            return;
        }
        seen.add(state);

        for (Chord c : chords) {
            for (int voice = 1; voice < 4; voice++) {
                c.notes[voice].octave = voice + 2;
                recur(seen, depth + 1, maxDepth, solutions);
                for (Note note : Configs.chordTable[c.number - 1]) {
                    if (note != null) {
                        c.notes[voice].name = note.name;
                        recur(seen, depth + 1, maxDepth, solutions);
                    }
                }
                c.notes[voice].octave = voice + 3;
                recur(seen, depth + 1, maxDepth, solutions);
                for (Note note : Configs.chordTable[c.number - 1]) {
                    if (note != null) {
                        c.notes[voice].name = note.name;
                        recur(seen, depth + 1, maxDepth, solutions);
                    }
                }
            }
        }
    }

    public static int indexOf(String[] arr, String element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(element))
                return i;
        }
        return -1;
    }
}
