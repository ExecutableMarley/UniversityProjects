package com.company.common;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Message implements Serializable
{
    private final String text;
    private final int id;
    private long timestamp;

    public Message(String text, int id)
    {
        this.text = text;
        this.id = id;
        timestamp = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    static public Message getMessageFromObject(Object obj) throws IllegalAccessException
    {
        int messageId = -1;
        for (Field f : obj.getClass().getDeclaredFields())
        {
            if (f.getType().equals(int.class))
            {
                f.setAccessible(true);
                messageId = (int) f.get(obj);
                break;
            }
        }

        String messageText = "-1";
        for (Field f : obj.getClass().getDeclaredFields())
        {
            if (f.getType().equals(String.class))
            {
                f.setAccessible(true);
                messageText = (String) f.get(obj);
                break;
            }
        }

        //Sanity check
        if (messageId == -1 || messageText.equals("-1"))
            return null;

        return new Message(messageText, messageId);
    }
}
