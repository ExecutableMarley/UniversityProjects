package com.company;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.beans.EventHandler;

final class FunctionCanvas extends Canvas
{
    private double c1 = 1, d1 = 0, c2 = 1, d2 = 0;

    FunctionCanvas() {}
    FunctionCanvas(double width, double height)
    {
        this.setWidth(width);
        this.setHeight(height);

        //Redraw when the width property changes
        this.widthProperty().addListener(observable ->
        {
            drawFunctionCurve();
        });
    }

    //Canvas background color
    private static final Color backGroundColor = Color.color(0.95,0.95,0.95,0.95);
    private static final int yOffset = 20;

    void setFunctionParameters(double c1, double d1, double c2, double d2)
    {
        this.c1 = c1;
        this.d1 = d1;
        this.c2 = c2;
        this.d2 = d2;
    }

    void drawFunctionCurve()
    {
        GraphicsContext gc = getGraphicsContext2D();
        //Set fill color to the bc color
        gc.setFill(backGroundColor);
        //Clears the whole canvas
        gc.fillRect(0, 0, getWidth(), getHeight());

        double h = this.getHeight() - 2 * yOffset;
        int xOffset = 40;
        double w = this.getWidth() - 2 * xOffset;
        double yZero = yOffset + h / 2;
        gc.setFill(Color.BLACK);
        gc.fillText("f(x)", xOffset - 30, yOffset);
        gc.fillText("0", xOffset - 25, yZero + 5);
        gc.fillText("2\u03c0", xOffset + w - 5, yZero - 5);
        gc.setLineWidth(1.0);
        gc.setStroke(Color.GRAY);
        //X-Axis
        gc.strokeLine(xOffset, yOffset, xOffset, yOffset + h);
        //Y-Axis
        gc.strokeLine(xOffset, yZero, xOffset + w, yZero);
        gc.setLineWidth(1.5);
        gc.setStroke(Color.INDIANRED);
        //Draw Function Graph
        for (int x = 0; x < w; x++)
        {
            int y = (int) ((h / 2) * sinCosFunc(x * 2 * Math.PI / w));
            gc.strokeLine(xOffset + x, yZero - y, xOffset + x, yZero - y);
        }
    }

    void drawFunctionCurve(double c1, double d1, double c2, double d2)
    {
        setFunctionParameters(c1, d1, c2, d2);

        drawFunctionCurve();
    }


    private double sinCosFunc(double x)
    {
        return Math.sin(c1 * x + d1) * Math.cos(c2 * x + d2);
    }
}
