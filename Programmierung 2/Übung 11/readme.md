Aufgabe 1 (\*)\
Currying ist “die Umwandlung einer Funktion mit mehreren Argumenten in eine Funktion
mit einem Argument” (https://de.wikipedia.org/wiki/Currying).
Implementieren Sie in Python 3 zwei Funktionen, die jeweils einen Parameter n übergeben bekommen und eine Funktion zuruckgeben,
die ebenfalls einen Parameter erhält, zu dem dann n addiert wird. Die erste Funktion soll einen Lambda-Ausdruck verwenden, die zweite nicht (siehe Code).\


Aufgabe 2 (\*\*)\
Implementieren Sie die folgenden drei Funktionen (siehe Code):
1. Eine Funktion nth(gen, n), die das n-t nächste Element eines Generator gen zuruckliefert.
2. Eine Funktion example gen(), die einen Generator zuruckliefert, der Tupel der Form(x, 2x) mit x ∈ [0, 99] erzeugt.
3. Eine Funktion example list(), die eine Liste zuruckliefert, die alle Tupel der Form(x, 2x) mit x ∈ [0, 99] enthält.

Fuhren Sie den vorgegebenen Code aus und erläutern Sie kurz Ihre Beobachtungen.\


Aufgabe 3 (\*)\
Implementieren Sie einen Python Dekorator, welcher fur alle Argumente einer beliebigen
Funktion den Namen mit dem dazugehörigen Wert ausgibt. Dabei sollen sowohl positionelle, als auch keyword Argumente beachtet werden.
Hinweis: Mit Hilfe der Funktion function.__code__.co_varnames kann der Name von
Variablen, beginnend mit den Funktionsargumenten, ausgelesen werden. Die Funktion
function.__code__.co_argcount liefert die Anzahl an Funktionsargumenten.
