Aufgabe 1 (\*)\
Erstellen Sie mithilfe von JavaFX eine GUI (Figure 1) für ein Chatfenster. Das Fenster
besteht aus einem Pane zur Darstellung von Nachrichten (inkl. Scrollbar), einem Textfeld
zur Eingabe von Nachrichten, einem Button zum Versenden der eingegebenen Nachrichten
und einem Menü mit der Möglichkeit zum Speichern und Wiederherstellen des Chats.
Zusätzlich soll es möglich sein, beliebig viele Instanzen von Fenstern zu öffnen.

Aufgabe 2 (\*\*)\
Implementieren Sie die Funktion zum zeitgleichen Versenden von Nachrichten an alle Chatfenster (Gruppenchat). Dabei hat jedes Chatfenster eine eindeutige ID und jede Nachricht
enthält die ID des jeweiligen Absenders. Zusätzlich sollte es einen zentralen Manager
geben, welcher alle Nachrichten verwaltet, empfängt und an die jeweiligen Chatfenster
weiterleitet. Ahnlich wie in den bekannten Chat-Applikationen, soll eine selbst verfasste
Nachricht rechtsbündig sein und alle anderen Nachrichten linksbündig. Zudem soll auch
farblich unterschieden werden können, ob es sich bei einer Nachricht um eine eigene oder
fremde handelt. Zusätzlich wird in jeder fremden Nachricht die ID des Absenders angezeigt.
Die eigene Nachricht im entsprechenden Fenster soll die eigene ID nicht anzeigen. Die Aufgabe soll ohne Netzwerkfunktionalität implementiert werden.

Aufgabe 3 (\*)\
Implementieren Sie nun die Funktionen Backup und Restore. Bei einem Klick auf Backup
werden alle Nachrichten in eine Datei gespeichert. Entsprechend werden alle Nachrichten
bei einem Klick auf Restore wiederhergestellt und vor den aktuellen Nachrichten (falls
vorhanden) wieder hinzugefügt. Dabei ist es Ihnen überlassen, ob Sie sich ein eigenes
Format zum Speichern und Wiederherstellen überlegen möchten oder ob Sie Serialisierung
nutzen.
