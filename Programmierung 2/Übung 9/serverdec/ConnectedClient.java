package com.company.serverdec;

import com.company.common.Handshake;
import com.company.common.Message;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class ConnectedClient extends Thread
{
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;

    public ConnectedClient(Socket socket)
    {
        //TODO create streams
        try
        {
            this.clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.clientInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException var3)
        {
            var3.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            this.clientOutputStream.writeObject(new Handshake(Server.getInstance().getClientID(), Server.getInstance().getMessageList()));
            this.clientOutputStream.flush();

            while(!Thread.currentThread().isInterrupted())
            {
                Message message = (Message)this.clientInputStream.readObject();
                Iterator var2 = Server.getInstance().getClientsList().iterator();

                while(var2.hasNext())
                {
                    ConnectedClient connectedClient = (ConnectedClient)var2.next();
                    Server.getInstance().addMessageToList(message);
                    connectedClient.sendMessageToClient(message);
                }
            }
        }
        catch (ClassNotFoundException | IOException var12)
        {
            var12.printStackTrace();
        } finally
        {
            try
            {
                this.clientInputStream.close();
                this.clientOutputStream.close();
            } catch (IOException var11)
            {
                var11.printStackTrace();
            }

        }
    }

    private void sendMessageToClient(Object object)
    {
        try
        {
            this.clientOutputStream.writeObject(object);
            this.clientOutputStream.flush();
        }
        catch (IOException var3)
        {
            var3.printStackTrace();
        }

    }
}
