/**
 * @author Marley Arns
 * @version 1.0
 * @github: github.com/ExecutableMarley
 */

package puzzle;

public class Timer
{
    private long timePoint;

    private long nanoPoint;

    public Timer()
    {
        reset();
    }

    void reset()
    {
        timePoint = System.currentTimeMillis();

        nanoPoint = System.nanoTime();
    }

    public long getNano()
    {
        return System.nanoTime() - nanoPoint;
    }

    public long getMillis()
    {
        return System.currentTimeMillis() - timePoint;
    }

    @Override
    public String toString()
    {
        return String.format("Time passed: [%dms]", getMillis());
    }
}

