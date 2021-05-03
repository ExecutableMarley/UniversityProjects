package com.company;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main
{

    public static void main(String[] args)
    {
	// write your code here

        Random rand = new Random(System.currentTimeMillis());

        List <Integer> intListe = new LinkedList<Integer>();

        for (int i = 0; i < 10; i++)
            intListe.add(rand.nextInt(1000));

        List <String> stringListe = new LinkedList<String>();

        stringListe.add("A test");
        stringListe.add("B test");
        stringListe.add("abc");


        System.out.println(intListe.toString());



        //Aufgabe 1 (a)
        Double average = ListUtils.average(intListe);

        System.out.printf("Average Value of even number = [%f] \n",average);

        //Aufgabe 1 (b)
        List <String> stringresult = ListUtils.upperCase(stringListe);

        System.out.println(stringresult.toString());


        //Aufgabe 1 (c)
        String idontevenknow = ListUtils.getString(intListe);

        System.out.println(idontevenknow);
    }
}
