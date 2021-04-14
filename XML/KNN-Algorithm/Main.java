/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Marley Arns
 * @version 1.0
 * @github: github.com/ExecutableMarley
 */

package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Clock;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

enum AbstandsmaßType
{
    EuklidischerAbstand,
    ManhattanMetrik,
    TschebyschewNorm,
    KosinusÄhnlichkeit
}

//Simple time measurement class.
class Timer
{
    Timer()
    {
        tPoint = System.currentTimeMillis();
    }
    long tPoint;
    void reset()
    {
        tPoint = System.currentTimeMillis();
    }
    //returns time difference in nanoseconds
    int get()
    {
        return (int)(System.currentTimeMillis() - tPoint);
    }
    String getString()
    {
        return Integer.toString(get());
    }
}

//Holds data from the documents
class Dokument
{
    Dokument(int featureCount)
    {
        iFeatures = new double[featureCount];
        nFeatures = new double[featureCount];

        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;
    }
    //MinMax document feature vector
    void minMaxNormalize(double min[],double max[])
    {
        for (int i = 0; i < iFeatures.length; i++)
        {
            if (maxValue - minValue == 0) continue; //Division by zero
            nFeatures[i] = Double.valueOf(iFeatures[i] - minValue) / Double.valueOf(maxValue - minValue);
        }
    }

    int getTokenCount(String source, String token)
    {   //Probably already defined in some string utility class
        int lastIndex = 0, count = 0;
        while (true)
        {
            lastIndex = source.indexOf(token,lastIndex) + 1;
            if (lastIndex == 0) //indexOf return -1 when it doesn't found anything | +1 = 0
                break;
            else
                count++;
        }
        return count;
    }
    void scanDocumentForFeatures(String[] featureList)
    {
        for (int i = 0; i < iFeatures.length; i++)
        {
            iFeatures[i] = getTokenCount(szText,featureList[i]);
            if (iFeatures[i] > maxValue)
                maxValue = iFeatures[i];
            else if (iFeatures[i] < minValue)
                minValue = iFeatures[i];
        }
    }
    String szId;
    String szClass;
    String szText;

    //Features
    //int A,B,C,D,E,F;
    double[] iFeatures;
    //Min Max Normalized
    double[] nFeatures;
    //vector Length
    double vectorLength;
    double maxValue;
    double minValue;
}
// A ranking class to easily sort numbers. Its similar to a list but it also pushes unimportant values out
class sortedRanking
{
    class sortedElement
    {
        //Min sets the value to the opposite of what we want
        sortedElement(boolean min)
        {
            if (min)
                value = Double.MIN_VALUE;
            else
                value = Double.MAX_VALUE;
        }
        String szId;
        double value;
    }

    //Size is the list size and bool max describes if we want high or low numbers
    sortedRanking(int size, boolean max)
    {
        isMaxRanking = max;
        rankingSize = size;
        elements = new sortedElement[size];

        for (int i = 0; i < size;i++)
            elements[i] = new sortedElement(max);
    }
    //
    void push(int index, String sz, double val)
    {
        for (int i = rankingSize - 1; i > index; i--)
        {
            //Moves elements an array index higher. Highest array index gets "deleted"
            elements[i].szId = elements[i-1].szId;
            elements[i].value=elements[i-1].value;
        }
        //index gets replaced with the new values
        elements[index].szId = sz;
        elements[index].value = val;
    }
    //Used to add an new element if its "better"
    void add(String sz, double val)
    {
        int bestSpot = -1;
        for (int i = rankingSize -1; i >= 0;i--)
        {
            if (!isMaxRanking)
            {
                if (elements[i].value > val)
                {
                    bestSpot = i;
                }
            }
            else
            {
                if (elements[i].value < val)
                {
                    bestSpot = i;
                }
            }
        }
        if (bestSpot == -1)
            return;
        else
        {
            push(bestSpot,sz,val);
            return;
        }
    }
    //Returns the result class/classes
    String getResult()
    {
        //First we collect all different classes into one array
        int classCount = 0;
        String[] classArray = new String[rankingSize];
        for (int i = 0; i < rankingSize;i++)
        {
            for (int x = 0; x < classCount+1;x++)
            {
                if (x == classCount)
                {
                    classArray[x] = elements[i].szId;
                    classCount++;
                }
                if (classArray[x].equals(elements[i].szId))
                {
                    break;
                }
            }
        }
        //Init classAppearance array here
        int[] classAppearance = new int[classCount];
        for (int i = 0; i < classCount;i++)
        {
            classAppearance[i] = 0;
        }

        int highestIndex = -1;
        int highestCount = 0;
        //Then we calculate how many times each class appears and what the highest count is
        for (int i = 0; i < rankingSize;i++)
        {
            for (int x = 0;x < classCount;x++)
            {
                if (elements[i].szId.equals(classArray[x]))
                {
                    classAppearance[x]++;
                    if (classAppearance[x] > highestCount)
                        highestCount = classAppearance[x];
                    break;
                }
            }
        }

        String out2 = "";
        //Then we output all the resulting classes here
        for (int i = 0; i < classCount;i++)
        {
            if (classAppearance[i] == highestCount)
                out2 += classArray[i] + " ";
        }
        return out2;
    }
    boolean isMaxRanking;
    int rankingSize;
    sortedElement elements[];
}

//The class that will keep track of the training documents and compare them to requested documents
class KNearestNeighbors
{
    //Holds the feature words we are looking for
    String featureList[];

    //Holds the document data used for calculations
    //Dokument[] documents;
    LinkedList <Dokument> documents = new LinkedList<Dokument>();

    //Holds the max and min feature values that are used to calculate the normalized feature vectors
    double maxFeatures[];
    double minFeatures[];

    //How many different features we have
    //int featureCount;
    //How many different documents we have
    //int documentCount = 10;


    KNearestNeighbors(String[] features)
    {
        //Grab features
        featureList = features;

        //Init min max vectors
        minFeatures = new double[features.length];
        maxFeatures = new double[features.length];
    }




    //Math functions
    double fnEuklidischerAbstand(double[] v1, double[] v2)
    {
        double out = 0;
        for (int i = 0; i < featureList.length; i++)
        {
            out += Math.pow((v1[i] - v2[i]), 2);
        }
        out = Math.sqrt(out);
        return out;
    }
    double fnManhattanMetrik(double[] v1, double[] v2)
    {
        double out = 0;
        for(int i = 0; i < featureList.length;i++)
        {
            out += Math.abs(v1[i] - v2[i]);
        }
        return out;
    }
    double fnTschebyschewNorm(double[] v1, double[] v2)
    {
        double out = Double.MIN_VALUE;
        for (int i = 0; i < featureList.length;i++)
        {
            double curDist = Math.abs(v1[i] - v2[i]);
            if (out < curDist)
                out = curDist;
        }
        return out;
    }
    double fnKosinusAhnlichkeit(double[] v1, double[] v2)
    {
        double out = 0; double length1 = 0, length2 = 0;
        for (int i = 0;i <featureList.length;i++)
        {
            out += v1[i] * v2[i];

            length1 += v1[i] * v1[i];
            length2 += v2[i] * v2[i];
        }

        length1 = Math.sqrt(length1);
        length2 = Math.sqrt(length2);

        out = out / (length1 * length2);
        return out;
    }
    //K-Nearest-Neighbor-Algorithm
    String CalculateKNearestNeighbor(double[] reqVector,int k,AbstandsmaßType type ,boolean useNormalization)
    {
        double distance[] = new double[documents.size()];

        double normalizedReq[] = new double[featureList.length];
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (int i = 0; i < featureList.length;i++)
        {
            if (max < reqVector[i])
                max = reqVector[i];
            if (min > reqVector[i])
                min = reqVector[i];
        }
        for (int i = 0; i < featureList.length;i++)
        {
            normalizedReq[i] = Double.valueOf(reqVector[i] - min) / Double.valueOf(max - min);
        }

        for (int i = 0; i < documents.size(); i++)
        {
            if (!useNormalization)
            {
                switch (type)
                {
                    case EuklidischerAbstand: distance[i] = fnEuklidischerAbstand(documents.get(i).iFeatures,reqVector); break;
                    case ManhattanMetrik: distance[i] = fnManhattanMetrik(documents.get(i).iFeatures,reqVector); break;
                    case TschebyschewNorm: distance[i] = fnTschebyschewNorm(documents.get(i).iFeatures,reqVector); break;
                    case KosinusÄhnlichkeit: distance[i] = fnKosinusAhnlichkeit(documents.get(i).iFeatures,reqVector);
                }
            }
            else
            {
                switch (type)
                {
                    case EuklidischerAbstand:
                        distance[i] = fnEuklidischerAbstand(documents.get(i).nFeatures, normalizedReq);
                        break;
                    case ManhattanMetrik:
                        distance[i] = fnManhattanMetrik(documents.get(i).nFeatures, normalizedReq);
                        break;
                    case TschebyschewNorm:
                        distance[i] = fnTschebyschewNorm(documents.get(i).nFeatures, normalizedReq);
                        break;
                    case KosinusÄhnlichkeit:
                        distance[i] = fnKosinusAhnlichkeit(documents.get(i).nFeatures, normalizedReq);
                }
            }
        }

        sortedRanking ranking = new sortedRanking(k, type == AbstandsmaßType.KosinusÄhnlichkeit);
        for (int i = 0; i < documents.size(); i++)
        {
            ranking.add(documents.get(i).szClass,distance[i]);
        }

        String szResult = ranking.getResult();

        //Assemble output
        String szNormalized;
        if (useNormalization)
            szNormalized = "True ";
        else
            szNormalized = "False";

        String szCalculation = "";
        switch (type)
        {
            case EuklidischerAbstand: szCalculation = "Euklidischen Abstand";break;
            case ManhattanMetrik: szCalculation     = "Manhattan-Metrik    ";break;
            case TschebyschewNorm: szCalculation    = "Tschebyschew-Norm   ";break;
            case KosinusÄhnlichkeit: szCalculation  = "KosinusÄhnlichkeit  ";
        }

        System.out.println("K-Nearest-Neighbor Result: K = " + Integer.toString(k) + " | Normalized = " + szNormalized + " | Formula used = " + szCalculation + " | Classes = " + szResult);

        return szResult;
    }

    //This will return the data behind the data specifier inside the source string
    String getStringFromToken(String source, String token)
    {
        String result = "-1";

        token = "\""+token+"\"";

        int tokenStart = source.indexOf(token) + token.length();

        if (tokenStart == 0)
            return "-1";

        int dataStart = source.indexOf("\"",tokenStart) + 1;
        int dataEnd = source.indexOf("\"",dataStart);

        result = source.substring(dataStart,dataEnd);

        return result;
    }
    //Parses the document file into the class. Each line gets parsed into and document object
    boolean readDocumentFile(String documentPath)
    {
        File dokumentFile = new File(documentPath);

        if (!dokumentFile.exists())
        {
            return false; //No file exists ->return
        }
        try //Its not like its necessary on this point since we already test if the file exists
        {
            Scanner fileScanner = new Scanner(dokumentFile);

            //Iterate through the file lines, as long as we have an next line
            while(fileScanner.hasNextLine())
            {   //Copy file line into string
                String str = fileScanner.nextLine();
                System.out.println("Parsing Line:" + str + "|<end>");

                String szID    = getStringFromToken(str,"id");
                String szClass = getStringFromToken(str,"class");
                String szText  = getStringFromToken(str,"text");

                //Check if we got valid values from the line. invalid line -> line gets ignored
                if (szID != "-1" && szClass != "-1" && szText != "-1")
                {
                    Dokument newDokument = new Dokument(featureList.length);

                    newDokument.szId = szID;
                    newDokument.szClass = szClass;
                    newDokument.szText = szText;

                    documents.add(newDokument);
                }
                else
                    continue; // We just ignore the line if it wasn't valid and keep going

            }

            fileScanner.close();
            return true;
        }
        catch (FileNotFoundException e)
        {
            //curious how we got here
            e.printStackTrace();
            return false;
        }
    }
    //This function will use the featureList to fill the feature vectors of the documents
    boolean scanDocumentsForFeatures()
    {   //Iterate through documents
        for (int i = 0; i < documents.size(); i++)
        {
            documents.get(i).scanDocumentForFeatures(featureList);
        }
        return true;
    }
    //Todo: This is wrong. Dont use that
    void minMaxNormalization()
    {   //Iterate through documents and call minMaxNormalize
        for (int i = 0; i < documents.size(); i++)
        {
            documents.get(i).minMaxNormalize(minFeatures,maxFeatures);
        }
    }
    //Outputs the document data
    void outputDocumentData()
    {
        System.out.println("\nOutputting Document Table Data:");

        //Assemble table header string
        String szHeader = "ID  |  CLASS  | ";
        for (int i = 0; i < featureList.length; i++)
        {
            szHeader += featureList[i] + "  | ";
        }

        szHeader += "Text";
        System.out.println(szHeader);

        //Assemble table data
        for (int i = 0; i < 10; i++)
        {
            String out;
            //Assemble current line
            out =  documents.get(i).szId + "  |  " + documents.get(i).szClass + "      | ";
            for (int x = 0; x < featureList.length; x++)
            {
                if (documents.get(i).iFeatures[x] < 10)
                    out += String.format("0%.2f | ", documents.get(i).iFeatures[x]);
                else
                    out += String.format("%.2f | ", documents.get(i).iFeatures[x]);
            }
            out += documents.get(i).szText;
            //Print it
            System.out.println(out);
        }
    }
}






public class Main {

    public static void main(String[] args)
    {
        //Put the features we want to use in here
        String[] features = {"A","B","C","D","E","F"};
        //Init new KNearestNeighbors with the feature list
        KNearestNeighbors KNN = new KNearestNeighbors(features);

        //First we see if we have an Parameter that could be used as file path
        if ((args.length > 0 && KNN.readDocumentFile(args[0])))
            System.out.println("Using Path from arguments");
        //If the above fails we will use the current directory to search for the file "KNN-Data.txt"
        if (!KNN.readDocumentFile("./KNN-Data.txt"))
        {   //Otherwise we exit if that fails too
            System.out.println("File not found");
            return;
        }

        KNN.scanDocumentsForFeatures();

        KNN.outputDocumentData();

        KNN.minMaxNormalization();

        //The new document that we compare to the training documents
        double[] newDocument = {25,10,10,0,100,5};

        System.out.println("\nCalculations Starting now...");
        System.out.println("//The Class will be the class that appears the most under the nearest neighbors.");
        System.out.println("//Since we got no instructions about the case when multiple classes appear the same time");
        System.out.println("//the Program will output every one of them");
        System.out.println("//Normalized refers to minMaxNormalization | Formula used refers to \"Abstandsmaß Implementationen\"");
        Timer calcTimer = new Timer();

        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.EuklidischerAbstand,false);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.EuklidischerAbstand,false);
        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.EuklidischerAbstand,true);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.EuklidischerAbstand,true);

        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.ManhattanMetrik,false);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.ManhattanMetrik,false);
        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.ManhattanMetrik,true);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.ManhattanMetrik,true);

        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.TschebyschewNorm,false);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.TschebyschewNorm,false);
        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.TschebyschewNorm,true);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.TschebyschewNorm,true);

        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.KosinusÄhnlichkeit,false);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.KosinusÄhnlichkeit,false);
        KNN.CalculateKNearestNeighbor(newDocument,3,AbstandsmaßType.KosinusÄhnlichkeit,true);
        KNN.CalculateKNearestNeighbor(newDocument,5,AbstandsmaßType.KosinusÄhnlichkeit,true);

        int msPassed = calcTimer.get();
        System.out.println("Calculations took " + Integer.toString(msPassed) + " Milliseconds");
    }
}
