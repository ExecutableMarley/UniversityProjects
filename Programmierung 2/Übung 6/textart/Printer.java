package textart;

import java.io.PrintStream;

final class Printer
{
    private int printedIndex;
    private final PrintStream stream;

    Printer(final PrintStream stream)
    {
        this.stream = stream;
        printedIndex = -1;
    }

    int expectedIndex = 0;

    void print(final int indexToPrint, final char c)
    {
        synchronized (this)
        {
            while (indexToPrint - printedIndex != 1)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }

            stream.print(c);
            printedIndex = indexToPrint;

            expectedIndex++;

            notifyAll();
            return;
        }
    }
}
