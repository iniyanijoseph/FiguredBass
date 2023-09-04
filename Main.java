import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner file = new Scanner(new File("input.in"));
        PrintWriter out = new PrintWriter("realized.out");

        Note[] bassLine = new Note[] {
                new Note("c", 0, 3),
                new Note("d", 0, 3),
                new Note("e", 0, 3),
                new Note("e", 0, 2),
                new Note("b", 0, 2),
                new Note("c", 0, 3),
                new Note("d", 0, 3),
                new Note("e", 0, 3),
                new Note("a", 0, 2)
        };

        int[] inversions = new int[] {
                1, 0, 2, 0, 0, 1, 1, 0, 0
        };

        boolean[] sevenths = new boolean[]{
            false, false, false, false, false, false, false, false, false
        };

        file.close();

        Note[][] cTable = Note.generateChordTable(Configs.keyScale[0]);
        Chord[] chordSet = new Chord[bassLine.length];

        for (int i = 0; i < chordSet.length; i++) {
            chordSet[i] = new Chord(cTable, inversions[i], sevenths[i], bassLine[i]);
        }

        for (Chord chord : chordSet) {
            System.out.println(chord);
        }

        Compose l = new Compose(chordSet, out);

        HashSet<String> solutions = new HashSet<>();

        for (int i = 1; solutions.size() == 0 && i < 200; i++) {
            HashSet<String> seen = new HashSet<>();
            l.recur(seen, 1, i, solutions);
            System.out.println(i + "" + solutions);    
        }
        for (String sol : solutions) {
            out.println(sol);
            System.out.println(sol);
        }
        out.close();
    }
}