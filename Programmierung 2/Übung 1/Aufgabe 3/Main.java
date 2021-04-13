package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Spinner; //Spinner object
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
//Unused imports
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar; //Menu bar object
import javafx.scene.control.Menu;

//Class that combines an spinner with an label
class CustomSpinner extends HBox
{
    private Label label;
    private Spinner <Double> spinner;
    //Initial value that gets set inside the constructor
    private double initValue;

    CustomSpinner(String name, double initValue, double amountToStepBy)
    {
        this.initValue = initValue;

        //Label
        label = new Label(name);

        //Spinner
        spinner = new Spinner<Double>
                (Double.MIN_VALUE, Double.MAX_VALUE, initValue, amountToStepBy);

        spinner.setEditable(true);

        spinner.setPrefSize(75, 10);

        //Layout
        this.getChildren().addAll(label,spinner);

        this.setAlignment(Pos.CENTER);
    }
    CustomSpinner(String name)
    {
        new CustomSpinner(name,0,1);
    }

    double getValue()
    {
        return spinner.getValue();
    }
    void setValue(double newValue)
    {
        spinner.getValueFactory().setValue(newValue);
    }

    void resetValue()
    {
        setValue(initValue);
    }
}


public class FunctionCurve extends Application
{
    public FunctionCurve() { }

    @Override
    public void start(Stage stage)
    {
        CustomSpinner c1 = new CustomSpinner("c1: ", 1, 0.5);
        CustomSpinner c2 = new CustomSpinner("c2: ", 1, 0.5);
        CustomSpinner d1 = new CustomSpinner("d1: ", 0, 0.5);
        CustomSpinner d2 = new CustomSpinner("d2: ", 0, 0.5);

        //Button for draw operation
        Button button_draw = new Button("draw: sin(c1 * x + d1) * cos(c2 * x + d2)");

        //HBox = Horizontal aligned box
        //HBox topsideControls = new HBox(5);
        FlowPane topsideControls = new FlowPane();

        //Used to proper align the spinners with the labels
        topsideControls.setAlignment(Pos.CENTER);

        topsideControls.getChildren().addAll(
                c1,
                d1,
                c2,
                d2,
                button_draw);

        //The canvas we will draw on (note that FunctionCanvas extends from canvas)
        FunctionCanvas canvas = new FunctionCanvas(650,400);



        //Button for reset operation
        Button button_reset = new Button("reset");

        //Event handler for the draw button
        EventHandler<ActionEvent> drawEvent = event ->
        {
            canvas.drawFunctionCurve(c1.getValue(),d1.getValue(),
                    c2.getValue(),d2.getValue());
        };

        //Event handler for the reset button
        EventHandler<ActionEvent> resetEvent = event ->
        {
            c1.resetValue();
            d1.resetValue();
            c2.resetValue();
            d2.resetValue();

            canvas.drawFunctionCurve(c1.getValue(),d1.getValue(),
                    c2.getValue(),d2.getValue());
        };

        button_draw.setOnAction(drawEvent);
        button_reset.setOnAction(resetEvent);

        //VBox = Vertically alligned box
        VBox content = new VBox(10);

        //Horizontally center objects
        //(This doesn't affect objects that are inside other layouts objects like the top controls)
        content.setAlignment(Pos.TOP_CENTER);

        //Empty space
        Region spacer = new Region();
        spacer.setPrefHeight(10);

        //Add objects box
        content.getChildren().addAll(spacer,topsideControls,canvas,button_reset);


        //Bind the Flow pane width to the root width
        topsideControls.prefWidthProperty().bind(content.widthProperty());
        //Bind our canvas to the root width
        //Note that we will also need and event handler to auto redraw the canvas when its width changes
        canvas.widthProperty().bind(content.widthProperty());


        //Creating the scene
        Scene scene = new Scene(content, 670, 530);

        //Show the stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();


        //Redraw our canvas for the first time
        canvas.drawFunctionCurve(1,0,1,0);
    }
