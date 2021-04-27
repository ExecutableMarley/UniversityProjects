package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

class CustomButton extends Button
{
    int type;

    public int x,y;

    CustomButton(String initName, EventHandler<ActionEvent> someEvent, int x, int y)
    {
        super(initName);
        //Cheap auto scale once again
        setPrefSize(1000,1000);

        setOnAction(someEvent);

        this.x = x;
        this.y = y;
    }

    EventHandler<ActionEvent> someEvent2= event ->
    {

    };
}


public class A2 extends Application
{
    boolean flip = false;

    boolean gameActive = true;

    CustomButton [][]buttons;

    public void init()
    {

    }

    private boolean checkForDraw()
    {
        int freefields = 0;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
               if (buttons[i][j].getText().equals(" "))
                   freefields++;

        return freefields == 0;
    }

    private boolean checkForWinCondition(int x, int y, String token)
    {
        //int curToken;
        int tokensInRow = 0;
        
        for (int x2 = 0; x2 < 3; x2++)
        {
            if (buttons[x2][y].getText().equals(token))
                tokensInRow++;
        }

        if (tokensInRow == 3)
            return true;
        else
            tokensInRow = 0;

        for (int y2 = 0; y2 < 3; y2++)
        {
            if (buttons[x][y2].getText().equals(token))
                tokensInRow++;
        }

        if (tokensInRow == 3)
            return true;
        else
            tokensInRow = 0;
        //Diagonal 1
        for (int i = 0; i < 3; i++)
        {
            if (buttons[i][i].getText().equals(token))
                tokensInRow++;
        }

        if (tokensInRow == 3)
            return true;
        else
            tokensInRow = 0;
        //Diagonal 2
        for (int i = 0,j = 2 ; i < 3; i++,j--)
        {
            if (buttons[j][i].getText().equals(token))
                tokensInRow++;
        }

        if (tokensInRow == 3)
            return true;
        else
            tokensInRow = 0;

        return false;
    }

    public void start(Stage primaryStage) throws Exception
    {
        GridPane root = new GridPane();

        buttons = new CustomButton[3][3];

        EventHandler<ActionEvent> someEventHandler= event ->
        {
            //Just return if the game is over
            if (!gameActive)
                return;

            CustomButton b = (CustomButton) event.getSource();

            if (!b.getText().equals("X") && !b.getText().equals("O"))
            {
                b.setText(flip ? "O" : "X");

                //Checks if someone has won
                if (checkForWinCondition(b.x,b.y, flip ? "O" : "X") && gameActive)
                {
                    System.out.println((flip ? "O" : "X") + " Won The Game!");
                    gameActive = false;
                }
                //Checks if it is a draw
                if (checkForDraw() && gameActive)
                {
                    System.out.println("Its a draw!");
                    gameActive = false;
                }

                flip = !flip;
            }
        };

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
                buttons[i][j] = new CustomButton(" ", someEventHandler, i,j);

                root.add(buttons[i][j], i, j);
            }

        Scene scene = new Scene(root,200,200);
        primaryStage.setTitle("Aufgabe A2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
