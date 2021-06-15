package com.company;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

public class ArcFrame extends Application
{
    int width = 300; // initial width
    int height = 300; // initial height
    int margin = 20;
    Canvas canvas;
    private ArcFillThread[] arcFillThreads;
    private int numberOfThreads = 3;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Filling Arcs");
        BorderPane root = new BorderPane();
        canvas = new Canvas(width,height);
        root.setCenter(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        arcFillThreads = new ArcFillThread[numberOfThreads];

        int angle = (int) (360d / numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++)  {
            int startAngle = (int) (360d / numberOfThreads * i);
            int mod = i % 3;
            int r = 255 * (1 & (1 << mod));
            int g = 255 * ((2 & (1 << mod)) >> 1);
            int b = 255 * ((4 & (1 << mod)) >> 2);

            Color color = new Color(r/255.0, g/255.0, b/255.0, 1.0);

            ArcFillThread current = new ArcFillThread(this, color, startAngle, angle);

            arcFillThreads[i] = current;

            if (i < 1) continue;

            // Registering a circular single linked list, i.e. a thread knows its successor.
            ArcFillThread predecessor = arcFillThreads[i - 1];
            if (predecessor != null) {
                // In particular, the successor of the predecessor thread is the current one.
                predecessor.successor = current;
            }
        }

        // The successor of the last one in the array will be the first one.
        ArcFillThread successor = arcFillThreads[0];
        if (successor != null) {
            arcFillThreads[numberOfThreads - 1].successor = successor;
        }

        // start animation timer and all threads
        final AnimationTimer timer = new AnimationTimer() {
            public void handle(long timestamp) {
                for (int i = 0; i < numberOfThreads; i++) arcFillThreads[i].draw(); } };
        timer.start();
        for (int i = 0; i < numberOfThreads; i++) arcFillThreads[i].start();
        // we might want to wait until all threads are really started before
        // we access their state

        //Super professional hardcoded sleep? We dont do that here
        //That would be Hochgradig unschön :D
        while (arcFillThreads[0].getState() != Thread.State.WAITING)
        {
            try
            {
                Thread.sleep(10);
            } catch(InterruptedException e)
            {
            }
        }
        //First thread is no waiting so i assume that the other
        //Threads should be also waiting when the first one finishes

        //Wake up first thread
        synchronized (arcFillThreads[0].syncObject)
        {
            arcFillThreads[0].syncObject.notify();
        }
    }
}