Aufgabe 1\
Implementieren Sie die Methode NDArray reshape(int... args), die ein neu erzeugtes
NDArray zurückliefert, das auf die Daten des ursprünglichen Arrays verweist, aber die 
ubergebenen Dimensions args und neu berechnete Schrittweiten strides verwendet. Die
Bedeutung der Schrittweiten wird in der folgenden Abbildung illustriert. Falls die Anzahl
der Einträge in den Daten nicht mit den Dimensionen übereinstimmt, soll eine Fehlermeldung erfolgen. 
Hinweis: Ein mehrdimensionales NDArray mit den Dimension (n1,...,nk) hat
genau n1 ∗ · · · ∗ nk viele Einträge.


Aufgabe 2\
Mit der Funktion np.zeros(n) können Sie ein mit n Nullen gefülltes Array erzeugen.
Verwenden Sie reshape() und Slicing, um das Array so umzuformen und mit Werten zu
füllen, dass ein Gitter entsteht mit je einem Punkt in jeder Gitterzelle (siehe Abbildung). 

![image](https://user-images.githubusercontent.com/47697856/124141521-c5de7f80-da89-11eb-82f0-6b7865513c19.png)
