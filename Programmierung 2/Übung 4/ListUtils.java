package com.company;

import sun.awt.image.ImageWatched;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.function.Function;


abstract class ListUtils
{
    /*
    *This Constructor prevents:
    * 1. that a subclass is created that extends this class
    * 2. that an instance can be created from this class
    *
    * Why?: Because the sole constructor is explicitly private.
    * Subclass therefore subclasses can't use super() and an instance cant be created
    * Also the default constructors is only generated if there is no other constructor
     */
    private ListUtils(){};


    /**
     * Returns the Arithmetic mean of the even numbers from the provided Integer list
     * @param list The list that is us
     * @return Arithmetic mean of all even numbers
     */
    public static double average(List<Integer> list)
    {
        return list.stream().filter(i -> i % 2 == 0).reduce(0, Integer::sum)
                / (double)list.stream().filter(i -> i % 2 == 0).count();
    }

    public static List<String> upperCase(List<String> list)
    {
        /*
        * Step 1: Convert to stream
        * Step 2: Modify strings that fulfill condition to upper case variant
        * Step 3: Convert to list again
        * */
        return  list.stream().
                map(s -> (s.startsWith("A") || s.startsWith("a") || s.length() == 3) ? s.toUpperCase() : s).
                        collect(Collectors.toList());
    }

   public static String getString(List<Integer> list)
    {
        return list.stream().
                map(integer -> String.format( (integer % 2 == 0 ? "g" : "u") + "%d, ", integer)).
                reduce("", String::concat);
    }
}
