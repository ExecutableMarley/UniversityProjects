package com.company;

import java.lang.reflect.Method;

import static java.lang.System.out;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class A1
{
    public void methodA(int x, int y)
    {
        out.println((x + y));
    }

    public boolean testIfEven(int x)
    {
        return x % 2 == 0;
    }

    public int methodB(int z)
    {
        return z * z;
    }

    public boolean isOdd(int test)
    {
        return test % 2 != 0;
    }

    public boolean isNullTestMethod(Object o)
    {
        return o == null;
    }

    private static void printTestMethods(Object o)
    {
        //Non Lambda stream approach

        //get the class from object and then the methods array
        Method methods[] = o.getClass().getDeclaredMethods();
        //Iterate through methods array
        for (Method curMethod : methods)
        {
            //Check if method isn't static && method name contains case insensitive "test"
            if (!Modifier.isStatic(curMethod.getModifiers()) &&
                    curMethod.getName().toLowerCase().contains("test"))
                System.out.println(curMethod.toString());
        }

        //Lambda Stream approach
        Arrays.stream(o.getClass().getDeclaredMethods()).
                filter(method -> !Modifier.isStatic(method.getModifiers()) &&
                        method.getName().toLowerCase().contains("test")).
                forEach(method -> System.out.println(method.toString()));
    }

    public static void main(String[] args)
    {
        printTestMethods(new A1());
    }
}

