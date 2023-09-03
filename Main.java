import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Note n = new Note("a", 0, 2);
        Note[][] cTable = Note.generateChordTable(n);

        HashSet<String> seen = new HashSet<>();
        HashSet<String> solutions = new HashSet<>();

        Compose l = new Compose(new Chord[] { new Chord(cTable[1], 2, 2, false), new Chord(cTable[4], 5, 2, false) });
        l.recur(seen, solutions);
        System.out.println(solutions);
    }
}