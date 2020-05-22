/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerank;

/**
 *
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


/* Iteriert durch einen 2d array. Kommt in folgender source an meheren stellen vor
for (int index_1 = 0; index_1 < maxMatrixIndex; index_1++)
        {
            for (int index_2 = 0; index_2 < maxMatrixIndex; index_2++)
            {
                unknownMatrix[index_1][index_2] = [Random Var];
            }             
        }
*/

//Main Klasse
//
class MarkovKette
{   
    //Well since Java has no sizeof method we use this (or i dont understand how it works)
    int maxMatrixIndex = 8;
    //Self explaining | Im using float as the type because it will make the calculations simpler
    float adjazenmatrix[][] = 
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
    //Stuff we will calculate
    float tMatrix[][]; //Matrix T | 
    float rMatrix[][]; //Matrix R | Random Jump Matrix
    float pMatrix[][]; //Matrix P | Übergangs Matrix
    
    
    /*
    Calculation Functions
    */
    //Calculate the Matrix t from the "adjazenmatrix" and the epsilon value
    void calculateT_Matrix(float epsilon)
    {
        //Init tMatrix
        tMatrix = new float[maxMatrixIndex][maxMatrixIndex];
        
        float rowSum = 0;
        
        for (int index_1 = 0; index_1 < maxMatrixIndex; index_1++)
        {
            rowSum = sumOfRow(adjazenmatrix[index_1], maxMatrixIndex);
            for (int index_2 = 0; index_2 < maxMatrixIndex; index_2++)
            {
                if (rowSum > 0) //Ausgehende Kanten vorhanden an diesem knoten
                   if (adjazenmatrix[index_1][index_2] > 0) //Vorhandene kante an diesem punkt
                       tMatrix[index_1][index_2] = (adjazenmatrix[index_1][index_2] - epsilon) / rowSum;
                   else //Keine Kante an diesem Punkt
                       tMatrix[index_1][index_2] = 0;
                else //Keine Ausgehenden Kanten vorhanden an diesem knoten -> Random Jump
                tMatrix[index_1][index_2] = (1 - epsilon) / maxMatrixIndex; 
            }             
        }
    }
    //Calculate the R matrix
    void calculateR_Matrix(float epsilon)
    {   //(I guess i could use a fill method too if arrays in java have something like this)
        rMatrix = new float[maxMatrixIndex][maxMatrixIndex];
        
        for (int index_1 = 0; index_1 < maxMatrixIndex; index_1++)
        {
            for (int index_2 = 0; index_2 < maxMatrixIndex; index_2++)
            {   //B1g m4th right here: 
                rMatrix[index_1][index_2] = epsilon / maxMatrixIndex;
            }             
        }
    }
    //Calculate the P matrix
    void calculateP_Matrix()
    {
        pMatrix = new float[maxMatrixIndex][maxMatrixIndex];
        for (int index_1 = 0; index_1 < maxMatrixIndex; index_1++)
        {
            for (int index_2 = 0; index_2 < maxMatrixIndex; index_2++)
            {   
                pMatrix[index_1][index_2] = tMatrix[index_1][index_2]+rMatrix[index_1][index_2];
            }             
        }
    }
    //This will do the power iteration and returns if it was successfull
    boolean doPowerIteration(float startVektor[],float delta)
    {
        printString("Power-Iteration has started\n");
        //This arrays holds the current and last vector calculated vector
        float curArray[] = {0,0,0,0,0,0,0,0};
        float lastArray[] = startVektor;
        //We keep track of the iterations with this
        int currentIteration = 0;
        
        //curLine Stores the line we print
        String curLine = "x(0) = (";
        for (int i = 0; i < maxMatrixIndex;i++)
        {
           curLine = curLine + Float.toString(startVektor[i]) + " ";
        }
        curLine = curLine + ")";
        printString(curLine);
        
        //The for loop is only here to prevent an infinity loop in case something goes wrong
        //10000 operations should never be reached and will save the programm from an infinity loop
        for (int i = 0; i < 10000; i++)
        {
            currentIteration++;
            
            //Calculate the current iteration vector
            for (int index_1 = 0; index_1 < maxMatrixIndex; index_1++)
            {   
                float newVal = 0;
                for (int index_2 = 0; index_2 < maxMatrixIndex; index_2++)
                {
                    newVal = newVal + pMatrix[index_2][index_1] * lastArray[index_2];
                }
                //
                curArray[index_1] = newVal;
            }
            
            //Assemble the next output line
            curLine = "x(" + currentIteration + ") = (";
            for (int ii = 0; ii < maxMatrixIndex;ii++)
            {
                curLine = curLine + Float.toString(curArray[ii]) + " ";
            }
            curLine = curLine + ")";
            //Output current vector
            printString(curLine);
            
            
            //Break the loop if the difference is smaller then delta
            if (delta > difOfArrays(curArray,lastArray,maxMatrixIndex))
            {
                //Return that the operation was successfull
                printString("\nPower-Iteration Terminated after " + Integer.toString(currentIteration) + " iterations \n");
                return true;
            }
            
            //Copy curArray into lastArray
            for (int ii = 0; ii < maxMatrixIndex;ii++)
            {
                lastArray[ii] = curArray[ii];
            }
        } 
        //Something whent wrong and the loop run more times than expected
        printString("Power operation reached iteration limit and got Terminated after 10.000 iterations");
        return false;
    }
    
    
    //Return the sum of an array row
    float sumOfRow(float array[], int maxIndex)
    {
        float result = 0;
        for (int i = 0; i < maxIndex;i++)
        {
            result = result + array[i];
        }
        return result;
    }
    //Returns the sum of the difference between 2 arrays
     float difOfArrays(float array1[], float array2[], int maxIndex)
     {
         float result = 0;
         for (int i = 0; i < maxIndex;i++)
         {   //Calculate difference and add it into result
             result = result + Math.abs(array1[i] - array2[i]);
         }
         return result;
     }
    
    //Print functions
    //Used to print our results into the console
    void printString(String str)
    {
        System.out.println(str);
    }
    //This function will print a 2d matrix to the console
    void printMatrix(float Matrix[][], boolean castToInt) //Why does java has no default params anyway? castToInt controls if the output should be casted to int
    {
        //String that we will output at the end
        String finalString = "";
        
        for (int index_1 = 0; index_1 < maxMatrixIndex; index_1++)
        {
            finalString = finalString + "| ";
            for (int index_2 = 0; index_2 < maxMatrixIndex; index_2++)
            {
                if (!castToInt)
                finalString = finalString + Float.toString(Matrix[index_1][index_2]) + " ";
                else
                finalString = finalString + Integer.toString((int)Matrix[index_1][index_2]) + " ";
            }            
            finalString = finalString + " | \n";   
        }
        //We assembled out string, now print it!
        printString(finalString);
    }
    //Wrapper Functions
    //Easier to call and looks simple
    void printAdjazenmatrix() 
    {
        printMatrix(adjazenmatrix, true);
    }
    void printMatrixT()
    {
        printMatrix(tMatrix,false);
    }
    void printMatrixR()
    {
        printMatrix(rMatrix,false);
    }
    void printMatrixP()
    {
        printMatrix(pMatrix,false);
    }
}




public class PageRank 
{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        //e von der Aufgabenstellung
        float epsilon = 0.6f;
        
        MarkovKette markovKette = new MarkovKette();
        
        //markovKette.printString("Proccess Entry");
        
        //Adjazenzmatrix von G wird hier ausgegeben
        markovKette.printString("Adjazenmatrix wird ausgegeben:\n");
        markovKette.printAdjazenmatrix();
        
        //T Matrix berechnen mit epsilon als parameter
        markovKette.calculateT_Matrix(epsilon);
        
        //Die Matrix wird jetzt etwas unschöner werden, da die aufgabenstellung keine rundungen erwähnt
        //und das beispiel aus der vorlesung auch nicht gerundet war, werde ich das hier auch nicht tun 
        //(abgesehen das es pseudo gerundet wird durch die float precision)
        markovKette.printString("Matrix T wird ausgegeben\n");
        markovKette.printMatrixT();
        
        //Ich denke es sollte selbsterklärend sein was die folgenden statements machen
        //Die namen der methoden sollten relativ aussagekräftig sein
        markovKette.calculateR_Matrix(epsilon);
        
        markovKette.printString("Matrix R wird ausgegeben\n");
        markovKette.printMatrixR();
        
        
        markovKette.calculateP_Matrix();
        markovKette.printString("Matrix P wird ausgegeben\n");
        markovKette.printMatrixP();
        
        //Die parameter aus der aufgabenstellung
        float startVektor[] = {0.1f,0.05f,0.13f,0.08f,0.04f,0.06f,0.24f,0.3f};
        float delta = 0.001f;
        
        //Führt die power itaration durch und schreibt die ergebnisse in die konsole
        boolean powerIterationResult = markovKette.doPowerIteration(startVektor, delta);
        
    }
    
}
