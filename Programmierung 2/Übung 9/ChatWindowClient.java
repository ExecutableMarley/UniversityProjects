package com.company;

import com.company.common.Handshake;
import com.company.common.Message;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatWindowClient extends Thread
{
    private final VBox messageContainer = new VBox(5);
    private int chatWindowID;

    public void createChatWindow()
    {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("File");
        menuBar.getMenus().add(menu);

        menu.getItems().add(new MenuItem("Backup"));
        menu.getItems().add(new MenuItem("Restore"));
        root.setTop(menuBar);
        ScrollPane messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(300);
        messageScroller.setFitToWidth(true);
        messageContainer.setPrefHeight(373);
        messageContainer.setStyle("-fx-background-color: #f0e7de;");

        HBox hBox = new HBox();
        TextField msgField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setMinWidth(50);

        sendButton.setOnAction(actionEvent ->
        {
            if (!msgField.getText().equals(""))
                sendMessage(msgField.getText());
            msgField.clear();
        });

        msgField.setPrefWidth(250);
        hBox.getChildren().add(msgField);
        hBox.getChildren().add(sendButton);
        root.setCenter(messageScroller);
        root.setBottom(hBox);

        Scene scene = new Scene(root, 300, 400);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
            {
                if (!msgField.getText().equals(""))
                {
                    sendMessage(msgField.getText());
                }
                msgField.clear();
            }
        });

        connect();
        Stage stage = new Stage();
        stage.setTitle("User " + chatWindowID);
        stage.setScene(scene);
        stage.show();
    }

    public void displayReceiveMessage(Message msg)
    {
        String message;
        if (msg.getId() != chatWindowID)
        {
            message = "User " + msg.getId() + ": " + msg.getText();
        } else
        {
            message = msg.getText();
        }
        TextBubble msgBubble = new TextBubble(message, msg.getId(), chatWindowID);
        messageContainer.getChildren().add(msgBubble);
    }

    public void displayReceiveMessages(Message ... messages)
    {
        for (Message m : messages)
        {
            displayReceiveMessage(m);
        }
    }
    public void displayReceiveMessages(ArrayList<Message> messages)
    {
        for (Message m : messages)
        {
            displayReceiveMessage(m);
        }
    }

    @Override
    public void run()
    {
        //TODO waiting for new messages

        //Actually isConnected remains true if the connection is lost
        //Which means that its basically a while(true) loop
        while (socket.isConnected())
        {
            try
            {
                //Cast directly to message:
                //Message message = (Message) objIn.readObject();

                //Read object and use reflection:
                Object obj = objIn.readObject();

                Message message = Message.getMessageFromObject(obj);

                if (message != null)
                    Platform.runLater(() -> displayReceiveMessage(message));
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String text)
    {
        //TODO Send message to Server
        Message message = new Message(text, chatWindowID);

        try
        {
            objOut.writeObject(message);
            objOut.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    Socket socket;

    ObjectOutputStream objOut;
    ObjectInputStream objIn;

    public void connect()
    {
        //TODO Connect to Server
        try
        {
            socket = new Socket("localhost", 3321);

            objOut = new ObjectOutputStream(socket.getOutputStream());

            objIn = new ObjectInputStream(socket.getInputStream());

            //Cast directly
            //Handshake handshake = (Handshake)objIn.readObject();

            //Reflection approach
            Object obj = objIn.readObject();

            Handshake handshake = Handshake.getHandshakeFromObject(obj);

            chatWindowID = handshake.getId();

            displayReceiveMessages(handshake.getMessagesList());
        }
        catch (java.net.ConnectException e)
        {
            //Probably server doesn't exist
            System.out.println(e.toString());

            //Set id to something invalid and return before we start the thread
            chatWindowID = -1;
            return;
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        //Start receiving Thread when connected
        start();
    }
}
