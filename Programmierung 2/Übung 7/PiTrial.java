package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.System.out;

public final class PiTrial extends Application implements Runnable
{
    private Label estimatePiLabel;
    private Label trialCountLabel;
    private Label totalTimeLabel;

    private Spinner<Integer> threadNumSpinner;

    private final int cpuCoresAvailable;
    private int iThreadCount;

    private int iThreadFinished = 0;

    enum States
    {
        notActive,
        active,
        paused
    }

    States curState = States.notActive;

    Executor workExecutor;

    //We use that to control when the gui components get updated
    final Object guiUpdateLock = new Object();

    final Object pauseLock = new Object();

    public PiTrial()
    {
        this.cpuCoresAvailable = Runtime.getRuntime().availableProcessors();
        this.iThreadCount = 3;

        this.workExecutor = Executors.newFixedThreadPool(cpuCoresAvailable);
    }

    private static BorderPane createBorderPane(String leftLabelText, Node right)
    {
        Label label = new Label(leftLabelText);
        BorderPane pane = new BorderPane();
        pane.setLeft(label);
        pane.setRight(right);
        pane.setBorder(new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return pane;
    }
    private BorderPane createLabelWithSLider(String labelName)
    {
        BorderPane pane = new BorderPane();

        pane.setLeft(new Label(labelName));

        Spinner<Integer> spinner = new Spinner<Integer>
                (1, cpuCoresAvailable,3, 1);

        spinner.setPrefSize(80, 10);

        pane.setRight(spinner);

        threadNumSpinner = spinner;

        pane.setBorder(new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        return pane;
    }

    @Override
    public void start(Stage stage)
    {
        final int width = 400;
        final int height = 220;

        VBox vbox = new VBox();
        ObservableList<Node> vboxChildren = vbox.getChildren();


        vboxChildren.add(createLabelWithSLider("Number"));



        vboxChildren.add(createBorderPane("  Actual value of pi: ", new Label(String.format("%.15f  ", Math.PI))));

        estimatePiLabel = new Label(String.format("%.15f  ", 0f));
        vboxChildren.add(createBorderPane("  Approximated value of pi: ", estimatePiLabel));

        trialCountLabel = new Label(String.format("%d  ", 0));
        vboxChildren.add(createBorderPane("  Number of trials: ", trialCountLabel));

        totalTimeLabel = new Label();
        vboxChildren.add(createBorderPane("  Total time elapsed (sec): ", totalTimeLabel));




        Button button = new Button("Start/Pause");

        button.setOnAction(event ->
        {
            switch (curState)
            {
                case active:
                    //Pause (Threads check on there own if the need to wait)
                    curState = States.paused;

                    break;
                case notActive:
                    //Start

                    //Reset results
                    initPiTrial();

                    //Get the wanted thread count from spinner
                    iThreadCount = threadNumSpinner.getValue();
                    //Deactivate the spinner
                    threadNumSpinner.setDisable(true);
                    //Init threads
                    initThreads();
                    curState = States.active;

                    break;
                case paused:
                    curState = States.active;

                    //Continue (Wake up worker threads)
                    synchronized (pauseLock)
                    {
                        pauseLock.notifyAll();
                    }
            }
        });

        vboxChildren.add(button);


        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5.);
        vbox.setPadding(new Insets(0, 5, 0, 5));

        Scene scene = new Scene(vbox, width, height);

        stage.setTitle(this.getClass().getSimpleName());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();

        //initPiTrial();
        Thread thr = new Thread(this);
        thr.start();

        stage.setOnCloseRequest(event -> thr.interrupt());

        stage.show();

        initPiTrial();
    }

    private void initPiTrial()
    {
        start = System.nanoTime();
        trials = 0;
        inCircle = 0;
        timeElapsed = 0;
        totalTimeLabel.setText("n/a  ");
    }

    private void initThreads()
    {
        long start = System.nanoTime();

        for (int i = 0; i < iThreadCount; i++)
        {
            workExecutor.execute(new PiTrialThread(this, Integer.MAX_VALUE / iThreadCount));
        }
        timeElapsed += System.nanoTime() - start;
    }

    private long start;
    private long timeElapsed;
    private long trials;
    private long inCircle;


    synchronized void addTrials(long trials, long inCircle)
    {
        this.trials   += trials;
        this.inCircle += inCircle;

        //Notify our gui update thread
        synchronized (guiUpdateLock)
        {
            guiUpdateLock.notify();
        }
    }

    synchronized void threadDone()
    {
        out.printf("%s done\n", Thread.currentThread());

        //Super professional method for checking if all threads are finished
        iThreadFinished++;
        if (iThreadCount <= iThreadFinished)
        {
            iThreadFinished = 0;
            threadNumSpinner.setDisable(false);
            curState = States.notActive;
        }
    }




    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                synchronized (guiUpdateLock)
                {
                    guiUpdateLock.wait();
                }

                Platform.runLater(() ->
                        {
                            estimatePiLabel.setText(String.format("%.15f  ", 4 * ((double) inCircle / trials)));
                            trialCountLabel.setText(String.format("%d  ", trials));
                            totalTimeLabel.setText(String.format("%.3f  ", (double) (timeElapsed + (System
                                .nanoTime() - start)) / 1E9));
                        }
                );
            }
            catch (InterruptedException e)
            {
                break;
            }
        }
    }
}