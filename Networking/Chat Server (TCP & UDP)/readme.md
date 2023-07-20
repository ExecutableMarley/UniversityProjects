The goal of this project was to create a Python-based chat server/client, utilizing TCP and UDP networking protocols, with the aim of conducting a comparison between the two.

[USAGE]

1. Start UDP/server.py or TCP/server.py
2. Start any number of UDP/client.py or TCP/client.py
3. Enter Name and server address/port on the client interface
4. Use commands inside the client interface to send messages or perform operations


[Client Interface Usage] \
The client interface allows you to interact with other clients using the following commands:

@signin: Use this command to sign in to the chat application.
Syntax: @signin

@signout: Use this command to sign out of the chat application.
Syntax: @signout

@getclients: Use this command to retrieve a list of all available clients.
Syntax: @getclients

@all: Use this command to send a message to all connected clients.
Syntax: @all <message>

Sending Messages to Specific Clients
You can also send messages to specific clients by mentioning their names after the "@" symbol. If you use "@" without any specific command, the client will interpret it as the receiving client of the message.

For example: \
@john Hello, how are you?: Sends the message "Hello, how are you?" to the client named "john". \
@alice @bob @charlie Let's have a meeting at 3 PM: Sends the message "Let's have a meeting at 3 PM" to the clients named "alice", "bob", and "charlie". \

You can execute multiple commands in a single line. The commands will be executed in the order they appear. \

For example: \
@signin @all Welcome, everyone! @signout : Signs in, sends a message to all clients, and then signs out.
