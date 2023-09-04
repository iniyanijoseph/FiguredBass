import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Note n = new Note("a", 0, 2);
        Note[][] cTable = Note.generateChordTable(n);

        HashSet<String> solutions = new HashSet<>();

        Compose l = new Compose(new Chord[] { new Chord(cTable[1], 2, 2, true), new Chord(cTable[4], 5, 2, true) });

        for (int i = 1; solutions.size() == 0; i++) {
            HashSet<String> seen = new HashSet<>();
            l.recur(seen, 1, i, solutions);
            System.out.println(i);
        }
        System.out.println(solutions);
    }
}