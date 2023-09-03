public class Configs {
    private static Note key = new Note("a", 0, 2);
    public static Note[] keyScale = Note.generateScale(key);
    public static Note[][] chordTable = Note.generateChordTable(key);

}
