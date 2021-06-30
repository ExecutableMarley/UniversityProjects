Aufgabe 1 (\*\*)\
Client: Sobald der Client gestartet ist, soll sich dieser automatisch mit dem Server
verbinden. Erweitern Sie die connect-Methode des ChatWindows so, dass eine Verbindung
zum Server herstellt wird und anschließend eine Client ID empfangen wird (Handshake).
Erweitern Sie dann die Methode sendMessage, sodass Nachrichten (Message) an den Server
geschickt werden können. Die Methode run (zum Empfangen von Nachrichten) soll anschließend so erweitert werden, dass Nachrichten vom Server empfangen und im Chatfenster
dargestellt werden können (displayReceivedMessage).

Aufgabe 2 (\*\*)\
Server: Implementieren Sie nun Ihren eigenen Server. Dieser wird separat gestartet und
hat keine GUI (headless). Er wartet nach dem Start in einer Schleife auf anfragende Clients
und erstellt für jeden Client einen Client-Thread. Die Client-Threads werden in einer Liste
clientThreads verwaltet. Diese Liste wird benötigt, um die Nachrichten zwischen den
Clients synchronisieren zu können. Kommt eine Nachricht bei dem Server an, soll diese
an alle verbundenen Clients verschickt werden. Zur Kommunikation zwischen Server und
Client sollen serialisierte Objekte zum Einsatz kommen. Verbindet sich ein Client neu
mit einem Server, wird zunächst eine eindeutige ID erzeugt und an den Client geschickt
(Handshake). Erst danach können Nachrichten empfangen werden (Message).

Aufgabe 3 (\*\*)\
Bei einem Neustart des Clients werden bisher keine alten Nachrichten angezeigt. Erweitern Sie Ihren Client und Server nun so, dass der Server bei einem Handshake (initialen
Verbinden des Clients) eine Liste aller bisherigen Nachrichten mit verschickt und diese
vom Client angezeigt werden.

Aufgabe 4 (\*\*\*)\
Andern Sie das Empfangen und das Senden von Nachrichten so ab, dass das zu empfangende 
Objekt dem Server bzw. Client nicht mehr bekannt ist (z.B. durch Entfernen der
Dateien Handshake.java und Message.java). Dadurch können die empfangen Objekte nicht
mehr in ihre ursprüngliche Form gecastet werden. Verwenden Sie daher Reflection, um an
die enthaltenen Daten zu gelangen.
