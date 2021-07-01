package com.company;

import static java.lang.System.out;

/*
 * NDArray.java
 * (c) Prof. Dr. Stephan Diehl, University of Trier, Germany, 2021
 */

final class NDArray
{
    private final int[] dimensions;
    private final int[] strides;
    private final double[] data;

    NDArray(double[] data)
    {
        this.data = data;
        this.dimensions = new int[]{data.length};
        this.strides = new int[]{1};
    }

    NDArray(double[] data ,int... args)
    {
        this.data = data;

        //Copy dimensions directly from args parameter
        this.dimensions = new int[args.length];
        System.arraycopy(args, 0,
                this.dimensions, 0, args.length);

        //[Calculate Strides]

        this.strides = new int[args.length];

        //Iterate through strides
        for (int i = 0; i < args.length; i++)
        {
            this.strides[i] = 1;
            //Revers iterate through args
            for (int j = args.length - 1; j > i; j--)
            {
                //Multiply with dimensions
                this.strides[i] *= args[j];
            }
        }
    }

    double get(int... args)
    {
        int offset = 0;
        for (int i = 0; i < args.length; i++)
        {
            offset = offset + strides[i] * args[i];
        }
        return data[offset];
    }

    NDArray reshape(int... args)
    {
        //Reshape logic is inside the new constructor
        //To keep class and class vars final
        return new NDArray(this.data, args);

        /*
        Non final solution:
        NDArray result = new NDArray(this.data);

        result.strides = new int[args.length];

        result.dimensions = new int[args.length];

        System.arraycopy(args, 0, result.dimensions, 0, args.length);

        for (int i = 0; i < args.length; i++)
        {
            result.strides[i] = 1;

            for (int j = args.length - 1; j > i; j--)
            {
                result.strides[i] *= args[j];
            }
        }
        return result;
        */
    }

    static void testGet(NDArray a, double expected, int... args)
    {
        out.println("Array" + java.util.Arrays.toString(args) + " = " + a.get(args) + "  expected value: " + expected);
        //out.printf("Array%s = %.2f | expected value =  %.2f \n", java.util.Arrays.toString(args), a.get(args), expected);
    }

    public static void main(String[] argv)
    {
        out.println("Creating the NDArray 'a' with [1.0, ..., 9.0]");
        NDArray a = new NDArray(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0});
        testGet(a, 1.0, 0);
        testGet(a, 2.0, 1);
        testGet(a, 4.0, 3);

        out.println("reshape(3,3)");
        NDArray b = a.reshape(3, 3);
        testGet(b, 1.0, 0, 0);
        testGet(b, 6.0, 1, 2);

        out.println("\n\nCreating the NDArray 'a' with [1.0, ..., 24.0]");
        int n = 24;
        double[] tmp = new double[n];
        for (int i = 0; i < 24; i++)
        {
            tmp[i] = i;
        }
        a = new NDArray(tmp);
        testGet(a, 4.0, 4);
        out.println("reshape(2,12)");
        b = a.reshape(2, 12);
        testGet(b, 14.0, 1, 2);
        out.println("reshape(2,3,4)");
        NDArray c = b.reshape(2, 3, 4);
        testGet(c, 23.0, 1, 2, 3);
    }
}

