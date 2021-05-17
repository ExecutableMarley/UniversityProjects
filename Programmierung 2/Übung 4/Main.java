package com.company;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Main
{

    public static void main(String[] args)
    {
        Random rand = new Random(System.currentTimeMillis());

        List <Integer> intListe = new LinkedList<Integer>();

        for (int i = 0; i < 10; i++)
            intListe.add(rand.nextInt(1000));

        List <String> stringListe = new LinkedList<String>();

        stringListe.add("A test");
        stringListe.add("B test");
        stringListe.add("abc");

        System.out.println(intListe.toString() + "\n");



        //Aufgabe 1 a
        System.out.println("Exercise 1 (a):");

        Double average = ListUtils.average(intListe);

        System.out.printf("Average Value of even number = [%f] \n\n",average);

        //Aufgabe 1 b
        System.out.println("Exercise 1 (b):");

        List <String> stringresult = ListUtils.upperCase(stringListe);

        System.out.println(stringresult.toString()+ "\n");


        //Aufgabe 1 c
        System.out.println("Exercise 1 (c):\n");

        String idontevenknow = ListUtils.getString(intListe);

        System.out.println(idontevenknow + "\n");


        //Aufgabe 2
        System.out.println("Exercise 2:\n");

        NGramme.someFunction("Hallo   Trier ", 5).forEach(System.out::println);

        List<String> stringList = Arrays.asList("Hallo   Trier ", "  Hallo    World  ");

        NGramme.someFunction(stringList);


        //Aufgabe 3
        System.out.println("Exercise 3:\n");

        DataStructure dataStruct = new DataStructure();

        EvenIteratorStrategy even = new EvenIteratorStrategy();

        OddIteratorStrategy odd = new OddIteratorStrategy();

        //Original printEven
        dataStruct.printEven();

        //Print method with printEven functionality
        dataStruct.print(even);

        //Print method with odd iterator
        dataStruct.print(odd);

        //Print with lambda function
        dataStruct.print((Function<Integer, Boolean>) i -> i % 2 == 0);
        dataStruct.print((Function<Integer, Boolean>) i -> i % 2 == 1);

        //Print with method reference
        dataStruct.print(DataStructure::isEvenIndex);
        dataStruct.print(DataStructure::isOddIndex);
    }
}
