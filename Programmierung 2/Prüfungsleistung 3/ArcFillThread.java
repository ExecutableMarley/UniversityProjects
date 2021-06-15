package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

class ArcFillThread extends Thread
{
    ArcFillThread successor;
    private Color color;
    private ArcFrame frame;
    private int startAngle;
    private int angle;
    private int angleToFill=0;
    private boolean fillNotClear=true;

    ArcFillThread(ArcFrame frame, Color color, int startAngle, int angle)
    {
        this.frame = frame;
        this.color = color;
        this.startAngle = startAngle;
        this.angle = angle;
        this.setDaemon(true);
    }

    //Object we use to control sync
    Object syncObject = new Object();

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                //Wait until someone wakes us up
                synchronized (syncObject)
                {
                    syncObject.wait();
                }

                while (angleToFill <= angle)
                {
                    angleToFill++;
                    Thread.sleep(10);
                }

                fillNotClear=!fillNotClear;
                angleToFill=0;

                //Notify next thread that its time to work
                synchronized (successor.syncObject)
                {
                    successor.syncObject.notify();
                }

            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void draw()
    {
        GraphicsContext gc = frame.canvas.getGraphicsContext2D();
        gc.setFill(fillNotClear ? color : Color.WHITE);
        int w=(int) gc.getCanvas().getWidth();
        int h=(int) gc.getCanvas().getHeight();
        gc.fillArc(frame.margin, frame.margin,
                w - 2 * frame.margin,
                h - 2 * frame.margin,
                startAngle,
                angleToFill,
                ArcType.ROUND);
    }
}