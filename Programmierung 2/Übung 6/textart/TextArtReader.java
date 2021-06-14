package textart;

public class TextArtReader extends Thread
{
    String fileName;
    TextArt textArt;

    TextArtReader(String fileName)
    {
        this.fileName = fileName;
    }

    @Override
    public void run()
    {
        textArt = new TextArt(fileName);
    }

    TextArt getTextArt()
    {
        return textArt;
    }
}
