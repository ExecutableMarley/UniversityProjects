Aufgabe 1 (\*)      
Erweitern Sie die Implementierung der Klasse List (doppelt verkettete Liste) um eine
Methode, die alle in der Liste enthaltenen Werte in eine CSV-Datei schreibt bzw. anfügt.
Existiert noch keine Datei, so sollen die Werte der Liste mit Kommata getrennt in die erste
Zeile, ansonsten in eine neue Zeile, geschrieben werden. Lagern Sie das Exception-Handling
möglichst in aufrufende Methoden aus.


Aufgabe 2 (\*\*)\
Erweitern Sie die Implementierung der Klasse List um eine Methode, die alle in einer
CSV-Datei hinterlegten Fließkommazahlen einliest und an die Liste (this) anfügt. Jede
Zeile der CSV-Datei steht für eine Liste, die einzelnen Werte sind durch Kommata getrennt.
Es sollen zuerst alle Werte in der ersten Zeile angefügt werden, dann die Werte aus der
zweiten Zeile, usw. Am Ende soll die Anzahl der eingelesenen Zeilen auf der Konsole
ausgegeben werden. Fangen Sie möglicherweise auftretende Exceptions ab und geben Sie
eine Fehlermeldung auf der Konsole aus.


Aufgabe 3 (\*\*\*)\
Betrachten Sie Ihre Version des MineSweeperFX-Projekts aus dem vorherigen Aufgabenblatt. Erweitern Sie das Programm so, dass ein weiteres MenuItem mit der Aufschrift
”Save” der grafischen Bedienoberfläche hinzugefügt wird. Ein Maus-Klick auf dieses MenuItem (oder STRG+S) soll es ermäglichen das aktuelle Spielfeld in eine CSV-Datei zu
exportieren. Die Zeilen und Spalten der CSV-Datei sollen genau dem Spielfeld entsprechen.
Zellen mit dem Wert ”F” kodieren Felder, die keine Mine enthalten; Zellen mit dem Wert
”M” kodieren Felder, die eine Mine enthalten. Bei Fehlern während der Ausgabe sollte
Ihre Implementierung dies entsprechend mitteilen. Implementieren Sie eine Strategie, falls
die Datei bereits existiert.
