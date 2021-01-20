/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marley Arns
 * @version 1.0
 * @github: github.com/ExecutableMarley
 */

package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;
//Simple timer class
class Timer
{
    Timer()
    {
        tPoint = System.currentTimeMillis();
    }
    long tPoint;
    void reset()
    {
        tPoint = System.nanoTime();
    }
    //returns time difference in milliseconds
    int get()
    {
        return (int)(System.currentTimeMillis() - tPoint);
    }
    String getString()
    {
        return Integer.toString((int)(System.currentTimeMillis() - tPoint));
    }
}
//Simply 2d vector class with math operations
class vector2d
{
    vector2d(double newX, double newY)
    {
        x = newX;
        y = newY;
    }
    double x;
    double y;

    //no operator overloading for special symbols...
    vector2d vector2dSubtract(vector2d a, vector2d b)
    {
        return new vector2d(a.x-b.x,a.y-b.y);
    }
    vector2d vector2dAdd(vector2d a, vector2d b)
    {
        return new vector2d(a.x+b.x,a.y+b.y);
    }
    vector2d vector2dDivide(vector2d a, double b)
    {
        return new vector2d(a.x/b,a.y/b);
    }
    double getLength()
    {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }
    double sumOfPower()
    {
        return Math.pow(x,2) + Math.pow(y,2);
    }
}
//Class that we use for every cluster we need
class Cluster
{
    enum centroidCalculationMethod
    {
        Median,
        ArithmeticMean,
        HarmonicMean,
        RootMean
    }
    Cluster(vector2d newCenter)
    {
        centroid = newCenter;
    }
    vector2d centroid;

    LinkedList<vector2d> clusterList = new LinkedList<vector2d>();

    void add(vector2d newVector)
    {
        clusterList.add(newVector);
    }
    void setCenter(vector2d newCenter)
    {
        centroid = newCenter;
    }
    double EuclideanDistance(vector2d a, vector2d b)
    {
        return a.vector2dSubtract(a,b).getLength();
    }
    //Center calculation functions
    vector2d medianCenter()
    {
        int size = 0;
        vector2d[] vecArray = new vector2d[clusterList.size()];

        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            vecArray[size] = curIterator.next();
            size++;
        }

        //Sort the newly made array
        for (int i = 0;i < (size - 1);i++)
        {
            //if (vecArray[i].getLength() > vecArray[i+1].getLength())
            if (vecArray[i].x + vecArray[i].y > vecArray[i+1].x + vecArray[i+1].y)
            {
                vector2d temp = vecArray[i];
                vecArray[i] = vecArray[i+1];
                vecArray[i+1] = temp;

                if(i > 0)
                i -= 2;
            }
        }
        vector2d out = new vector2d(0,0);
        //size even
        if (size % 2 == 0)
        {
            out = out.vector2dDivide(out.vector2dAdd(vecArray[size / 2],vecArray[size / 2 + 1]), 2);
        }
        else //size odd
        {
            out = vecArray[(size + 1) / 2];
        }
        return out;
    }
    vector2d arithmethicMeanCenter()
    {
        int size = 0;
        vector2d center = new vector2d(0,0);
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            size++;
            center = center.vector2dAdd(center, curIterator.next());
        }

        center = center.vector2dDivide(center, size);
        return center;
    }
    vector2d harmonicMeanCenter()
    {
        int size = 0;
        vector2d center = new vector2d(0,0);
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            size++;
            vector2d curVector = curIterator.next();
            center = center.vector2dAdd(center, new vector2d(1 / curVector.x,1 / curVector.y));
        }

        center = new vector2d(size / center.x,size / center.y);
        return center;
    }
    vector2d rootMeanCenter()
    {
        int size = 0;
        vector2d center = new vector2d(0,0);
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            size++;
            vector2d curVector = curIterator.next();
            center = center.vector2dAdd(center, new vector2d(curVector.x * curVector.x, curVector.y * curVector.y));
        }

        center = new vector2d(Math.sqrt(center.x / size),Math.sqrt(center.y / size));
        return center;
    }
    //We calculate a cluster center with this
    void calculateCenter(centroidCalculationMethod type)
    {
        if (clusterList.isEmpty())
            return;


       switch (type)
       {
           case Median        : centroid = medianCenter()         ;break;
           case ArithmeticMean: centroid = arithmethicMeanCenter();break;
           case HarmonicMean  : centroid = harmonicMeanCenter()   ;break;
           case RootMean      : centroid = rootMeanCenter()       ;break;
       }

    }
    //We calculate the quality of that cluster with this
    double calculateQuality()
    {
        double quality = 0;
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            quality += Math.pow(EuclideanDistance(centroid,curIterator.next()), 2);
        }
        return quality;
    }
    //Used to gather informations about the cluster center
    String centroidToString()
    {
        return "{X:" + Double.toString(centroid.x)+ " Y:" + Double.toString(centroid.y) + "}";
    }
}
//Class to handle the clsutering
class KMeansClustering
{
    KMeansClustering(int size)
    {
        cordListSize = size;
        coordList = new vector2d[size];
    }

    vector2d[] coordList;
    int cordListSize;

    //[Common operations]
    //no operator overloading for special symbols...
    vector2d vector2dSubtract(vector2d a, vector2d b)
    {
        return new vector2d(a.x-b.x,a.y-b.y);
    }

    double EuclideanDistance(vector2d a, vector2d b)
    {
        return vector2dSubtract(a,b).getLength();
    }

    //[Main Functions]
    //This will do the main calculation
    //k ist the number of cluster | type is the calculation type we use for the centroids | useRandomCentroids specifys if we use random centroids
    //If we use random centroids the vector array is ignored | the vector array holds the centroids in case we dont use random centroids
    double performCalculations(int k,Cluster.centroidCalculationMethod type, boolean useRandomCentroids, vector2d[] centroids)
    {
        //Check if the parameters make sense
        if (!useRandomCentroids && centroids == null)
            return -1;

        //init timer
        Timer calculationTimer = new Timer();

        //Init clusters
        Cluster[] clusterArray = new Cluster[k];

        Random randGenerator = new Random();
        if (useRandomCentroids)
        {   //set a seed to we achieve better randomness
            randGenerator.setSeed(System.currentTimeMillis());
        }

        //Set centroids
        for (int i = 0; i < k; i++)
        {
            if (useRandomCentroids)
            {
                clusterArray[i] = new Cluster(new vector2d(randGenerator.nextDouble() * 100,randGenerator.nextDouble() * 100));
            }
            else
            {
                clusterArray[i] = new Cluster(centroids[i]);
            }
        }

        //Initial Cluster Sorting
        for (int i = 0; i < 1000; i++)
        {
            int bestIndex = 0;
            double bestDist = Double.MAX_VALUE;

            for (int c = 0; c < k; c++)
            {
                double temp = EuclideanDistance(coordList[i],clusterArray[c].centroid);
                if (bestDist > temp)
                {
                    bestDist = temp;
                    bestIndex = c;
                }
            }

            clusterArray[bestIndex].add(coordList[i]);
        }

        //Calculate new centroids for the first time
        for (int i = 0; i < k;i++)
        {
            clusterArray[i].calculateCenter(type);
        }

        int objectsChanged = 100;

        //Auschnitt aus der aufgaben stellung:
        //"Sobald nur noch 10 Elemente oder nachdem...soll der Algorithmus terminieren"
        //Sinn machen würde kleiner gleich zehn aber die aufgabenstellung
        //scheint genau 10 elemente zu verlangen also != 10
        //Dennoch werde ich den Algorithmus abbrechen sobald objects changed unter 10 ist
        //Wenn das nicht so vorgesehen ist dann macht es wirklich keinen sinn
        for (int i = 0; i < 10000 && objectsChanged > 10; i++) //We dont want over 10000 iterations
        {
            objectsChanged = 0; //Reset objects changed for this cycle

            for (int c = 0; c < k; c++) //Iterate through cluster
            {
                int bestIndex = 0;
                double bestDist = Double.MAX_VALUE;

                Iterator<vector2d> curIterator = clusterArray[c].clusterList.iterator();

                while (curIterator.hasNext()) //Iterate through every point in that cluster
                {
                    vector2d currentPoint = curIterator.next();
                    for (int g = 0; g < k; g++) //Compare current point to cluster centroid
                    {
                        double temp = EuclideanDistance(clusterArray[g].centroid,currentPoint);
                        if (bestDist > temp)
                        {
                            bestDist = temp;
                            bestIndex = c;
                        }
                    }
                    if (bestIndex != c) //we need to "move" the point to another cluster
                    {   //Remove from current, move to new
                        curIterator.remove();
                        clusterArray[bestIndex].add(currentPoint);
                        objectsChanged++; //Increase this
                    }
                }
            }
            //We did one cycle, now recalculate the center
            for (int c = 0; c < k;c++)
            {
                clusterArray[c].calculateCenter(type);
            }
        }

        String calculationTimeString = calculationTimer.getString();

        //Assemble centroid calc method used
        String consoleOut = "";

        consoleOut = "\nClustering Calculation Finished:\n";
        consoleOut += "Centroid Calculation Method Used = ";
        switch (type)
        {
            case Median: consoleOut += "Median";break;
            case ArithmeticMean:consoleOut += "Arithmetisches Mittel";break;
            case HarmonicMean:consoleOut += "Harmonisches Mittel";break;
            case RootMean:consoleOut += "Quadratisches Mittel";
        }
        consoleOut += "\nCluster Number (k) = " + Integer.toString(k);

        System.out.println(consoleOut);

        //asemble and output centroid positions
        consoleOut = "";
        for (int i = 0; i < k;i++)
        {
            consoleOut += "Cluster " + Integer.toString(i) + " = " + clusterArray[i].centroidToString() + " | ";
        }
        System.out.println(consoleOut);

        //Calculate clustering quality
        double finalQuality = 0;
        for (int i = 0; i < k;i++)
        {
            finalQuality += clusterArray[i].calculateQuality();
        }
        //Output quality
        System.out.println("Clustering Quality = " + Double.toString(finalQuality));
        //Output calc time
        System.out.println("Calculation took " + calculationTimeString + " ms");
        //Some pseudo border
        System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");


        return finalQuality;
    }



    //[Miscellaneous]
    //used to read the text file and extract the coordinates
    boolean readDocumentFile(String documentPath)
    {
       File documentFile = new File(documentPath);

       if (!documentFile.exists())
       {
           return false;
       }
       try
       {
           Scanner fileScanner = new Scanner(documentFile);

           int curLineNum = 0;

           while (fileScanner.hasNextLine())
           {
               String curLine = fileScanner.nextLine();
               //Regex for simple extraction of number pairs
               String[] XYNUM = curLine.split("\\D+");

               coordList[curLineNum] = new vector2d(Double.parseDouble(XYNUM[0]),Double.parseDouble(XYNUM[1]));

               curLineNum++;
           }
       }
       catch (FileNotFoundException e)
       {
           e.printStackTrace();
           return false;
       }
       return true;
    }
}




public class Main
{

    public static void main(String[] args)
    {
	// write your code here

        //Needs to know how many numbers it should hold
        KMeansClustering cluster = new KMeansClustering(1000);

        //We read the file here. Path needs to be valid
        if (!cluster.readDocumentFile("./kMeans.txt"))
        {   //Otherwise the program does nothing
            return;
        }
        //The centroid positions from the
        vector2d[] startCentroidk3 = new vector2d[3];
        vector2d[] startCentroidk6 = new vector2d[6];

        startCentroidk3[0] = new vector2d(10.5,40.3);
        startCentroidk3[1] = new vector2d(46.5,28.1);
        startCentroidk3[2] = new vector2d(86.4,10.8);

        startCentroidk6[0] = new vector2d(10.5,40.3);
        startCentroidk6[1] = new vector2d(46.5,28.1);
        startCentroidk6[2] = new vector2d(70.4,10.8);
        startCentroidk6[3] = new vector2d(50.5,50.3);
        startCentroidk6[4] = new vector2d(26.1,68.3);
        startCentroidk6[5] = new vector2d(6.9 ,71.6);

        /*
        * Der centroid vector muss bei useRandomCentroids = true nicht angegeben werden. Bei useRandomCentroids = false wird die funktion jedoch ohne diesen vector array nicht funktionieren
        * und einfach -1 zurückgeben
        * */

        //k = 3
        cluster.performCalculations(3,Cluster.centroidCalculationMethod.Median,false,startCentroidk3);
        cluster.performCalculations(3,Cluster.centroidCalculationMethod.ArithmeticMean,false,startCentroidk3);
        cluster.performCalculations(3,Cluster.centroidCalculationMethod.HarmonicMean,false,startCentroidk3);
        cluster.performCalculations(3,Cluster.centroidCalculationMethod.RootMean,false,startCentroidk3);
        //k = 6
        cluster.performCalculations(6,Cluster.centroidCalculationMethod.Median,false,startCentroidk6);
        cluster.performCalculations(6,Cluster.centroidCalculationMethod.ArithmeticMean,false,startCentroidk6);
        cluster.performCalculations(6,Cluster.centroidCalculationMethod.HarmonicMean,false,startCentroidk6);
        cluster.performCalculations(6,Cluster.centroidCalculationMethod.RootMean,false,startCentroidk6);


        //Die aufgabenstellung sieht verlangt nur die berechnungen der benutzergewählten centroide
        //und nicht die der zufällig gewählten. Weshalb ich auch nur diese ausführe
        //Das programm ist jedoch auch zu diesen in der lage

        //Beispiel für die verwendung der zufälligen:
        //cluster.performCalculations(3,Cluster.centroidCalculationMethod.Median,true,null);




    }
}
