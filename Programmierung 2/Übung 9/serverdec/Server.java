package com.company.serverdec;

import com.company.common.Handshake;
import com.company.common.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread
{
    private static Server instance;
    private final int port = 3321;
    private final ArrayList<ConnectedClient> clientsList = new ArrayList();
    private final ArrayList<Message> messageList = new ArrayList();
    private int clientID;

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

    @Override
    public void run()
    {
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(3321);

            while(!Thread.currentThread().isInterrupted())
            {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ConnectedClient connectedClient = new ConnectedClient(socket);
                this.clientsList.add(connectedClient);
                connectedClient.start();
            }
        } catch (IOException var12)
        {
            var12.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed())
                {
                    serverSocket.close();
                }
            } catch (IOException var11)
            {
                var11.printStackTrace();
            }

        }
    }

    public synchronized int getClientID() {
        return this.clientID++;
    }

    public synchronized void addMessageToList(Message message) {
        this.messageList.add(message);
    }

    public synchronized ArrayList<Message> getMessageList() {
        return this.messageList;
    }

    public synchronized ArrayList<ConnectedClient> getClientsList() {
        return this.clientsList;
    }
}
