Aufgabe 1\
Der Programmcode dieser Aufgabe befindet sich im Package textart. Das Programm liest
eine Datei ein, in der ein Bild gespeichert ist, welches aus einzelnen Symbolen besteht. Dieses Bild wird von mehreren der sogenannten ArtistThreads mithilfe der Klasse Printer
auf einem PrintStream ausgegeben. Wenn Sie die main-Methode starten, werden Sie sehen,
dass das Bild nicht korrekt wiedergegeben wird.
1. (\*) Legen Sie eine Klasse TextArtReader an, die von der Klasse java.lang.Thread
erbt. Uberschreiben Sie die run()-Methode so, dass eine Datei aus dem übergebenen
Dateinamen eingelesen und ein TextArt-Objekt erstellt wird. Weiterhin stellt
ein TextArtReader eine Getter-Methode fur das TextArt-Objekt zur Verfugung. 
Ersetzen Sie in der main-Methode das Erzeugen des TextArt-Objektes durch einen
TextArtReader. Sorgen Sie dafur, dass jeder ArtistThread zunächst pruft, ob der
TextArtReader das Erzeugen des TextArt-Objektes abgeschlossen hat, daraufhin das
TextArt-Objekt vom TextArtReader bezieht und dann mit dem Drucken beginnt.
Verzichten Sie dabei auf statische Variablen (Klassenvariablen).
Hinweis: Wenn Sie alle Anderungen umgesetzt haben und die gleiche fehlerhafte
Ausgabe sehen, wie zuvor, haben Sie diesen Aufgabenteil erfolgreich bearbeitet.
2. (\*\*) Das Bild wird nicht korrekt ausgegeben. Ein Grund dafur ist die Reihenfolge 
der Ausgabe der einzelnen Symbole (Character). Passen Sie die Methode print der
Klasse Printer so an, dass die Symbole des Bildes in der korrekten Reihenfolge
ausgegeben werden. Lassen Sie das Printer-Objekt daruber buchführen, welches 
Zeichen als nächstes gedruckt werden muss und nutzen Sie das Konzept von Java
Monitoren und die Methoden wait() bzw. wait(long timeOut) und notify() bzw.
notifyAll(), um die ArtistThreads zu koordinieren. W¨ahlen Sie insbesondere ein
geeignetes Objekt, welches Sie als Java Monitor gebrauchen.
Hinweis: Wenn Sie das Bild in der korrekten Reihenfolge gedruckt sehen und das
Programm dennoch nicht terminiert oder das Drucken des Bildes zum Stillstand
kommt, haben Sie diesen Aufgabenteil erfolgreich bearbeitet. Bemerkung: In seltenen
F¨allen kann es dennoch vorkommen, dass sich das Programm terminiert. Fuhren Sie ¨
das Programm mehrfach aus und beobachten Sie das Verhalten.
3. (\*\*) Die Termination des Programms hängt davon ab, dass sich alle ArtistThreads
erfolgreich beenden. Die einzige M¨oglichkeit fur einen ArtistThread aus seiner Schleife auszubrechen ist, wenn kein weiteres Symbol zum Drucken mehr vorhanden ist oder
von außen ein Interrupt signalisiert wird. Durch einen bestehenden Data-Race Bug
bekommen manche ArtistThreads es nicht rechtzeitig mit, dass bereits jedes Symbol
des Bildes gedruckt wurde bzw. ein Symbol ubersprungen wird. Dadurch verharren
Sie unendlich in der Waiting-Queue des Monitors, der in der print-Methode verwendet wurde (sofern Sie die bisherigen Aufgaben nach unseren Vorgaben bearbeitet
haben). Versuchen Sie das Problem nachzuvollziehen und beheben Sie den Ursache
des Data-Race Bugs mithilfe eines dedizierten Java Monitors.
Hinweis: Untersuchen Sie die Methoden der Klasse TextArt.
Vielen Dank an Moritz Kunzl f ¨ ur die Inspiration zu dieser Aufgabenstellung.

Aufgabe 2 (\*\*)\
Der Programmcode dieser Aufgabe befindet sich im Package matrix. Die Klasse MatrixVectorSum fuhrt für eine Matrix M und einen Vektor v die Multiplikation M · v = c
durch und gibt die Summe der Elemente des Ergebnisvektors c zuruck.
Führen Sie das gegebene Programm ein paar mal aus. An der Ausgabe sollten Sie feststellen, 
dass sich das Programm nicht deterministisch verhält: Die Ausgabe der Berechnung
der multi-threaded Version gibt mit jeder Ausfuhrung bei gleicher Eingabe unterschiedliche
falsche Werte aus oder terminiert erst gar nicht.
Identifizieren Sie die kritischen Bereiche und beheben Sie die Synchronisations-Bugs mithilfe von Java-Monitoren.
1. Stellen Sie sicher, dass bei der Berechnung der Summe keine Data-Races auftreten
können.
2. Sorgen Sie dafür, dass der Thread, der die Methode computeMatrixVectorSum aufruft, mithilfe von Synchronisation mittels eines Java-Monitors wartet, bis die Berechnung der Summe aller SumThreads abgeschlossen ist.
