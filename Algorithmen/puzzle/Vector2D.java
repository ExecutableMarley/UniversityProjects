/**
 * @author Marley Arns
 * @version 1.0
 * @github: github.com/ExecutableMarley
 */

package puzzle;

/**
 * Used to represent 2D Coordinates
 * */
class Vector2D
{
    int x;
    int y;

    Vector2D(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    Vector2D()
    {
        this(0,0);
    }

    //Effectively cloning the vector parameter
    Vector2D(Vector2D v)
    {
        this(v.x, v.y);
    }

    int ManhattanDistance(Vector2D v)
    {
        return Math.abs(this.x - v.x) + Math.abs(this.y - v.y);
    }
}