package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class A1 extends Application
{
    public void init()
    {
    }

    public void start(Stage primaryStage) throws Exception
    {
        GridPane root = new GridPane();


        Button[][]buttons = new Button[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
                String name = Math.random() > 0.50 ? "O" : "X";

                Button b = new Button(name);
                //To big on purpose for auto scale
                b.setPrefSize(1000,1000);

                root.add(b, i, j);
            }
        
        Scene scene = new Scene(root, 200, 200);
        primaryStage.setTitle("Aufgabe A1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
