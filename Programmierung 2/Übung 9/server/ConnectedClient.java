package com.company.server;

import com.company.common.Handshake;
import com.company.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectedClient extends Thread
{
    ObjectInputStream objIn;
    ObjectOutputStream objOut;

    Socket socket;

    int clientId;

    public ConnectedClient(Socket socket,int clientId)
    {
        this.socket = socket;
        this.clientId = clientId;
        try
        {
            this.objIn = new ObjectInputStream(socket.getInputStream());
            this.objOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        //Sending handshake
        sendHandshake(Server.getInstance().getMessageList());

        //isConnected doesn't even work
        while (socket.isConnected())
        {
            Message message = receiveMessage();

            if (message != null)
            {
                Server.getInstance().distributeMessage(message);
            }
        }
    }

    private void sendHandshake(ArrayList<Message> messageList)
    {
        try
        {
            objOut.writeObject(new Handshake(clientId, messageList));
            objOut.flush();
        } catch (IOException e)
        {
            System.out.printf("Sending Handshake to User [%d] failed", clientId);
        }
    }

    private Message receiveMessage()
    {
        try
        {
            //Cast directly to message:
            //Message message = (Message)objIn.readObject();

            //Read object and use reflection:
            Object obj = objIn.readObject();

            return Message.getMessageFromObject(obj);
        }
        catch (IOException | ClassNotFoundException | IllegalAccessException e)
        {
            System.out.printf("Receive message from User [%d] failed", clientId);
        }
        return null;
    }

    public void sendMessageToClient(Object object)
    {
        try
        {
            objOut.writeObject(object);
            objOut.flush();
        } catch (IOException e)
        {
            System.out.printf("Sending message to User [%d] failed", clientId);
        }
    }
}
