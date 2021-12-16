package puzzle;

public class Summary
{
    public Summary(){}

    public Summary(Puzzle startState, String algorithm, PuzzleSolver.Heuristic heuristic,
                   boolean detectDouble, int maxDepthPermitted)
    {
        this.startState = startState;
        this.algorithm = algorithm;
        this.heuristic = heuristic;
        this.detectDouble = detectDouble;
        this.maxDepthPermitted = maxDepthPermitted;
    }

    // Konfiguration
    public Puzzle startState;     // Puzzlezustand zu Beginn
    public String algorithm;      // "Greedy" or "A*"
    public PuzzleSolver.Heuristic heuristic;  // Indikator der verwendeten Heuristik
    public boolean detectDouble;  // Werden wiederkehrende Zustaende erkannt?
    public int maxDepthPermitted; // maximale Tiefe, bis zu dieser gesucht wird

    // Ergebnis
    public Puzzle finalState;     // letzter betrachteter Zustand
    public boolean isSolution;    // Handelt es sich dabei um den Zielzustand?
    public int numExpansions;     // Anzahl der Expandierten Knoten im Suchbaum
    public int maxDepthReached;   // maximal erreichte Tiefe im Suchbaum
    public int queueSize;         // Anzahl der verbleibenden Elemente, nachdem das zuletzt betrachtete entfernt wurde
    public int maxQueueSize;	  // maximale Anzahl an Elementen, die gleichzeitig in der Queue gespeichert wurden
    public int pathLength;        // Laenge des Loesungspfades = Kosten
    public String path;           // Loesungspfad
    public String timePassed;

    // Output
    public String toString()
    {
        return "Puzzle-Start:"
                + startState
                + "Suchalgorithmus: " + algorithm
                + "\nHeuristik: " + (heuristic==PuzzleSolver.Heuristic.MANHATTAN ? "Manhattan" : "Anzahl falscher Kacheln")
                + "\nErkennung wiederkehrender Zustaende: " + (detectDouble ? "Ja" : "Nein")
                + "\nMaximale, erlaubte Suchtiefe: " + maxDepthPermitted
                + "\nSuch-Ende:"
                + finalState
                + "Geloest: " + (isSolution ? "Ja" : "Nein")
                + "\nAnzahl expandierter Knoten: " + numExpansions
                + "\nMaximale, erreichte Suchtiefe: " + maxDepthReached
                + "\nAktuelle Queue-Groesse: " + queueSize
                + "\nMaximal erreichte Queue-Groesse: " + maxQueueSize
                + "\n" + timePassed
                + "\nLaenge des Loesungspafades: " + pathLength
                + "\nLoesungspfad: " + path;
    }
}
