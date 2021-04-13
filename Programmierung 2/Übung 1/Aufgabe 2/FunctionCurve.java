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



public class FunctionCurve extends Application
{
    public FunctionCurve() { }

    @Override
    public void start(Stage stage)
    {
        Label la_c1 = new Label("c1: ");
        Label la_c2 = new Label("c2: ");
        Label la_d1 = new Label("d1: ");
        Label la_d2 = new Label("d2: ");

        Spinner<Double> sp_c1 = new Spinner<Double>(0, 1000, 1, 0.5);
        Spinner<Double> sp_c2 = new Spinner<Double>(0, 1000, 1, 0.5);
        Spinner<Double> sp_d1 = new Spinner<Double>(0, 1000, 0, 0.5);
        Spinner<Double> sp_d2 = new Spinner<Double>(0, 1000, 0, 0.5);

        //Enables keyboard input (Default is false)
        sp_c1.setEditable(true);
        sp_c2.setEditable(true);
        sp_d1.setEditable(true);
        sp_d2.setEditable(true);
        //Set spinner width & height
        sp_c1.setPrefSize(75, 10);
        sp_c2.setPrefSize(75, 10);
        sp_d1.setPrefSize(75, 10);
        sp_d2.setPrefSize(75, 10);

        //Button for draw operation
        Button button_draw = new Button("draw: sin(c1 * x + d1) * cos(c2 * x + d2)");

        //HBox = Horizontal aligned box
        HBox topsideControls = new HBox(5);

        //Used to proper align the spinners with the labels
        topsideControls.setAlignment(Pos.CENTER);

        topsideControls.getChildren().addAll(
                la_c1,sp_c1,
                la_d1,sp_d1,
                la_c2,sp_c2,
                la_d2,sp_d2,
                button_draw);

        //The canvas we will draw on (note that FunctionCanvas extends from canvas)
        FunctionCanvas newFunctionCanvas = new FunctionCanvas(650,400);

        newFunctionCanvas.setFunctionParameters(1,0,1,0);

        newFunctionCanvas.drawFunctionCurve();

        //Button for reset operation
        Button button_reset = new Button("reset");

        EventHandler<ActionEvent> drawEvent = event ->
        {
            newFunctionCanvas.setFunctionParameters(sp_c1.getValue(),sp_d1.getValue(),
                    sp_c2.getValue(),sp_d2.getValue());

            newFunctionCanvas.drawFunctionCurve();
        };

        EventHandler<ActionEvent> resetEvent = event ->
        {
            sp_c1.getValueFactory().setValue(1.d);
            sp_d1.getValueFactory().setValue(0.d);
            sp_c2.getValueFactory().setValue(1.d);
            sp_d2.getValueFactory().setValue(0.d);

            newFunctionCanvas.setFunctionParameters(1,0,1,0);
            newFunctionCanvas.drawFunctionCurve();
        };

        button_draw.setOnAction(drawEvent);
        button_reset.setOnAction(resetEvent);

        //VBox = Vertically alligned box
        VBox content = new VBox(10);
        //Horizontally center objects
        //(This doesn't affect objects that are inside other layout objects like the top controls)
        content.setAlignment(Pos.TOP_CENTER);

        //Empty space
        Region spacer = new Region();
        spacer.setPrefHeight(10);

        //Add objects box
        content.getChildren().addAll(spacer,topsideControls,newFunctionCanvas,button_reset);

        //The pane that will act as the root
        Pane root = new Pane();
        root.getChildren().addAll(content);

        //Creating the scene
        Scene scene = new Scene(root, 670, 500);

        //Show the stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
