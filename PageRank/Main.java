/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

/**
 * @author Marley Arns
 * @version 1.0
 * @github: github.com/ExecutableMarley
 */

//Der graph G als als Adjazenzmatrix
/*          0 1 2 3 4 5 6 7
 0       |  0 0 0 0 0 1 0 0 |     1 out 2 in
 1       |  1 0 0 1 0 0 0 0 |     2 out 2 in
 2       |  1 1 0 0 0 0 0 1 |     3 out 2 in
 3       |  0 1 0 0 0 1 1 0 |     3 out 2 in
 4   A = |  0 0 1 1 0 0 0 0 |     2 out 1 in
 5       |  0 0 0 0 0 0 1 0 |     1 out 2 in
 6       |  0 0 1 0 1 0 0 1 |     3 out 2 in
 7       |  0 0 0 0 0 0 0 0 |     0 out 2 in
*/

//Main Klasse
//
class MarkovKette
{
    //We get that from the constructor parameter
    double adjazenmatrix[][];
    //Stuff we will calculate
    double tMatrix[][]; //Matrix T |
    double rMatrix[][]; //Matrix R | Random Jump Matrix
    double pMatrix[][]; //Matrix P | Übergangs Matrix

    MarkovKette(double newMatrix[][])
    {
        adjazenmatrix = newMatrix;
    }

    //Return the sum of an array row
    double sumOfRow(double array[], int maxIndex)
    {
        double result = 0;
        for (int i = 0; i < maxIndex;i++)
        {
            result = result + array[i];
        }
        return result;
    }
    //Returns the sum of the difference between 2 arrays
    double difOfArrays(double array1[], double array2[], int maxIndex)
    {
        double result = 0;
        for (int i = 0; i < maxIndex;i++)
        {   //Calculate difference and add it into result
            result = result + Math.abs(array1[i] - array2[i]);
        }
        return result;
    }
    /*
    Calculation Functions
    */
    //Calculate the Matrix t from the "adjazenmatrix" and the epsilon value
    void calculateT_Matrix(double epsilon)
    {
        //Init tMatrix
        tMatrix = new double[adjazenmatrix.length][adjazenmatrix[0].length];

        double rowSum = 0;

        for (int index_1 = 0; index_1 < adjazenmatrix.length; index_1++)
        {
            rowSum = sumOfRow(adjazenmatrix[index_1], adjazenmatrix[index_1].length);
            for (int index_2 = 0; index_2 < adjazenmatrix[index_1].length; index_2++)
            {
                if (rowSum > 0) //Ausgehende Kanten vorhanden an diesem knoten
                    if (adjazenmatrix[index_1][index_2] > 0) //Vorhandene kante an diesem punkt
                        tMatrix[index_1][index_2] = (adjazenmatrix[index_1][index_2] - epsilon) / rowSum;
                    else //Keine Kante an diesem Punkt
                        tMatrix[index_1][index_2] = 0;
                else //Keine Ausgehenden Kanten vorhanden an diesem knoten -> Random Jump
                    tMatrix[index_1][index_2] = (1 - epsilon) / adjazenmatrix[index_2].length;
            }
        }
    }
    //Calculate the R matrix
    void calculateR_Matrix(double epsilon)
    {
        rMatrix = new double[adjazenmatrix.length][adjazenmatrix[0].length];

        for (int index_1 = 0; index_1 < adjazenmatrix.length; index_1++)
        {
            for (int index_2 = 0; index_2 < adjazenmatrix[index_1].length; index_2++)
            {
                rMatrix[index_1][index_2] = epsilon / adjazenmatrix[index_2].length;
            }
        }
    }
    //Calculate the P matrix
    void calculateP_Matrix()
    {
        pMatrix = new double[adjazenmatrix.length][adjazenmatrix[0].length];
        for (int index_1 = 0; index_1 < adjazenmatrix.length; index_1++)
        {
            for (int index_2 = 0; index_2 < adjazenmatrix[index_1].length; index_2++)
            {
                pMatrix[index_1][index_2] = tMatrix[index_1][index_2]+rMatrix[index_1][index_2];
            }
        }
    }
    //This will do the power iteration and returns if it was successfull
    boolean doPowerIteration(double startVektor[],double delta, int precision)
    {
        printString("Power-Iteration has started\n");
        //This arrays holds the current and last vector calculated vector
        double curArray[] = new double[startVektor.length];
        double lastArray[] = startVektor;
        //We keep track of the iterations with this
        int currentIteration = 0;

        //curLine Stores the line we print
        String curLine = "x(0) = (";
        for (int i = 0; i < adjazenmatrix.length;i++)
        {
            curLine = curLine + String.format("%." + 10 + "f ", startVektor[i]);
        }
        curLine = curLine + ")";
        printString(curLine);

        //The for loop is only here to prevent an infinity loop in case something goes wrong
        //10000 operations should never be reached and will save the programm from an infinity loop
        for (int i = 0; i < 10000; i++)
        {
            currentIteration++;

            //Calculate the current iteration vector
            for (int index_1 = 0; index_1 < adjazenmatrix.length; index_1++)
            {
                double newVal = 0;
                for (int index_2 = 0; index_2 < adjazenmatrix[index_1].length; index_2++)
                {
                    newVal = newVal + pMatrix[index_2][index_1] * lastArray[index_2];
                }
                //
                curArray[index_1] = newVal;
            }

            //Assemble the next output line
            curLine = "x(" + currentIteration + ") = (";
            for (int ii = 0; ii < curArray.length;ii++)
            {
                curLine = curLine + String.format("%." + 10 + "f ", curArray[ii]);
            }
            curLine = curLine + ")";
            //Output current vector
            printString(curLine);


            //Break the loop if the difference is smaller then delta
            if (delta > difOfArrays(curArray,lastArray,curArray.length))
            {
                //Return that the operation was successfull
                printString("\nPower-Iteration Terminated after " + Integer.toString(currentIteration) + " iterations \n");
                return true;
            }

            //Copy curArray into lastArray
            for (int ii = 0; ii < curArray.length;ii++)
            {
                lastArray[ii] = curArray[ii];
            }
        }
        //Something went wrong and the loop run more times than expected
        printString("Power operation reached iteration limit and got Terminated after 10.000 iterations");
        return false;
    }
    //Do power iteration with a decimal precision of 10
    boolean doPowerIteration(double startVektor[],double delta)
    {
        return doPowerIteration(startVektor,delta,10);
    }

    //Print functions
    //Used to print our results into the console
    void printString(String str)
    {
        System.out.println(str);
    }
    //This function will print a 2d matrix to the console
    void printMatrix(double Matrix[][], int precsion)
    {
        //String that we will output at the end
        String finalString = "";

        //Format String with presision decimal places
        String str = "%." + precsion + "f";

        for (int index_1 = 0; index_1 < Matrix.length; index_1++)
        {
            finalString = finalString + "| ";
            for (int index_2 = 0; index_2 < Matrix[index_1].length; index_2++)
            {
                finalString = finalString + String.format(str, Matrix[index_1][index_2]) + " ";
            }
            finalString = finalString + " | \n";
        }
        //We assembled out string, now print it!
        printString(finalString);
    }

    void printAdjazenmatrix(int precision)
    {
        printMatrix(adjazenmatrix, precision);
    }
    void printAdjazenmatrix()
    {
        printMatrix(adjazenmatrix, 0);
    }
    void printMatrixT(int precision)
    {
        printMatrix(tMatrix,precision);
    }
    void printMatrixT()
    {
        printMatrix(tMatrix,5);
    }
    void printMatrixR(int precision)
    {
        printMatrix(rMatrix,precision);
    }
    void printMatrixR()
    {
        printMatrix(rMatrix,5);
    }
    void printMatrixP(int precision)
    {
        printMatrix(pMatrix,precision);
    }
    void printMatrixP()
    {
        printMatrix(pMatrix,5);
    }
}

public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //e von der Aufgabenstellung
        double epsilon = 0.6f;

        //Adjazenmatrix von der aufgabenstellung
        double adjazenmatrix[][] =
                { //
                        // 0 1 2 3 4 5 6 7
                        {0,0,0,0,0,1,0,0}, //0
                        {1,0,0,1,0,0,0,0}, //1
                        {1,1,0,0,0,0,0,1}, //2
                        {0,1,0,0,0,1,1,0}, //3
                        {0,0,1,1,0,0,0,0}, //4
                        {0,0,0,0,0,0,1,0}, //5
                        {0,0,1,0,1,0,0,1}, //6
                        {0,0,0,0,0,0,0,0}, //7
                };

        MarkovKette markovKette = new MarkovKette(adjazenmatrix);


        //markovKette.printString("Proccess Entry");

        //Adjazenzmatrix von G wird hier ausgegeben
        markovKette.printString("Adjazenmatrix wird ausgegeben:\n");
        markovKette.printAdjazenmatrix(0);

        //T Matrix berechnen mit epsilon als parameter
        markovKette.calculateT_Matrix(epsilon);

        markovKette.printString("Matrix T wird ausgegeben\n");
        markovKette.printMatrixT(5);



        markovKette.calculateR_Matrix(epsilon);

        markovKette.printString("Matrix R wird ausgegeben\n");
        markovKette.printMatrixR(5);


        markovKette.calculateP_Matrix();
        markovKette.printString("Matrix P wird ausgegeben\n");
        markovKette.printMatrixP(5);

        //Die parameter aus der aufgabenstellung
        double startVektor[] = {0.1f,0.05f,0.13f,0.08f,0.04f,0.06f,0.24f,0.3f};
        double delta = 0.001f;

        //Führt die power itaration durch und schreibt die ergebnisse in die konsole
        //Last parameter is the decimal precision
        boolean powerIterationResult = markovKette.doPowerIteration(startVektor, delta, 10);
    }
}
