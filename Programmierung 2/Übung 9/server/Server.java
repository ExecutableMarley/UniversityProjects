package com.company.server;

import com.company.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server extends Thread
{
    private static Server instance;

    LinkedList<ConnectedClient> clientList = new LinkedList<>();

    ArrayList<Message> messageList = new ArrayList<>();

    int currentUserCount = 0;

    //Server as Singleton
    public static Server getInstance()
    {
        if (instance == null)
        {
            synchronized (Server.class)
            {
                if (instance == null)
                {
                    instance = new Server();
                }
            }
        }
        return instance;
    }

    public Server()
    {

    }

    ServerSocket serverSocket;

    @Override
    public void run()
    {
        try
        {
            serverSocket = new ServerSocket(3321);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        while (true)
        {
            try
            {
                Socket socket = serverSocket.accept();

                ConnectedClient newClient = new ConnectedClient(socket,currentUserCount++);

                clientList.add(newClient);

                newClient.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Message> getMessageList()
    {
        return messageList;
    }

    public void distributeMessage(Message message)
    {
        messageList.add(message);

        for (ConnectedClient c : clientList)
        {
            c.sendMessageToClient(message);
        }
    }
}
