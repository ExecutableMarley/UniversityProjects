package com.company;

import java.util.HashSet;
import java.util.LinkedList;

import static java.lang.System.out;

public class A2
{
    private static String commonAncestor(Object a, Object b)
    {
        for (Class cA = a.getClass(); cA != null; cA = cA.getSuperclass())
        {
            for (Class cB = b.getClass(); cB != null; cB = cB.getSuperclass())
            {
                //Check if the two classes are the same
                if (cA.equals(cB))
                    return cA.getName();
            }
        }
        //Shouldn't be possible to reach this since every class extends from object
        return "Invalid";
    }

    public static void main(String[] args)
    {
        out.println(commonAncestor(new LinkedList<Integer>(), new HashSet<Integer>()));
    }
}
