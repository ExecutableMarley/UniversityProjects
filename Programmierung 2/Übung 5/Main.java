package com.company;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

final class Main
{
    private Main() {}

    private static boolean testFunctionality(BiFunction<String, String, String> func,
                                             BiFunction<String, String, String> testFunc,
                                             String arg1, String arg2)
    {
        return func.apply(arg1, arg2).equals(testFunc.apply(arg1, arg2));
    }

    private static boolean testFunctionality(BiFunction<String, String, String> func,
                                             Method testMethod,
                                             String arg1, String arg2) throws InvocationTargetException, IllegalAccessException
    {
        return func.apply(arg1, arg2).equals(testMethod.invoke(null, arg1, arg2));
    }

    public static void main(String[] args)
    {
        //out.println(testFunctionality(String::concat, ObfuscatedUtil::i3f20y1OZq, "Hallo", "Du"));

        /*Get the methods array from out bytecode class
         * Actually we also could use getMethods however this would also include
         * non declared methods (function methods without a body) which we don't want*/
        Method []methods = ObfuscatedUtil.class.getDeclaredMethods();
        for (Method method : methods)
        {
            //Check if the method has String return type + 2 parameters that are compatible with strings
            //We also could use .equal however this wouldn't allow classes that extends from the string class
            //which would perfectly work too (example: custom strings class inherits from std string)
            if (    String.class.isAssignableFrom(method.getReturnType()) &&
                    method.getParameterCount() == 2 &&
                    String.class.isAssignableFrom(method.getParameterTypes()[0]) &&
                    String.class.isAssignableFrom(method.getParameterTypes()[1]) &&
                    Modifier.isStatic(method.getModifiers()))
            {
                try
                {
                    //Check if the method functionality matches with String::concat
                    if (testFunctionality(String::concat, method, "Hello", " World"))
                    {
                        System.out.println(method.toString());

                        //First parameter is the instance of the class which is null since we call a static method
                        System.out.println(method.invoke(null, "Hello", " World"));
                    }
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
