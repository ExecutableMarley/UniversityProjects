Aufgabe A1\
Verfügbar von: Sunday, 27. April 2021, 16:15
Abgabetermin: Tuesday, 27. April 2021, 17:30
Erforderliche Dateien: A1.java (Herunterladen)
Maximale Anzahl an Dateien: 10
Arbeitstyp: Einzelarbeit
Nutzen Sie JavaFX, um ein Tic-Tac-Toe Feld anzuzeigen. Die Spielfelder sollen zufällig gewählt mit "o" oder "x" beschriftet sein. 

![image](https://user-images.githubusercontent.com/47697856/116279107-72993980-a787-11eb-850d-6896d47fb592.png)

A1.java:
```java
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class A1 extends Application {
  
  public void init() {   }
  
  public void start(Stage primaryStage) throws Exception 
    {
        GridPane root = new GridPane();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Aufgabe A1");
        primaryStage.setScene(scene);        
        primaryStage.show(); 
    }
  
  public static void main(String[] args){
     launch(args);
   }
}
```

Aufgabe A2\
Verfügbar von: Sunday, 4. April 2021, 17:59
Abgabetermin: Tuesday, 27. April 2021, 17:30
Erforderliche Dateien: A2.java (Herunterladen)
Maximale Anzahl an Dateien: 10
Arbeitstyp: Einzelarbeit
Erweitern Sie das Programm aus Aufgabe A1 so, dass der Benutzer damit Tic-Tac-Toe spielen kann. Anfangs sind alle Spielfelder unbeschriftet. Wenn der Nutzer auf ein noch unbeschriftetes Spielfeld klickt, wird dieses mit "o" oder "x" markiert. Dabei wird immer abwechselnd zwischen den beiden Zeichen umgeschaltet wie im folgenden Video verdeutlicht. 

A2.java:
```java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class A2 extends Application {
  
  public void init() {   }
  
  public void start(Stage primaryStage) throws Exception 
    {
        GridPane root = new GridPane();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Aufgabe A2");
        primaryStage.setScene(scene);        
        primaryStage.show(); 
    }
  
  public static void main(String[] args){
     launch(args);
   }
  
}
```
