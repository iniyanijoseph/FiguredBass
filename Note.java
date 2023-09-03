public class Note implements Comparable<Note> {
    String name = "";
    int accidental = 0;
    int octave = 0;

    final static String[] naturals = new String[] { "c", "d", "e", "f", "g", "a", "b" };
    final static String[] order = new String[] { "c", "cd", "d", "de", "e", "f", "fg", "g", "ga", "a", "ab", "b" };
    final static int[] ionianScale = new int[] { 2, 2, 1, 2, 2, 2, 1 };

    boolean isChordalSeventh = false;

    public Note(String name, int accidental, int octave) {
        this.name = name;
        this.accidental = accidental;
        this.octave = octave;
    }

    @Override
    public String toString() {
        return this.name + "\t Accidental: " + this.accidental + "\t Octave: " + this.octave;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Note) {
            Note n = (Note) o;
            if (n.name == this.name && n.accidental == this.accidental) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int compareTo(Note n) {
        int thisIndex = Compose.indexOf(naturals, (this.name));
        int nIndex = Compose.indexOf(naturals, n.name);
        if (n.octave > this.octave || (thisIndex < nIndex && this.octave == n.octave)) {
            return distance(this, n);
        } else if (this.octave > n.octave || (thisIndex > nIndex && this.octave == n.octave)) {
            return distance(n, this) * -1;
        }

        return 0;
    }

    public static Note[][] generateChordTable(Note n) {
        Note[][] table = new Note[7][4];
        Note[] baseScale = generateScale(n);

        for (int i = 0; i < table.length; i++) {
            // Add each
            table[i][0] = baseScale[i % 7];
            table[i][1] = baseScale[(i + 2) % 7];
            table[i][2] = baseScale[(i + 4) % 7];
            if (i == 1 || i == 4) // Add chordal seventh for II and V chords
                table[i][3] = baseScale[(i + 6) % 7];
        }

        return table;
    }

    public static Note[] generateScale(Note n) {
        int naturalIndex = Compose.indexOf(naturals, n.name);
        int trueIndex = noteIndex(n);
        Note[] scale = new Note[7];

        for (int j = 0; j < scale.length; j++) {
            scale[j] = new Note("", 0, -1);
            scale[j].name = naturals[naturalIndex % naturals.length];
            scale[j].accidental = trueIndex - Compose.indexOf(order, naturals[naturalIndex]);
            naturalIndex = (naturalIndex + 1) % naturals.length;
            trueIndex = (trueIndex + ionianScale[j % ionianScale.length]) % order.length;
        }

        return scale;
    }

    public static int noteIndex(Note n) {
        int index = Compose.indexOf(order, n.name);
        if (n.accidental != 0) {
            for (int i = 0; i < n.accidental; i++) {
                index += ((n.accidental > 0) ? 1 : -1) % order.length;
            }
        }

        return index;
    }

    public static int distance(Note a, Note b) {
        if (a == null || b == null)
            return -1;

        int oct = a.octave;
        int index = Compose.indexOf(naturals, a.name);
        int count = 1;

        while (oct != b.octave || naturals[index] != b.name) {
            index = (index + 1) % naturals.length;
            if (index - 1 == -1) {
                oct++;
            }
            count++;
        }
        return count;
    }

    public static int pureInterval(Note a, Note b) {
        int index = Compose.indexOf(naturals, a.name);
        int count = 1;

        while (naturals[index] != b.name) {
            index = (index + 1) % naturals.length;
            count++;
        }

        return count;
    }

    public static int intervalQuality(Note a, Note b) {
        int q = b.accidental - a.accidental;

        return q;
    }

    
}
