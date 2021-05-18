O2 Aufgabe D.A1

Implementieren Sie die Methode liesZeichenliste(), die den Text aus der Datei buchstabensalat zeichenweise einliest 
und jedes Zeichen zusammen mit seiner Position in einer geeigneten Map<Integer,Character> abspeichert.

```` java
import java.util.Map;


interface Test { boolean test(Map.Entry<Integer,Character> p); }

public class A1 {

    public static void main(String argv[]) {
        Map<Integer,Character> zeichenliste=liesZeichenliste();
        druckeZeichen(zeichenliste, (Map.Entry<Integer,Character> p)->p.getKey()%2==1);
        druckeZeichen(zeichenliste, (Map.Entry<Integer,Character> p)->p.getKey()%2==0);
    }

   
    static void druckeZeichen(Map<Integer,Character> zeichenliste, Test pred) {
        for (Map.Entry<Integer,Character> p : zeichenliste.entrySet()) {
           if (pred.test(p)) System.out.print(p.getValue());
        }
        System.out.println();
    }

  

    static Map<Integer,Character> liesZeichenliste() {
        Map<Integer, Character> map = null;
    
        return map;
    }

}
````

O2 Aufgabe D.A2

Erweitern Sie das Programm aus Aufgabe A1 so, dass die Methode druckeZeichen(Map<Integer,Character> zeichenliste, Test pred) 
statt einer for-Schleife einen funktionalen Strom verwendet, d.h. die Funktionalität der gesamten for-Schleife lässt sich 
mit Hilfe funktionaler Ströme in einer einzigen Zeile implementieren.

```` java
import java.util.Map;


interface Test { boolean test(Map.Entry<Integer,Character> p); }

public class A2 {

    public static void main(String argv[]) {
        Map<Integer,Character> zeichenliste=liesZeichenliste();
        druckeZeichen(zeichenliste, (Map.Entry<Integer,Character> p)->p.getKey()%2==1);
        druckeZeichen(zeichenliste, (Map.Entry<Integer,Character> p)->p.getKey()%2==0);
    }

   
    static void druckeZeichen(Map<Integer,Character> zeichenliste, Test pred) {
        for (Map.Entry<Integer,Character> p : zeichenliste.entrySet()) {
           if (pred.test(p)) System.out.print(p.getValue());
        }
        System.out.println();
    }

  

    static Map<Integer,Character> liesZeichenliste() {
        Map<Integer, Character> map = null;
    
        return map;
    }

}
````
