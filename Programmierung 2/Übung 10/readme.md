Aufgabe 1 (\*)\
Ein Palindrom ist eine Zeichenkette, die “vorwärts wir ruckwärts gelesen identisch ist”
(https://de.wikipedia.org/wiki/Palindrom). Beispiele sind: “neben”, “otto”, und “rentner”. Implementieren Sie in Python 3 zwei Funktionen, die uberprüfen, ob ein String ein 
Palindrom ist. Die erste Funktion soll Rekursion verwenden, die zweite nicht. Implementieren Sie außerdem eine For-Schleife, die die Palindrom-Funktionen testet.

Aufgabe 2 (\*\*)\
Es seien folgende Listen gegeben:
* breads = ["Weissbrot", "Vollkorn", "Dinkel", "Speckbrot"]
* patties = ["Wildschwein", "Rind", "Halloumi", "Aubergine"]
* souces = ["Kaese", "Knoblauch", "Curry"]
* toppings = ["Kopfsalat", "Bacon", "Tomate"]
 
Ein Burger wird durch ein 5-Tupel (bottom, patty, souce, topping, top) modelliert,
wobei bottom und top den unteren bzw. oberen Teil des Burgerbrötchens darstellt. Die
Auswahl des Burgerpattys bestimmt vorrangig, ob ein Burger nicht vegetarisch, vegetarisch
oder gar vegan sein wird. Dementsprechend sind alle Kombinationen mit Aubergine, die
Speckbrot, Kaese oder Bacon beinhalten sowie Kombinationen mit Halloumi, die Speckbrot
oder Bacon beinhalten unzulässig. Des Weiteren soll es nur Burger geben, deren obere
Brothälfte nicht gleich der unteren ist. Nutzen Sie List Comprehension, um die Anzahl der
möglichen Kombinationen von Burgern zu bestimmen. Dabei gilt es als die selbe Kombination, wenn die Sorte der oberen und unteren Brötchenhälfte lediglich vertauscht ist.
Beispiel:

("Dinkel", "Aubergine", "Curry", "Tomate", "Weissbrot")\
ist die selbe Kombination, wie:\
("Weissbrot", "Aubergine", "Curry", "Tomate", "Dinkel")\
Hinweis: Ergebnis = 138.


Aufgabe 3 (\*\*)\
Welche zwei Sätze verbergen sich hinter dem folgenden scheinbaren Buchstabensalat:\
DIenr diesmt Sdienrn eb eisstte HLielhfree rz,u rd eSre lsbiscthh
 inlafceh iumnmde rn aucnhs eürbee rofbleürssstieg Mmaaxcihmte..\
Hinweis: Verwenden Sie Slicing
