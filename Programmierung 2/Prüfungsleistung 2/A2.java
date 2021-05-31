package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

interface Test { boolean test(Map.Entry<Integer,Character> p); }

public class A2
{
    public static void main(String argv[])
    {
        Map<Integer,Character> zeichenliste=liesZeichenliste();
        druckeZeichen(zeichenliste, (Map.Entry<Integer,Character> p)->p.getKey()%2==1);
        druckeZeichen(zeichenliste, (Map.Entry<Integer,Character> p)->p.getKey()%2==0);
    }

    static void druckeZeichen(Map<Integer,Character> zeichenliste, Test pred)
    {
        zeichenliste.entrySet().stream().
                filter(pred::test).
                map(Map.Entry::getValue).
                forEach(System.out::println);

        System.out.println();
    }

    static Map<Integer,Character> liesZeichenliste()
    {
        //Init new map instance
        Map<Integer, Character> map = new HashMap<>();

        try
        {
            //Read file
            FileReader fileReader = new FileReader("buchstabensalat");
            //Actually not necessary since fileReader could be used instead
            BufferedReader reader = new BufferedReader(fileReader);

            for (int i = 0; reader.ready(); i++)
            {
                char a = (char)reader.read();
                //Put into map
                map.put(i, a);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //Returns empty map if we run into an exception
        return map;
    }
}
