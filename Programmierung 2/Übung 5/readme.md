Aufgabe 1 (\*)\
Erstellen Sie in der Klasse A1 folgende Methode:
public static void printTestMethods(Object o)
Diese Methode erh¨alt ein Objekt und gibt mit Hilfe der Reflection API alle Methodensignaturen der nicht-statischen Methoden dieses Objekts auf der Konsole aus, deren Namen
den String ”test” enthalten (case insensitive). Fur ein Objekt der Klasse A1 sollte diese
Methode beispielsweise folgende Signaturen ausgeben:
````java
public boolean de.unitrier.st.fp.s21.ueb05.A1.testIfEven(int)
public boolean de.unitrier.st.fp.s21.ueb05.A1.isNullTestMethod(java.lang.Object)
````
Die Signatur public boolean A1.isOdd(int) sollte hingegen nicht ausgegeben werden (obwohl ein Parameter den Namen test hat). Die eigene Signatur sollte die Methode
ebenfalls nicht ausgeben, da die Methode statisch ist.


Aufgabe 2 (\*\*)\
Erstellen Sie in der Klasse A2 folgende Methode:
public static String commonAncestor(Object a, Object b)
Diese Methode erhält zwei Objekte und liefert mit Hilfe der Reflection API den Namen der
speziellsten gemeinsamen Oberklasse zuruck. Erstellen Sie zum Testen Ihrer Lösung einige einfache Beispielklassen oder verwenden Sie Klassen aus der Java Standardbibliothek.
Wenn Sie der Methode beispielsweise eine Objekt der Klasse LinkedList und ein Objekt
der Klasse HashSet ubergeben, sollte sie java.util.AbstractCollection als gemeinsame
Oberklasse zurückliefern.

Aufgabe 3 (\*\*\*)\
Betrachten Sie die Klasse Main. Diese enthält Methoden zum Testen, ob zwei Funktionen mit den selben Eingaben das gleiche Ergebnis liefern. In der main-Methode ist ein beispielhafter Aufruf einer dieser Methoden zu sehen. Die Klasse ObfuscatedUtil liegt nur als .class-Datei vor, d.h. lediglich der Java-Bytecode dieser Klasse steht zur Verfugung (siehe
Ordner obfuscated). Finden Sie nun mithilfe der Reflection API eine Methode in der Klasse ObfuscatedUtil, die zwei Parameter vom Typ String erhält und bei der Eingabe von
’Hello’ und ’ World’ die Zeichenkette ’Hello World’ zuruckliefert. Dementsprechend wird nach einer Methode mit der selben Semantik, wie die der String Methode concat gesucht.
