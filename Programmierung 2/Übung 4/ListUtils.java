package com.company;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ListUtils
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
     * @param list The list that is used
     * @return Arithmetic mean of all even numbers
     */
    public static double average(List<Integer> list)
    {
        return list.stream().filter(i -> i % 2 == 0).reduce(0, Integer::sum)
                / (double)list.stream().filter(i -> i % 2 == 0).count();
    }

    /**
     * Transforms all strings that either starts with an "a"/"A" or have an size that equals 3 to uppercase
     * @param list The list that is used
     * @return the transformed list of strings
     */
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

    /**
     * Converts an List of integers to and list of strings where each strings starts with
     * either an g (for even numbers) or and u (for odd numbers)
     * @param list The list that is used
     * @return the converted list of strings
     */
   public static String getString(List<Integer> list)
    {
        return list.stream().
                map(integer -> String.format( (integer % 2 == 0 ? "g" : "u") + "%d, ", integer)).
                reduce("", String::concat);
    }
}


/*
* Beispiel: Betrachtete Zeichenkette "Hallo Trier "
→ 5-Gramme sind: {"Hallo", "allo ", "llo T", "lo Tr", "o Tri", " Trie", "Trier"}.
* */

class NGramme
{
    /**
     *
     * */
    static public List<String> someFunction(String param, int length)
    {
        if (length > param.length())
            return null;

        //Normalize string
        String nString = param.trim().replaceAll("\\s+", " ");

        /*
        * Step 1: generate sequence of number 0,1,2,3,4....
        * Step 2: Limit the resulting stream to string.length + 1 - length elements
        * Step 3: Convert those number sequences n-Gramme (substrings of source string)
        * Step 4: Convert to string list and return
        * */
        return Stream.iterate(0, n -> n + 1).
                limit(nString.length() + 1 - length).
                map(i -> nString.substring(i, i + length)).
                collect(Collectors.toList());
    }

    /**
     *
     * */
    static public Map<String, Long> someFunction(List<String> param, int length)
    {
        return param.stream().
                map(s -> someFunction(s, length)).
                flatMap(Collection::stream).
                collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    }
}