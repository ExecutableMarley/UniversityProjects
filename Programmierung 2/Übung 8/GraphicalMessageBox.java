package com.company;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class GraphicalMessageBox extends HBox
{
    String senderId;
    String messageBody;

    //Note: This is a completely mess. But it kinda works though
    GraphicalMessageBox(String senderId, String messageBody, boolean ownMessage)
    {
        Text mainBody = new Text();

        HBox pane = new HBox(mainBody);

        pane.setPadding(new Insets(0,5,0,5));

        mainBody.setWrappingWidth(230);

        if (ownMessage)
        {
            mainBody.setText(messageBody);

            mainBody.setTextAlignment(TextAlignment.RIGHT);

            //this.setAlignment(Pos.BASELINE_LEFT);

            pane.setBackground(new Background(new BackgroundFill(Color.PALEGREEN,
                    new CornerRadii(5), Insets.EMPTY)));

            this.setPadding(new Insets(5,10,0,50));
        }
        else
        {
            mainBody.setText(senderId + ": " + messageBody);

            //mainBody.setTextAlignment(TextAlignment.LEFT);
            //this.setAlignment(Pos.BASELINE_RIGHT);

            pane.setBackground(new Background(new BackgroundFill(Color.WHITE,
                    new CornerRadii(5), Insets.EMPTY)));

            this.setPadding(new Insets(5,50,0,10));
        }

        this.getChildren().addAll(pane);
    }
}
