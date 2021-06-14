/*
 * Author: Sir Clexalot aka Master Clexington aka Kuenzlord aka Kuenzlinger der Boss
 * Year: 2021
 */

package textart;

final class ArtistThread extends Thread
{
    private static int threadInitNumber;

    private static synchronized int nextThreadNum()
    {
        return threadInitNumber++;
    }

    private final Printer printer;
    private TextArt textArt;
    private TextArtReader textArtReader;

    ArtistThread(final Printer printer, final TextArt textArt)
    {
        super(ArtistThread.class.getName() + "-" + nextThreadNum());
        this.printer = printer;
        this.textArt = textArt;
    }

    ArtistThread(Printer printer, final TextArtReader textArtReader)
    {
        super(ArtistThread.class.getName() + "-" + nextThreadNum());

        this.printer = printer;
        this.textArtReader = textArtReader;
    }

//    ArtistThread(final Printer printer, final TextArtReader textArtReader)
//    {
//        super(ArtistThread.class.getName() + "-" + nextThreadNum());
//        this.printer = printer;
//        this.textArtReader = textArtReader;
//    }

    @Override
    public void run()
    {
        //Wait until textArt isn't null
        while ((textArt = textArtReader.getTextArt()) == null)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        while (!Thread.currentThread().isInterrupted())
        {
            final int nextCharacterIndex = textArt.getNextCharacterIndex();
            if (nextCharacterIndex < 0)
            {
                break;
            }

            final char nextCharacter = textArt.getCharacter(nextCharacterIndex);

            printer.print(nextCharacterIndex, nextCharacter);

            try
            {
                //noinspection BusyWait
                Thread.sleep(10);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}