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
        System.out.println(seen.size());
        String state = Arrays.toString(chords);
        double score = score();
        if (score > 0.5) {
            System.out.println("\t***" + state);
            solutions.add(state);
            return;
        }
        if (seen.contains(state)) {
            return;
        }
        seen.add(state);

        for (Chord c : chords) {
            for (int voice = 0; voice < 4; voice++) {
                int prevOctave = c.notes[voice].octave;
                c.notes[voice].octave = voice + 1;
                recur(seen, solutions);
                c.notes[voice].octave = voice + 2;
                recur(seen, solutions);
                c.notes[voice].octave = prevOctave;
                for (Note note : Configs.chordTable[c.number - 1]) {
                    String prevName = c.notes[voice].name;
                    c.notes[voice].name = note.name;
                    recur(seen, solutions);
                    c.notes[voice].name = prevName;
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
