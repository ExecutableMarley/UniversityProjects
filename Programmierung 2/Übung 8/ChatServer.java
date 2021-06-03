package com.company;

import java.util.LinkedList;
import java.util.List;

public class ChatServer
{
    /**Contains all connected clients*/
    List<MessageClient> clientList = new LinkedList<>();
    /**Id of the server*/
    String id;

    private static int iServerCount = 0;
    //Default Constructor that auto creates an id
    ChatServer()
    {
        this(ChatServer.class.getSimpleName() + " " + iServerCount++);
    }
    //Constructor that uses custom id
    ChatServer(String id)
    {
        this.id = id;
    }
    //Constructor that uses custom id and adds any number of clients
    ChatServer(String id, MessageClient ... clients)
    {
        this.id = id;

        addAllClients(clients);
    }

    @Override
    public String toString()
    {
        return id;
    }

    /**Used to check if an user with the specified id already exists*/
    boolean userExists(String id)
    {
        for (MessageClient client : clientList)
        {
            if (client.getId().equals(id))
                return true;
        }
        return false;
    }
    boolean userExists(ComSupport checkedClient)
    {
        for (MessageClient client : clientList)
        {
            if (client.getId().equals(checkedClient.getId()))
                return true;
        }
        return false;
    }

    void addClient(MessageClient client)
    {
        //Check if id is already used
        if (userExists(client))
            return;

        client.setChatServer(this);

        clientList.add(client);

        System.out.printf(this + ": User [%s] added\n", client.getId());
    }

    void addAllClients(MessageClient ... clients)
    {
        for (MessageClient client : clients)
        {
            addClient(client);
        }
    }
    /**
     * Removes an client from the server
     * @param client The client that the server will remove
     *
     * @return Returns true if the operation was successful
     * */
    boolean removeClient(MessageClient client)
    {
        if (clientList.remove(client))
        {
            System.out.printf(this + ": User [%s] removed\n", client.getId());
            return true;
        }
        return false;
    }

    void receiveMessage(Message message)
    {
        sendToClients(message);
    }

    void sendToClients(Message message)
    {
        for (MessageClient client : clientList)
        {
            client.receiveMessage(message);
        }
    }
}
