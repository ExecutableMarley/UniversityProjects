package com.company.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Handshake implements Serializable
{
    int id;
    ArrayList<Message> messagesList;

    public Handshake(int id, ArrayList<Message> messagesList)
    {
        this.id = id;
        this.messagesList = messagesList;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Message> getMessagesList() {
        return messagesList;
    }

    public static Handshake getHandshakeFromObject(Object obj) throws IllegalAccessException
    {
        int id = 0;
        for (Field f : obj.getClass().getDeclaredFields())
        {
            if (f.getType().equals(int.class))
            {
                f.setAccessible(true);
                id = (int) f.get(obj);
                break;
            }
        }

        ArrayList<Message> messages = null;

        for (Field f : obj.getClass().getDeclaredFields())
        {
            if (f.getType().equals(java.util.ArrayList.class))
            {
                f.setAccessible(true);
                messages = (ArrayList<Message>) f.get(obj);
                break;
            }
        }

        return new Handshake(id, messages);
    }
}
