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
//java util.* can also be used
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;
import java.util.Comparator;


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
        return Integer.toString(get());
    }
}
//Simply 2d vector class with math operations
class vector2d
{

    //[Fields]

    double x;
    double y;

    //[Constructors]

    vector2d(double newX, double newY)
    {
        x = newX;
        y = newY;
    }
    vector2d()
    {
        x = 0;
        y = 0;
    }

    //[Static Functions]

    //returns vector a - vector b
    public static vector2d subtract(vector2d a, vector2d b)
    {
        return new vector2d(a.x - b.x,a.y - b.y);
    }
    //returns vector a + vector b
    public static vector2d add(vector2d a, vector2d b)
    {
        return new vector2d(a.x + b.x,a.y + b.y);
    }
    //returns vector a / b
    public static vector2d divide(vector2d a, double b)
    {
        return new vector2d(a.x / b,a.y / b);
    }

    public static double euclideanDistance(vector2d a, vector2d b)
    {
        return a.subtract(b).getLength();
    }

    //[Class Methods]

    //Returns this vector - vector b
    public vector2d subtract(vector2d b)
    {
        return new vector2d(this.x - b.x,this.y - b.y);
    }
    //Returns this vector + vector b
    public vector2d add(vector2d b)
    {
        return new vector2d(this.x + b.x,this.y + b.y);
    }
    //Returns this vector / b
    public vector2d divide(double b)
    {
        return new vector2d(this.x / b,this.y / b);
    }
    //Returns the euclidean vector length
    public double getLength()
    {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }
    public double sumOfPower()
    {
        return Math.pow(x,2) + Math.pow(y,2);
    }

    @Override
    public String toString()
    {
        /*return "vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';*/

        return String.format("vector2d{x=%f,y=%f}", x, y);
    }
}
//Class that represents a cluster
class Cluster
{
    vector2d centroid;

    LinkedList<vector2d> clusterList = new LinkedList<vector2d>();

    enum centroidCalculationMethod
    {
        Median,
        ArithmeticMean,
        HarmonicMean,
        RootMean
    }

    //[Constructors]
    //Cluster with given centroid
    Cluster(vector2d newCenter)
    {
        centroid = newCenter;
    }
    //Cluster with random centroid
    Cluster()
    {
        Random randGenerator = new Random();
        randGenerator.setSeed(System.currentTimeMillis());

        centroid = new vector2d(
                randGenerator.nextDouble() * 100,
                randGenerator.nextDouble() * 100);
    }

    //[Class Methods]

    //Add new 2d vector to cluster
    void add(vector2d newVector)
    {
        clusterList.add(newVector);
    }
    //Manually set the center
    void setCenter(vector2d newCenter)
    {
        centroid = newCenter;
    }

    //[Center calculation functions]

    vector2d medianCenter()
    {
        //Sorts the list
        clusterList.sort(new Comparator<vector2d>()
        {
            @Override
            public int compare(vector2d a, vector2d b)
            {
                return (int)Math.ceil((a.x + a.y) - (b.x + b.y));
            }
        });


        vector2d out;

        int size = clusterList.size();

        //size is even
        if (clusterList.size() % 2 == 0)
        {
            out = clusterList.get(size / 2).add(clusterList.get(size / 2 + 1)).divide(2);
        }
        else
        {
            out = clusterList.get(size / 2);
        }
        return out;
    }

    vector2d arithmethicMeanCenter()
    {
        vector2d center = new vector2d(0,0);
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            center = center.add(curIterator.next());
        }

        return center.divide(clusterList.size());
    }

    vector2d harmonicMeanCenter()
    {
        vector2d center = new vector2d(0,0);
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            vector2d curVector = curIterator.next();
            center = vector2d.add(center, new vector2d(1 / curVector.x,1 / curVector.y));
        }

        return new vector2d(clusterList.size() / center.x,clusterList.size() / center.y);
    }

    vector2d rootMeanCenter()
    {
        vector2d center = new vector2d(0,0);
        for (Iterator<vector2d> curIterator = clusterList.iterator();curIterator.hasNext();)
        {
            vector2d curVector = curIterator.next();
            center = center.add(new vector2d(curVector.x * curVector.x, curVector.y * curVector.y));
        }

        center = new vector2d(Math.sqrt(center.x / clusterList.size()),Math.sqrt(center.y / clusterList.size()));
        return center;
    }
    //We calculate the cluster center with this
    void calculateCenter(centroidCalculationMethod type)
    {
        //No elements in our list -> return
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
            quality += Math.pow(vector2d.euclideanDistance(centroid,curIterator.next()), 2);
        }
        return quality;
    }
    //Used to output the center vector as string
    String centroidToString()
    {
        return String.format("{X:%f| Y:%f", centroid.x, centroid.y);
    }

    @Override
    public String toString()
    {
        String out = "[clusterList] \n";

        for (Iterator <vector2d> i = clusterList.iterator(); i.hasNext();)
        {
            out += i.next().toString();
        }

        out += "[Centroid] \n";
        out += centroid.toString();

        return out;
    }
}
//Class to handle the clustering

/**
 * Class with the ability to perform k-means clustering calculations
 * on the 2d vector list it holds
 *
 * https://en.wikipedia.org/wiki/K-means_clustering
* */
class KMeansClustering
{
    //[Fields]

    LinkedList<vector2d> coordList = new LinkedList<vector2d>();

    //[Constructors]

    KMeansClustering()
    {

    }



    //[Main Functions]

    private double performCalculations(Cluster.centroidCalculationMethod type,int k, boolean useRandomCentroids, vector2d[] centroids)
    {
        //Argument sanity check
        if (!useRandomCentroids)
            if (centroids == null || centroids.length < k)
                return -1;

        //init timer
        Timer calculationTimer = new Timer();

        //Init clusters
        Cluster[] clusterArray = new Cluster[k];

        //Set centroids
        for (int i = 0; i < k; i++)
        {
            if (useRandomCentroids)
                clusterArray[i] = new Cluster();
            else
                clusterArray[i] = new Cluster(centroids[i]);
        }

        //Initial Cluster Sorting
        for (int i = 0; i < coordList.size(); i++)
        {
            int bestIndex = 0;
            double bestDist = Double.MAX_VALUE;

            for (int c = 0; c < k; c++)
            {
                double thisDist = vector2d.euclideanDistance(coordList.get(i),clusterArray[c].centroid);

                if (bestDist > thisDist)
                {
                    bestDist = thisDist;
                    bestIndex = c;
                }
            }

            clusterArray[bestIndex].add(coordList.get(i));
        }

        //Calculate new centroids for the first time
        for (int i = 0; i < k;i++)
        {
            clusterArray[i].calculateCenter(type);
        }


        for (int objectsChanged = 11;objectsChanged >= 10; objectsChanged = 0)
        {
            for (int c = 0; c < k; c++) //Iterate through cluster
            {
                int bestIndex = 0;
                double bestDist = Double.MAX_VALUE;

                Iterator<vector2d> curIterator = clusterArray[c].clusterList.iterator();

                //Iterate through every point in that cluster
                while (curIterator.hasNext())
                {
                    vector2d currentPoint = curIterator.next();

                    for (int g = 0; g < k; g++) //Compare current point to cluster centroid
                    {
                        double temp = vector2d.euclideanDistance(clusterArray[g].centroid,currentPoint);
                        if (bestDist > temp)
                        {
                            bestDist = temp;
                            bestIndex = c;
                        }
                    }
                    //c is our current cluster index. If bestIndex != c -> move vector to bestIndex cluster
                    if (bestIndex != c)
                    {
                        //Remove from current
                        curIterator.remove();
                        //and add to new
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
            case Median:        consoleOut += "Median";break;
            case ArithmeticMean:consoleOut += "Arithmetisches Mittel";break;
            case HarmonicMean:  consoleOut += "Harmonisches Mittel";break;
            case RootMean:      consoleOut += "Quadratisches Mittel";
        }
        consoleOut += "\nCluster Number (k) = " + Integer.toString(k);

        System.out.println(consoleOut);

        //Assemble and output centroid positions
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

    /**
     * Performs the clustering operation
     *
     * @param k an integer which controls how many cluster are used
     * @param type specifies the method used for center calculation
     * @param centroids an 2d vector array containing the initial centroids
     * @return an double which holds the calculated quality of the clustering
     * @see KMeansClustering
     * */
    public double performCalculations(Cluster.centroidCalculationMethod type,int k, vector2d[] centroids)
    {
        return performCalculations(type, k, false, centroids);
    }

    /**
     * Performs the clustering operation
     *
     * <p>
     *     This method uses randomly generated cluster centroids for the calculation
     *     The result will therefore be different each each time the function is called
     *
     * @param k an integer which controls how many cluster are used
     * @param type specifies the method used for center calculation
     * @return an double which holds the calculated quality of the clustering
     * @see KMeansClustering
     * */
    public double performCalculations(Cluster.centroidCalculationMethod type,int k)
    {
        return performCalculations(type, k, true, null);
    }


    //[Miscellaneous]
    //Used to extract the Coordinates from a text file specified by the given path
    boolean readDocumentFile(String documentPath)
    {
        File documentFile = new File(documentPath);

        //Check if the file exists
        if (!documentFile.exists())
        {
            return false;
        }

        try
        {
            Scanner fileScanner = new Scanner(documentFile);

            for (int i = 0; fileScanner.hasNextLine(); i++)
            {
                String curLine = fileScanner.nextLine();

                //Regex for simple extraction of number pairs
                String[] XYNUM = curLine.split("\\D+"); // " " should do it too

                //This line didn't contained 2 numbers -> wrong file format
                if (XYNUM.length < 2)
                    return false;

                coordList.add(new vector2d(Double.parseDouble(XYNUM[0]),Double.parseDouble(XYNUM[1])));
            }

            return true;
        }
        //File was not found
        catch (FileNotFoundException e)
        {
            e.toString();
        }
        //Double.parseDouble failed -> text file format is wrong
        catch (NumberFormatException e)
        {
            e.toString();
        }

        return false;
    }
}




public class Main
{
    public static void main(String[] args)
    {
        // write your code here

        KMeansClustering cluster = new KMeansClustering();

        //We read the file here. Path needs to be valid
        if (!cluster.readDocumentFile("./kMeans.txt"))
        {
            //Otherwise the program does nothing
            System.out.println("File either doesn't exist or had an wrong format");
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



        //k = 3
        cluster.performCalculations(Cluster.centroidCalculationMethod.Median,         3,startCentroidk3);
        cluster.performCalculations(Cluster.centroidCalculationMethod.ArithmeticMean, 3,startCentroidk3);
        cluster.performCalculations(Cluster.centroidCalculationMethod.HarmonicMean,   3,startCentroidk3);
        cluster.performCalculations(Cluster.centroidCalculationMethod.RootMean,       3,startCentroidk3);
        //k = 6
        cluster.performCalculations(Cluster.centroidCalculationMethod.Median,         6,startCentroidk6);
        cluster.performCalculations(Cluster.centroidCalculationMethod.ArithmeticMean, 6,startCentroidk6);
        cluster.performCalculations(Cluster.centroidCalculationMethod.HarmonicMean,   6,startCentroidk6);
        cluster.performCalculations(Cluster.centroidCalculationMethod.RootMean,       6,startCentroidk6);



        //Example for usage with random centroids (Obviously the results will differ every run)
        //cluster.performCalculations(Cluster.centroidCalculationMethod.Median,3);
    }
}
