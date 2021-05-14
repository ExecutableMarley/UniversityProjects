Aufgabe 1 (\*)\
Verwenden Sie Lambdas und Streams zur Implementierung der nachfolgenden Methoden:\
* public static double average(List<Integer> list), die das arithmetische Mittel aller geraden Integer-Werte der ubergebenen Liste zurückgibt.
* public static List<String> upperCase(List<String> list), die alle mit einem
a (case insensitive) beginnende oder drei Zeichen lange Strings in der Liste zu deren
Großschreibung umwandelt.\
* public static String getString(List<Integer> list), die einen Komma separierten String der in der Liste enthaltenen Integer Werte zuruckgibt. Dabei soll
vor dem entsprechenden Integer Wert kodiert werden, ob der Wert gerade g oder ungerade u ist: Eingabe Liste = (3, 44), Ausgabe: "u3,g44".
  
Implementieren Sie alle obigen Methoden in einer Klasse ListUtils, von der weder abgeleitet noch von außerhalb der Klasse eine Instanz erzeugt werden kann.

Aufgabe 2 (\*\*)\
Als n-Gramme einer gegebenen Zeichenkette str bezeichnen wir im Kontext dieser Übung die Menge aller Teilworte von str mit der Länge n.
Beispiel: Betrachtete Zeichenkette "Hallo Trier "
→ 5-Gramme sind: {"Hallo", "allo ", "llo T", "lo Tr", "o Tri", " Trie", "Trier"}.
Nutzen Sie Lambdas und Streams zur Implementierung einer Methode, die von einem als Parameter ubergebenen String n-Gramme berechnet. Die Länge n der n-Gramme
wird dabei als weiterer Parameter ubergeben. Reagieren Sie entsprechend darauf, falls es
nicht möglich sein sollte n-Gramme eines Strings zu berechnen. Bevor ein String verarbeitet wird, sollte er zudem normalisiert werden. Die Normalisierung sieht vor, dass alle
Whitespaces rechts und links getrimmt werden sowie alle Whitespaces innerhalb des verbleibenden Strings durch ein einfaches Leerzeichen ersetzt werden.
Nutzen Sie weiterhin Lambdas und Streams zur Implementierung einer Methode, die eine
Liste von beliebigen Strings ubergeben bekommt, um von diesen Strings alle n-Gramme
zu berechnen. Vereinigen Sie innerhalb dieser Methode alle n-Gramme in einer Map, die für
jedes einzelne n-Gramm die absolute Anzahl ihrer Vorkommen speichert und geben Sie die
Map auf der Konsole aus.


Aufgabe 3 (\*\*\*)\
Betrachten Sie den in Moodle verfugbaren Programmcode FP S21 ueb04.zip. (Not inside this repo)
* Implementieren Sie eine Methode print(IteratorStrategy strategy) in der Klasse DataStructure. Rufen Sie diese Methode mit einer Instanz der Klasse EvenIteratorStrategy auf, sodass die selbe Funktionalität wie die der Methode printEven
ausgefuhrt wird.
* Rufen Sie die Methode print(IteratorStrategy strategy) so auf, dass die ungeraden Index-Werte ausgegeben werden. Nutzen Sie eine anonyme Klasse als Parameter der Methode, die als Instanz des Interfaces IteratorStrategy dient.
* Implementieren Sie eine Methode print(java.util.Function<Integer, Boolean>
iterator) in der Klasse DataStructure mit der selben Funktionalit¨at wie die der
Methode print(IteratorStrategy strategy). Rufen Sie die neu implementierte
Methode zweimal mithilfe eines Lambda Ausdrucks auf, sodass alle geraden IndexWerte beim ersten Aufruf und alle ungeraden Index-Werte beim zweiten Aufruf ausgegeben werden.
* Definieren Sie zwei Methoden, sodass die folgenden Statements die geraden bzw.
ungeraden Index Werte ausgeben:
```java
DataStructure ds = new DataStructure();
// ...
ds.print(DataStructure::isEvenIndex);
ds.print(DataStructure::isOddIndex);
```
