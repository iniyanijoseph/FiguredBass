import java.util.Arrays;
import java.util.HashSet;

public class Compose {
    Chord[] chords;

    public Compose(Chord[] chords) {
        this.chords = chords;
    }

    public boolean score() {
        boolean score = true;
        for (int i = 0; i < chords.length - 1; i++) {
            score = score && chords[i].scoreChord() && Chord.scoreChordMovement(chords[i], chords[i + 1]);
            if(!score)
                return false;
        }
        score = score && chords[chords.length - 1].scoreChord();
        return score;
    }

    public void recur(HashSet<String> seen, int depth, int maxDepth, HashSet<String> solutions) {
        if(depth >= maxDepth){
            return;
        }

        String state = Arrays.toString(chords);

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
                c.notes[voice].octave = voice + 1;
                recur(seen, depth + 1, maxDepth, solutions);
                c.notes[voice].octave = voice + 2;
                recur(seen, depth + 1, maxDepth, solutions);
                for (Note note : Configs.chordTable[c.number - 1]) {
                    c.notes[voice].name = note.name;
                    recur(seen, depth + 1, maxDepth, solutions);
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
