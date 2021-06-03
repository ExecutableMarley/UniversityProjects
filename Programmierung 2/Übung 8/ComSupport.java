package com.company;

/**
 * Guarantees that a class which implements this interface has
 * an "receiveMessage" method to receive Messages from type "Message"
 * and provides the getId method which can be used to for identification
 */
public interface ComSupport
{
    public boolean receiveMessage(Message message);

    public String getId();
}
