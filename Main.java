import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Note n = new Note("a", 0, 2);
        Note[][] cTable = Note.generateChordTable(n);

        Chord c = new Chord(cTable[1], 2, 2, false);
        Chord d = new Chord(cTable[4], 5, 2, false);

        for (int i = 0; i < c.notes.length; i++) {
            c.notes[i].octave = i;
            d.notes[i].octave = i;
        }

        c.updateChord();
        System.out.println(c);
        d.updateChord();
        System.out.println(d);

        for (int[] interval : c.riskyIntervals) {
            System.out.println(Arrays.toString(interval));
        }

        for (int[] interval : d.riskyIntervals) {
            System.out.println(Arrays.toString(interval));
        }

        System.out.println(cTable[1][0].compareTo(cTable[1][1]));

        c.scoreChord();
        d.scoreChord();

        Chord.scoreChordMovement(c, d);

    }
}