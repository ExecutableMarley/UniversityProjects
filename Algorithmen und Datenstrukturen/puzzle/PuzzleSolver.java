package puzzle;

import java.util.*;

import static java.lang.System.exit;

class VolatileAlgorithmException extends RuntimeException
{
    public VolatileAlgorithmException(String message)
    {
        super(message);
    }
}

public class PuzzleSolver
{
    public static int calcScore(Puzzle puzzle, Heuristic heuristic)
    {
        if (heuristic == Heuristic.MANHATTAN)
            return puzzle.manhattanDist();
        if (heuristic == Heuristic.WRONG_TILES)
            return puzzle.countWrongTiles();

        return -1;
    }

    /**
     * @param list A list containing all states that where already evaluated
     * @param p Puzzle Object where we want to know if we already visited that state
     * */
    static boolean checkIfVisited(List<Puzzle> list, Puzzle p)
    {
        //By Comparing the heuristic score first we save some time
        for (Puzzle elem : list)
            if (elem.heuristicScore == p.heuristicScore &&
                    elem.equals(p))
                return true;

        return false;
    }

    static void addToList(Puzzle state, List<Puzzle> visited, StablePriorityQueue<Puzzle> queue, boolean detectDouble)
    {
        //Do Visited check if necessary or skip
        if (detectDouble && checkIfVisited(visited, state))
        {
            return;
        }
        //Add to our queue
        queue.add(state);
        //Add tp visited list if necessary
        if (detectDouble)
            visited.add(state);
    }

    static void addOrUpdateList(Puzzle state, List<Puzzle> done, Queue<Puzzle> queue, boolean detectDouble)
    {
        boolean inQueue = false;

        if (checkIfVisited(done, state))
            return;

        for (Puzzle p : queue)
        {
            if (state.equals(p))
            {
                //Check if we found a shorter path
                if (state.pathScore < p.pathScore)
                {
                    //Set new path and score
                    p.pathScore = state.pathScore;
                    p.path = state.path;
                    inQueue = true;
                    break;
                }
            }
        }
        if (!inQueue)
            queue.add(state);
    }
    //Thread.Sleep wrapped inside an error catching function
    static void Sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param puzzle Initial Puzzle state
     * @param heuristic Indicator which will be used to determine how we will calculate the Heuristic Score
     * @param detectDouble Indicates if we want to check if a new generated state was already visited
     * @param maxDepth Max Depth that the algorithm will explore and expand to
     * @param limit Limit of Nodes the algorithm will process and expand
     * @return Returns a summary Object
     */
    public static Summary greedy(Puzzle puzzle, Heuristic heuristic, boolean detectDouble, int maxDepth, int limit)
    {
        if (puzzle == null || heuristic == null || maxDepth < 0 || limit < 0)
            throw new IllegalArgumentException();

        Timer timer = new Timer();

        Summary summary = new Summary();

        summary.startState = puzzle;
        summary.algorithm  = "Greedy";
        summary.heuristic  = heuristic;
        summary.detectDouble = detectDouble;
        summary.maxDepthPermitted = maxDepth;

        Puzzle curPuzzle = puzzle;

        //Tracks visited nodes
        LinkedList<Puzzle> visited = new LinkedList<>();

        StablePriorityQueue<Puzzle> queue = new StablePriorityQueue<>(
                heuristic == Heuristic.MANHATTAN ? greedyManhattanComparator() : greedyWrongTileComparator());

        queue.add(puzzle);

        //Check if either limit has been reached or the queue is empty
        for (int i = 0; (limit == 0 || i < limit) && queue.peekFirst() != null; i++)
        {
            curPuzzle = queue.pollFirst();

            //System.out.printf("Iteration = [%d], Queue Size = [%d]", i, queue.size());
            //System.out.print(curPuzzle);
            //System.out.printf("Cost = [%d]\n\n", curPuzzle.heuristicScore);

            //If Solution Found
            if (curPuzzle.isSolved())
            {
                //System.out.printf("-----Terminated-----\n\n");

                summary.finalState = curPuzzle;
                summary.isSolution = true;
                summary.numExpansions = i;
                summary.queueSize = queue.size();
                summary.pathLength = curPuzzle.pathScore;
                summary.path = curPuzzle.path;
                summary.timePassed = timer.toString();

                if (!Puzzle.testPath(summary.startState, summary.finalState, summary.path))
                    throw new VolatileAlgorithmException("Greedy Algorithm generated invalid path");

                return summary;
            }

            //Skip expanding if MaxDepth has been reached
            if (curPuzzle.pathScore >= maxDepth)
                continue;

            summary.numExpansions++;

            if (curPuzzle.canMoveLeft())
            {
                addToList(curPuzzle.moveLeft(), visited, queue, detectDouble);
            }
            if (curPuzzle.canMoveRight())
            {
                addToList(curPuzzle.moveRight(), visited, queue, detectDouble);
            }
            if (curPuzzle.canMoveUp())
            {
                addToList(curPuzzle.moveUp(), visited, queue, detectDouble);
            }
            if (curPuzzle.canMoveDown())
            {
                addToList(curPuzzle.moveDown(), visited, queue, detectDouble);
            }

            //Increase maxDepthReached if necessary
            if (curPuzzle.pathScore + 1 > summary.maxDepthReached)
                summary.maxDepthReached = curPuzzle.pathScore + 1;

            //Increase maxQueueSize if necessary
            if (queue.size() > summary.maxQueueSize)
                summary.maxQueueSize = queue.size();

            //Sleep(300);
        }

        //No Solution found
        summary.finalState = curPuzzle;
        summary.isSolution = false;
        summary.queueSize = queue.size();
        summary.pathLength = -1;
        summary.path = null;
        summary.timePassed = timer.toString();

        return summary;
    }

    /**
     * @param puzzle Initial Puzzle state
     * @param heuristic Indicator which will be used to determine how we will calculate the Heuristic Score
     * @param detectDouble Indicates if we want to check if a new generated state was already visited
     * @param maxDepth Max Depth that the algorithm will explore and expand to
     * @param limit Limit of Nodes the algorithm will process and expand
     * @return Returns a summary Object
     */
    public static Summary AStar(Puzzle puzzle, Heuristic heuristic, boolean detectDouble, int maxDepth, int limit)
    {
        if (puzzle == null || heuristic == null || maxDepth < 0 || limit < 0)
            throw new IllegalArgumentException();

        Timer timer = new Timer();

        Summary summary = new Summary();

        summary.startState = puzzle;
        summary.algorithm  = "A*";
        summary.heuristic  = heuristic;
        summary.detectDouble = detectDouble;
        summary.maxDepthPermitted = maxDepth;

        //Tracks nodes where we know the shortest path (As long as heuristic is good ofc)
        LinkedList<Puzzle> done = new LinkedList<>();

        StablePriorityQueue<Puzzle> queue = new StablePriorityQueue<>(
                heuristic == Heuristic.MANHATTAN ? astarManhattanComparator() : astarWrongTileComparator());

        queue.add(puzzle);

        Puzzle curPuzzle = puzzle;

        //Check if either limit has been reached or the queue is empty
        for (int i = 0; (limit == 0 || i < limit) && queue.peek() != null; i++)
        {
            curPuzzle = queue.poll();

            //System.out.printf("Iteration = [%d], Queue Size = [%d]", i, queue.size());
            //System.out.print(curPuzzle);
            //System.out.printf("Cost = [%d]\n\n", curPuzzle.pathScore + curPuzzle.heuristicScore);

            //If Solution Found
            if (curPuzzle.isSolved())
            {
                //System.out.printf("-----Terminated-----\n\n");

                summary.finalState = curPuzzle;
                summary.isSolution = true;
                summary.numExpansions = i;
                summary.queueSize = queue.size();
                summary.pathLength = curPuzzle.pathScore;
                summary.path = curPuzzle.path;
                summary.timePassed = timer.toString();

                if (!Puzzle.testPath(summary.startState, summary.finalState, summary.path))
                    throw new VolatileAlgorithmException("A* Algorithm generated invalid path");

                return summary;
            }

            //Skip expanding if MaxDepth has been reached
            if (curPuzzle.pathScore >= maxDepth)
                continue;

            summary.numExpansions++;

            if (curPuzzle.canMoveLeft())
                addOrUpdateList(curPuzzle.moveLeft(), done, queue, detectDouble);

            if (curPuzzle.canMoveRight())
                addOrUpdateList(curPuzzle.moveRight(), done, queue, detectDouble);

            if (curPuzzle.canMoveUp())
                addOrUpdateList(curPuzzle.moveUp(), done, queue, detectDouble);

            if (curPuzzle.canMoveDown())
                addOrUpdateList(curPuzzle.moveDown(), done, queue, detectDouble);

            //Current Puzzle state is done
            done.add(curPuzzle);

            //Increase maxDepthReached if necessary
            if (curPuzzle.pathScore + 1 > summary.maxDepthReached)
                summary.maxDepthReached = curPuzzle.pathScore + 1;

            //Increase maxQueueSize if necessary
            if (queue.size() > summary.maxQueueSize)
                summary.maxQueueSize = queue.size();

            //Sleep(300);
        }

        //No Solution found
        summary.finalState = curPuzzle;
        summary.isSolution = false;
        summary.queueSize = queue.size();
        summary.pathLength = -1;
        summary.path = null;
        summary.timePassed = timer.toString();

        return summary;
    }

    // hieran bitte nichts veraendern
    public enum Heuristic
    {
        WRONG_TILES,
        MANHATTAN
    }

    static Comparator<Puzzle> greedyManhattanComparator()
    {
        return new Comparator<Puzzle>()
        {
            @Override
            public int compare(Puzzle o1, Puzzle o2)
            {
                return Integer.compare(o1.heuristicScore, o2.heuristicScore);
            }
        };
    }

    static Comparator<Puzzle> greedyWrongTileComparator()
    {
        return new Comparator<Puzzle>()
        {
            @Override
            public int compare(Puzzle o1, Puzzle o2)
            {
                return Integer.compare(o1.wrongTiles, o2.wrongTiles);
            }
        };
    }

    static Comparator<Puzzle> astarManhattanComparator()
    {
        return new Comparator<Puzzle>()
        {
            @Override
            public int compare(Puzzle o1, Puzzle o2)
            {
                return Integer.compare(o1.pathScore + o1.heuristicScore, o2.pathScore + o2.heuristicScore);
            }
        };
    }

    static Comparator<Puzzle> astarWrongTileComparator()
    {
        return new Comparator<Puzzle>()
        {
            @Override
            public int compare(Puzzle o1, Puzzle o2)
            {
                return Integer.compare(o1.pathScore + o1.wrongTiles, o2.pathScore + o2.wrongTiles);
            }
        };
    }
}

