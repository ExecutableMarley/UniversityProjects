package com.company;

//Customs
import java.io.*;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Doppelt verkettete Liste.
 */
public class List
{
    protected Element begin;
    protected Element end;
    protected int length;

    /**
     * Fuege ein Element am Ende der Liste hinzu.
     *
     * @param e Neues Listenelement.
     */
    public void append(Element e)
    {
        if (begin == null)
        {
            begin = e;
            end = e;
            e.setPrev(null);
            e.setNext(null);
        } else
        {
            Element tmp = end;
            end = e;
            tmp.setNext(e);
            e.setPrev(tmp);
            e.setNext(null);
        }
        length++;
    }

    /**
     * Erzeuge ein Element, das den Wert v enthaelt, und fuege es am Ende der Liste hinzu.
     *
     * @param value Wert des neuen Elements.
     */
    public void append(double value)
    {
        append(new Element(value));
    }

    /**
     * Fuege die Elemente aus der uebergebenen Liste am Ende dieser Liste hinzu.
     *
     * @param list Die Liste, deren Elemente angefuegt werden sollen.
     */
    public void append(List list)
    {
        Element e = list.getBegin();
        while (e != null)
        {
            Element next = e.getNext(); // append sets e.next to null
            append(e);
            e = next;
        }
    }

    /**
     * Erzeuge ein neues Element pro Wert im uebergebenen Array und fuege die neuen Elemente am Ende der Liste hinzu.
     *
     * @param values Array mit Double-Werten, die eingefuegt werden sollen.
     */
    public void append(double[] values)
    {
        for (double value : values)
        {
            append(value);
        }
    }

    /**
     * Erzeuge ein Array mit den Elementen aus der Liste (gleiche Reihenfolge).
     *
     * @return Array mit Elementen der Liste (in gleicher Reihenfolge).
     */
    public double[] asArray()
    {
        double[] array = new double[length];
        Element element = begin;
        int arrayPos = 0;
        while (element != null)
        {
            array[arrayPos] = element.value;
            arrayPos++;
            element = element.next;
        }
        return array;
    }

    /**
     * Gib die Liste auf der Konsole aus.
     */
    public void print()
    {
        if (isEmpty())
        {
            System.out.println("Empty");
        } else
        {
            Element pos = begin;
            while (pos != null)
            {
                pos.print();
                pos = pos.next;
            }
        }
    }

    /**
     * Leere die Liste.
     */
    public void empty()
    {
        begin = null;
        end = null;
        length = 0;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public Element getBegin()
    {
        return begin;
    }

    public void setBegin(Element begin)
    {
        this.begin = begin;
    }

    public Element getEnd()
    {
        return end;
    }

    public void setEnd(Element end)
    {
        this.end = end;
    }

    public boolean isEmpty()
    {
        return (begin == null) && (end == null) && (length == 0);
    }


    /**
    * Customs
    * */



    // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    void writeFile(String fileName) throws java.io.IOException
    {
        if (isEmpty())
            return;

        //Get File Writter interface
        FileWriter fileWriter = new FileWriter(fileName, true);

        BufferedWriter writer = new BufferedWriter(fileWriter);

        Element curElement = begin;

        while (curElement != null)
        {
            //%f -> float , %d -> integer, %s -> String | Common Format Tokens

            //Check if we have an next element
            String curLine = String.format(curElement.next == null ? "%f \n" : "%f, ", curElement.getValue()); //"1.00000, "

            writer.append(curLine);

            curElement = curElement.next;
        }

        writer.close();
    }

    void readFile(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader reader = new BufferedReader(fileReader);

            Scanner scanner = new Scanner(fileReader);


            int iCount = 0;

            // \\w -> a-z und 0-9 | \\s -> any whitespace
            while (reader.ready())
            {
                String curLine = reader.readLine();

                curLine = curLine.replaceAll(",\\w", ".");

                String []values = curLine.split(",\\s");

                /*for (int i = 0; i < values.length; i++)
                {
                    append(Double.parseDouble(values[i]));
                }*/

                //Iterate through array fields
                for (String value : values)
                    append(Double.parseDouble(value));

                //Increase counter
                iCount++;
            }

            System.out.println(String.format("%d Rows Parsed", iCount));
        }
        catch (java.io.FileNotFoundException e)
        {
            System.out.println("File not found");

            System.out.println(e.toString());
        }
        catch (java.io.IOException e)
        {
            System.out.println(e.toString());
        }
        catch (NumberFormatException e)
        {
            System.out.println(e.toString());
        }
    }

    /**
     * Die Klasse fuer die Elemente der doppelt verketteten Liste.
     */
    private final class Element
    {
        private double value;
        private Element next;
        private Element prev;

        Element(double value)
        {
            setValue(value);
        }

        double getValue()
        {
            return value;
        }

        void setValue(double value)
        {
            this.value = value;
        }

        Element getNext()
        {
            return next;
        }

        void setNext(Element next)
        {
            this.next = next;
        }

        Element getPrev()
        {
            return prev;
        }

        void setPrev(Element prev)
        {
            this.prev = prev;
        }

        void print()
        {
            System.out.println(value);
        }

        //Customs
        boolean hasNext()
        {
            return next != null;
        }
    }
}
