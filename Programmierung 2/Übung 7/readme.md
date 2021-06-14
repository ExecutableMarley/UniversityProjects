Aufgabe 1 (\*)\
Führen Sie das gegebene Programm mindestens einmal aus. Auf den ersten Blick erscheint alles korrekt zu arbeiten. Allerdings befindet sich noch ein Synchronisationsbug
im Programmcode, der nach außen hin nicht unbedingt sichtbar ist. Außerdem ist die
Aktualisierung der GUI-Komponenten nicht optimal implementiert.
Identifizieren Sie die kritischen Bereiche und synchronisieren sie diese entsprechend mit (impliziten) Java-Monitoren. Verbessern Sie die Aktualisierungslogik der GUI-Komponenten
so, dass die Methoden wait() und notify() bzw. notifyAll() verwendet werden.

Aufgabe 2 (\*)\
Fügen Sie dem Programm einen Button hinzu, mit dem Sie die Berechnung starten,
pausieren und, sofern die Berechnung einmal abgeschlossen ist, neustarten können. Lösen
Sie diese Aufgabe mithilfe von Synchronisation und verwenden Sie insbesondere die Methoden wait() und notify() bzw. notifyAll().

Aufgabe 3 (\*\*)\
Erweitern Sie den vorgegebenen Programmcode so, dass die Anzahl der zu verwendenden
Threads (Klasse PiTrialThread) mithilfe eines Spinner grafisch eingelesen werden kann.
Folglich sollen mehrere Threads das Experiment simultan durchführen.
Diese Anzahl sollte allerdings nur geändert werden dürfen, sofern gerade keine Berechnung
aktiv ist.
Insgesamt ist die Anzahl der maximal durchzuführenden Experimente pro Thread auf den
Integer-Wert Integer.MAX VALUE gesetzt. Die Threads sollten sich aber die Gesamtanzahl
der durchzuführenden Experiment-Durchführungen teilen.

Aufgabe 4 (\*\*)\
Um bei dem hier gegebenen Programm zur Berechnung von Pi die Experimente durchzuführen,
werden entsprechende Threads in Form einer eigenen Klasse verwendet. Schreiben Sie
dieses Programm so um, dass anstelle der expliziten Threads ein Thread-Pool zur Durchführung
der Experimente verwendet wird.
