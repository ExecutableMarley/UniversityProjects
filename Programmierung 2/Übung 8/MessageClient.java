package com.company;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class MessageClient implements ComSupport
{
    //Identifier of client
    private String id;
    //The chat server that will distribute messages between clients
    private ChatServer chatServer;
    //
    List<Message> messageList = new LinkedList<>();

    //TextField where messages are typed into
    TextField textField;
    //Button which sends the message
    Button sendButton;

    //Vbox will show the graphical representation of the messages
    VBox vbox;
    //
    ScrollPane scrollPane;

    MessageClient(String id)
    {
        this.id = id;

        BorderPane root = new BorderPane();

        //Top Menu Bar
        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("File");

        MenuItem saveButton = new MenuItem("Backup");

        MenuItem loadButton = new MenuItem("Restore");

        saveButton.setOnAction(e -> backupMessages());

        loadButton.setOnAction(e -> restoreMessages());

        menu.getItems().addAll(saveButton,loadButton);

        menuBar.getMenus().add(menu);

        root.setTop(menuBar);

        //Center Message area
        vbox = new VBox();

        //vbox.setPrefHeight(10000);

        scrollPane = new ScrollPane(vbox);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        vbox.prefWidthProperty().bind(scrollPane.widthProperty());

        root.setCenter(scrollPane);



        //Bottom Text input and Send
        HBox bottomBar = new HBox();

        textField = new TextField();

        textField.setPrefWidth(10000);

        textField.setOnAction(sendEvent);

        sendButton = new Button("Send");

        sendButton.setMinWidth(50);

        sendButton.setOnAction(sendEvent);

        bottomBar.getChildren().addAll(textField, sendButton);

        root.setBottom(bottomBar);



        Stage stage = new Stage();

        stage.setTitle(id);

        stage.setResizable(false);

        stage.setScene(new Scene(root, 310, 400));
        stage.show();

        stage.setOnCloseRequest(e ->
        {
            //Detach from server
            chatServer.removeClient(this);
        });
    }

    @Override
    public String toString()
    {
        return id;
    }

    public String getId()
    {
        return id;
    }

    EventHandler<ActionEvent> sendEvent = e ->
    {
        //Send message to server
        sendMessage(textField.getText());
        //Clear text field
        textField.clear();
    };

    /**
     * Used to set the current chat server of an client
     * Will also disconnect from current server if already connected*/
    public void setChatServer(ChatServer chatServer)
    {
        //Disconnect from current server
        if (this.chatServer != null)
            this.chatServer.removeClient(this);
        //Set new server
        this.chatServer = chatServer;
    }

    /**
     * Used to send an message to the server using the id of this client
     * @param text An String containing the message text
     * */
    boolean sendMessage(String text)
    {
        Message message = new Message(this, text);

        //Check if an server exists
        if (chatServer != null)
        {
            chatServer.receiveMessage(message);

            return true;
        }
        //We could simply print it locally if not

        return false;
    }
    /**
     * Used to send an client an message which will be printed
     * on the chat client window
     * */
    public boolean receiveMessage(Message message)
    {
        //Save message to our client intern list
        messageList.add(message);

        printMessage(message);

        return true;
    }

    private void printMessage(Message message)
    {
        //Special formatting
        if (message.id.equals(this.id))
        {
            vbox.getChildren().add(new GraphicalMessageBox
                    ("", message.message, true));
        }
        else
        {
            vbox.getChildren().add(new GraphicalMessageBox
                    (message.id, message.message, false));
        }
    }

    private void clearChat()
    {
        vbox.getChildren().clear();

        messageList.clear();
    }

    private void backupMessages()
    {
        //Todo: Save messageList as extern file
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(id + ".cfg"));

            for (Message message : messageList)
            {
                writer.append(message.id + ":" + message.message + "\n");
            }

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private  void restoreMessages()
    {
        BufferedReader reader;

        try
        {
            reader = new BufferedReader(new FileReader(id + ".cfg"));

            List<Message> newList = new LinkedList();

            while (reader.ready())
            {
                String curLine = reader.readLine();

                String[] tokens = curLine.split(":", 2);

                if (tokens.length != 2)
                    continue;

                newList.add(new Message(tokens[0], tokens[1]));
            }

            newList.addAll(messageList);

            messageList = newList;
        }
        catch (FileNotFoundException e)
        {
            System.out.println(this + ": No Backup File was found");
            return;
        }
        catch (IOException e)
        {
            return;
        }

        //Recreate VBOX message area
        /*Todo: Load messages from extern file into messageList*/
        //Feed the whole list into printMessage

        vbox.getChildren().clear();

        for (Message message : messageList)
        {
            printMessage(message);
        }
    }
}
