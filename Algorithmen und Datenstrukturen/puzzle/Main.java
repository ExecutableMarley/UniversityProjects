package puzzle;

public class Main
{
    /* Vergessen Sie nicht, die nachfolgenden Behauptungen zu pruefen!
     * Wahr:   qX = true;
     * Falsch: qX = false;
     */

    // Behauptung q1: "Greedy findet NICHT immer eine vorhandene Loesung, aber
    //                wenn eine Loesung gefunden wird, ist der Loesungsweg optimal."
    public static Boolean q1 = false; // zu beantworten mit true oder false

    // Behauptung q2: "Greedy findet immer eine Loesung."
    /**Greedy findet immer einen vorhanden lösungsweg aber die Frage definiert nicht eindeutig, dass es einen lösungsweg geben muss*/
    public static Boolean q2 = false; // zu beantworten mit true oder false

    // Behauptung q3: "Wenn A* einen Loesungsweg gefunden hat, ist dieser immer optimal."
    /**Hängt von der verwendeten heuristic ab. Praktisch gesehen immer optimal wen diese optimistisch gewählt wurde.
     * Aber bei schlechter (nicht optimaler) heuristic kann ein optimaler lösungsweg nicht garantiert werden.
     * In diesem programm wird a stern jedoch immer optimale lösungspfade liefern.*/
    public static Boolean q3 = false; // zu beantworten mit true oder false

    // Behauptung q4: "Jedes Puzzle ist loesbar."
    public static Boolean q4 = false; // zu beantworten mit true oder false

    // Behauptung q5: "Die Methode countWrongTiles() in Puzzle.java ist KEINE zulaessige Heuristik."
    /**Keine optimale heuristic aber definitiv zulässig und optimistisch*/
    public static Boolean q5 = false; // zu beantworten mit true oder false


    // Hier ist Platz fuer Ihre Tests
    public static void main(String[] args)
    {
        Summary summary;

        //[Static Solvable Puzzle]
        System.out.println("\n[Static Solvable Puzzle]\n");

        Puzzle example1 = new Puzzle(0, 4, 5, 2, 7, 1, 3, 8, 6);

        System.out.println(PuzzleSolver.greedy(example1, PuzzleSolver.Heuristic.MANHATTAN, true, 100, 20000));
        //System.out.printf("Validating Path: [%s] = [%s]\n", summary.path, Puzzle.testPath(example1, Puzzle.goal, summary.path));

        System.out.println();

        System.out.println(PuzzleSolver.AStar(example1, PuzzleSolver.Heuristic.MANHATTAN, true, 100, 20000));
        //System.out.printf("Validating Path: [%s] = [%s]\n", summary.path, Puzzle.testPath(example1, Puzzle.goal, summary.path));


        /* Randomized Solvable Puzzle */
        System.out.println("\n[Randomized Solvable Puzzle]\n");

        Puzzle example2 = Puzzle.randomize(new Puzzle(Puzzle.goal), 2000);

        System.out.println(PuzzleSolver.greedy(example2, PuzzleSolver.Heuristic.MANHATTAN, true, 100, 20000));
        //System.out.printf("Validating Path: [%s] = [%s]\n", summary.path, Puzzle.testPath(example1, Puzzle.goal, summary.path));

        System.out.println();

        System.out.println(PuzzleSolver.AStar(example2, PuzzleSolver.Heuristic.MANHATTAN, true, 100, 20000));
        //System.out.printf("Validating Path: [%s] = [%s]\n", summary.path, Puzzle.testPath(example1, Puzzle.goal, summary.path));

        //[Impossible Puzzle]
        System.out.println("\n[Impossible Puzzle]\n");

        Puzzle example3 = new Puzzle(0, 1,2,3,4,5,6,7,8);

        System.out.println(PuzzleSolver.greedy(example3, PuzzleSolver.Heuristic.MANHATTAN, true, 100, 20000));

        System.out.println();

        System.out.println(PuzzleSolver.AStar(example3, PuzzleSolver.Heuristic.MANHATTAN, true, 100, 20000));
    }
}
