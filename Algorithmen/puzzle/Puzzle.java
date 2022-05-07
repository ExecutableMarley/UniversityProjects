package puzzle;

import java.util.Random;

public class Puzzle
{
    public int[][] state;
    //Position Of empty Block
    int zeroX;
    int zeroY;
    //
    int heuristicScore;
    int wrongTiles;
    int pathScore; //The actual dist this node will cost
    String path;


    //[Statics]
    final static int max_x = 2;
    final static int max_y = 2;

    // gesuchter Zielzustand
    public static Puzzle goal = new Puzzle(new int[][]
            {
                    {1, 2, 3},
                    {8, 0, 4},
                    {7, 6, 5}
            });

    // initialisiere Puzzle mit gegebenen Werten
    public Puzzle (int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8)
    {
        this(new int[][] {
                        {x0, x1, x2},
                        {x3, x4, x5},
                        {x6, x7, x8}
                });
    }

    public Puzzle (int[][] state, int pathScore, String path)
    {
        this.state = state;
        this.pathScore = pathScore;
        this.path = path;
        //Search zero pos
        for (int i = 0; i < 3;i++)
            for (int j = 0; j < 3; j++)
            {
                if (this.state[i][j] == 0)
                {
                    zeroX = j;
                    zeroY = i;
                    break;
                }
            }
        //Im not happy about this
        if (goal != null)
        {
            heuristicScore = manhattanDist();
            wrongTiles = countWrongTiles();
        }
        else
        {
            heuristicScore = 0;
            wrongTiles = 0;
        }
    }

    // initialisiere Puzzle mit 2D-Array
    public Puzzle (int[][] state)
    {
        this(state, 0, "");
    }

    //Init with Puzzle
    public Puzzle (Puzzle p)
    {
        this(deepArrayCopy(p.state), p.pathScore, p.path);
    }

    // Zaehlung der falsch platzierten Kacheln 1 bis 8
    public int countWrongTiles()
    {
        int iWrongTiles = 0;
        for (int i = 0; i < 3;i++)
            for (int j = 0; j < 3; j++)
            {
                if (this.state[i][j] != 0 && this.state[i][j] != goal.state[i][j])
                    iWrongTiles++;
            }

        return iWrongTiles;
    }

    // Berechnung der Summe aller (vertikalen und horizontalen) Distanzen der Kacheln 1 bis 8 zur jeweiligen Zielposition
    public int manhattanDist()
    {
        int dist = 0;

        for (int i = 1; i < 9; i++)
            dist += this.searchPosition(i).ManhattanDistance(goal.searchPosition(i));

        return dist;
    }

    public boolean canMoveLeft()
    {
        return zeroX > 0;
    }

    public boolean canMoveRight()
    {
        return zeroX < 2;
    }

    public boolean canMoveUp()
    {
        return zeroY > 0;
    }

    public boolean canMoveDown()
    {
        return zeroY < 2;
    }

    public Puzzle moveLeft()
    {
        if (!canMoveLeft())
            throw new ArrayIndexOutOfBoundsException();

        return new Puzzle(swap(deepArrayCopy(state), zeroX, zeroY, zeroX - 1, zeroY), pathScore + 1, path + "L");
    }

    public Puzzle moveRight()
    {
        if (!canMoveRight())
            throw new ArrayIndexOutOfBoundsException();

        return new Puzzle(swap(deepArrayCopy(state), zeroX, zeroY, zeroX + 1, zeroY), pathScore + 1, path + "R");
    }

    public Puzzle moveUp()
    {
        if (!canMoveUp())
            throw new ArrayIndexOutOfBoundsException();

        return new Puzzle(swap(deepArrayCopy(state), zeroX, zeroY, zeroX, zeroY - 1), pathScore + 1, path + "U");
    }

    public Puzzle moveDown()
    {
        if (!canMoveDown())
            throw new ArrayIndexOutOfBoundsException();

        return new Puzzle(swap(deepArrayCopy(state), zeroX, zeroY, zeroX, zeroY + 1), pathScore + 1, path + "D");
    }

    //Checks if the Puzzle p represents the same state
    public boolean equals(Puzzle p)
    {
        for (int i = 0; i < 3;i++)
            for (int j = 0; j < 3; j++)
            {
                if (this.state[i][j] != p.state[i][j])
                    return false;
            }
        return true;
    }

    // Ausgabe des Zustands als String
    public String toString()
    {
        String str = "\n";
        for(int r=0; r<3; r++)
        {
            str += "[";
            for(int c=0; c<3; c++)
            {
                str += state[r][c];
                if(c<2) str += ", ";
            }
            str += "]\n";
        }
        return str;
    }

    //Returns the position of a value
    Vector2D searchPosition(int elem)
    {
        for (int i = 0; i < 3;i++)
            for (int j = 0; j < 3; j++)
            {
                if (state[i][j] == elem)
                    return new Vector2D(j, i);
            }
        //Return Pos: [0,0]
        return new Vector2D();
    }

    //Returns true if puzzle equals goal
    public boolean isSolved()
    {
        return equals(goal);
    }

    //Swaps array elements | [x1][y1] <-> [x2][y2].
    static int[][] swap(int[][] arr, int x1, int y1, int x2, int y2)
    {
        int temp = arr[y1][x1];
        arr[y1][x1] = arr[y2][x2];
        arr[y2][x2] = temp;
        return arr;
    }
    //used to create a deep copy of a 2d array. The array referenz is returned to allow compact statements
    static int [][]deepArrayCopy(int array[][])
    {
        int newArr[][] = new int[array.length][array[0].length];
        for (int i = 0; i < array.length;i++)
            for (int j = 0; j < array[0].length;j++)
                newArr[i][j] = array[i][j];

        return newArr;
    }
    //Sets a new goal
    static public void setGoal(Puzzle goal)
    {
        Puzzle.goal = goal;
    }

    //Used to generate a random init state from a goal state
    static Puzzle randomize(Puzzle puzzle, int swaps)
    {
        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < swaps; i++)
        {
            switch (random.nextInt(4))
            {
                case 0: if (puzzle.canMoveLeft()) puzzle = puzzle.moveLeft();
                        else puzzle.moveRight(); break;
                case 1: if (puzzle.canMoveRight()) puzzle = puzzle.moveRight();
                        else puzzle.moveLeft(); break;
                case 2: if (puzzle.canMoveUp()) puzzle = puzzle.moveUp();
                        else puzzle.moveDown(); break;
                case 3: if (puzzle.canMoveDown()) puzzle = puzzle.moveDown();
                        else puzzle.moveUp(); break;
            }
        }
        puzzle.pathScore = 0;
        puzzle.path = "";

        return puzzle;
    }

    //Used to verify the paths that we will find
    static boolean testPath(Puzzle init, Puzzle goal, String path)
    {
        Puzzle cur = init;
        try
        {
            for (int i = 0; i < path.length(); i++)
            {
                if (path.charAt(i) == 'L')
                    cur = cur.moveLeft();
                if (path.charAt(i) == 'R')
                    cur = cur.moveRight();
                if (path.charAt(i) == 'U')
                    cur = cur.moveUp();
                if (path.charAt(i) == 'D')
                    cur = cur.moveDown();
            }
        }
        //Catches invalid moves
        catch (ArrayIndexOutOfBoundsException e)
        {
            return false;
        }
        return cur.equals(goal);
    }
}
