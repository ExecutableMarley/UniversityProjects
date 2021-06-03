package com.company;

public class Message
{
    String message;
    String id;

    ComSupport sender;

    Message(String id, String message)
    {
        this.id = id;
        this.message = message;
    }
    Message(ComSupport sender, String message)
    {
        this.sender = sender;
        this.id = sender.getId();
        this.message = message;
    }
}
