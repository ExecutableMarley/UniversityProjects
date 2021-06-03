package com.company;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {

       MessageClient user1 = new MessageClient("User 1");
       MessageClient user2 = new MessageClient("User 2");
       //MessageClient user3 = new MessageClient("User 3");
       //MessageClient user4 = new MessageClient("User 4");

        ChatServer server1 = new ChatServer();
        //ChatServer server2 = new ChatServer();

        server1.addAllClients(user1, user2);


        user1.sendMessage("Übungsaufgaben zu der Vorlesung \"Fortgeschrittene Programmierung\"");
        user2.sendMessage("Hier am Beispiel von einem Chat");

        //server2.addAllClients(user3, user4);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
