package com.company;

import javax.imageio.IIOException;
import java.io.IOException;

final class Main
{
    private Main() {}

    public static void main(String[] argv)
    {
        List list = new List();
        for (int i = 0; i < 20; i++)
        {
            list.append(i);
        }
        //list.print();

        try
        {
            list.writeFile("test.txt");

            //Clear list
            list.empty();

            list.readFile("test.txt");

            System.out.println("Printing list data after file parsing");
            list.print();
        }
        catch (java.io.IOException e)
        {
            System.out.println(e.toString());
        }
    }
}
