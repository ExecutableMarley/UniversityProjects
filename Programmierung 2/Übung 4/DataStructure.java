package com.company;

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.function.Function;

class DataStructure
{
    // Create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];

    DataStructure()
    {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++)
        {
            arrayOfInts[i] = i;
        }
    }

    int getSize()
    {
        return SIZE;
    }

    int[] getInts()
    {
        return arrayOfInts;
    }

    /**
     * Prints out the array elements while using the specified iterator to iterate over the array
     * */
    void print(IteratorStrategy strategy)
    {
        //Iterate over array using the param iterator to get the next elements
        for (DataStructureIterator iterator = strategy.getIterator(this); iterator.hasNext();)
        {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    /**
     * Prints out the array elements while using the specified function to check if an array index should be printed or skipped
     * */
    void print(Function<Integer, Boolean> iterator)
    {
        for (int i = 0; i < SIZE; i++)
        {
            //Use the param function to check if we should print this element
            if (iterator.apply(i))
                System.out.print(arrayOfInts[i] + " ");
        }
        System.out.println();
    }

    /**
     * Returns true if the integer is even
     * */
    public static boolean isEvenIndex(int i)
    {
        return i % 2 == 0;
    }

    /**
     * Returns true if the integer is odd
     * */
    public static boolean isOddIndex(int i)
    {
        return i % 2 == 1;
    }

    private DataStructureIterator getIterator(IteratorStrategy strategy)
    {
        return strategy.getIterator(this);
    }

    void printEven()
    {
        // Print out values of even indices of the array
        DataStructureIterator iterator = getIterator(new EvenIteratorStrategy());
        while (iterator.hasNext())
        {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
}
