import java.util.Arrays;
import java.util.HashSet;

public class Compose {
    Chord[] chords;
    

    public Compose(Chord[] chords) {
        this.chords = chords;
    }

    public double score() {
        double score = 1;
        for (int i = 0; i < chords.length - 1; i++) {
            score *= chords[i].scoreChord();
            score *= Chord.scoreChordMovement(chords[i], chords[i + 1]);
        }
        score *= chords[chords.length - 1].scoreChord();
        return score;
    }

    public void recur(HashSet<String> seen, HashSet<String> solutions) {
        String state = Arrays.toString(Configs.chordTable);
        seen.add(state);

        if (score() > 0) {
            solutions.add(state);
            return;
        }
        if (seen.contains(state)) {
            return;
        }

        seen.add(state);

        for (Chord c : chords) {
            for (int voice = 0; voice < 4; voice++) {
                c.notes[voice].octave = voice + 1;
                recur(solutions, seen);
                c.notes[voice].octave = voice + 2;
                recur(solutions, seen);
                for (Note note : Configs.chordTable[chords[voice].number - 1]) {
                    c.notes[voice].name = note.name;
                    recur(solutions, seen);
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
