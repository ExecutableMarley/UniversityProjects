O3 Aufgabe D.A1

Implementieren Sie die Methode entwirre(Object o) so, dass sie per Reflection auf die Werte der Felder des Objekts zugreift.

Die Methode soll mit dem Feld mit dem Namen "start" beginnen, dessen Wert vom Typ Paar auslesen und das darin gespeicherte Zeichen nach System.out ausgeben.
Dann soll die Methode als nächstes Feld, das Feld auslesen, dessen Name im zweiten Wert des Paares gespeichert ist.
Die Methode soll den Verweisen so lange nachlaufen, bis der zweite Wert des Paares null ist.
In Ihrem Programmkode dürfen die Namen der Felder (Objektvariablen) "a1" bis "g5" nicht explizit vorkommen, sondern müssen per Reflection ermittelt werden.
Das Objekt darf auch nicht in ein Objekt vom Typ Secret umgewandelt (cast) werden.
Verwenden Sie eine Schleife (oder Rekursion).
Entwirrt sollte am Ende auf dem Bildschirm ein Zitat von Mark Twain erscheinen.

```java 
class Paar {
    Character zeichen;
    String naechstesFeld;
    Paar(Character z, String s) { zeichen=z; naechstesFeld=s; }
}
public class Secret {
    // In den folgenden Variablen steht das verwirrte Zitat von Mark Twain.
    public Paar start = new Paar('M', "a3");
    public Paar a1 = new Paar('n', "a2");
    public Paar a2 = new Paar(' ', "a4");
    public Paar a3 = new Paar('a', "a1");
    public Paar a4= new Paar('m',"a5");
    public Paar a5 = new Paar('u', "a7");
    public Paar a6 = new Paar('s', "a8");
    public Paar a7 = new Paar('s', "a6");
    public Paar a8 = new Paar(' ', "a9");
    public Paar a9= new Paar('d',"b3");
    public Paar b1 = new Paar('e', "b2");
    public Paar b2 = new Paar(' ', "b4");
    public Paar b3 = new Paar('i', "b1");
    public Paar b4= new Paar('T',"b5");
    public Paar b5 = new Paar('a', "b7");
    public Paar b6 = new Paar('s', "b8");
    public Paar b7 = new Paar('t', "b6");
    public Paar b8 = new Paar('a', "b9");
    public Paar b9= new Paar('c',"c3");
    public Paar c1 = new Paar('e', "c2");
    public Paar c2 = new Paar('n', "c4");
    public Paar c3 = new Paar('h', "c1");
    public Paar c4= new Paar(' ',"c5");
    public Paar c5 = new Paar('k', "c7");
    public Paar c6 = new Paar('n', "c8");
    public Paar c7 = new Paar('e', "c6");
    public Paar c8 = new Paar('n', "c9");
    public Paar c9= new Paar('e',"d3");
    public Paar d1 = new Paar(',', "d2");
    public Paar d2 = new Paar(' ', "d4");
    public Paar d3 = new Paar('n', "d1");
    public Paar d4= new Paar('b',"d5");
    public Paar d5 = new Paar('e', "d7");
    public Paar d6 = new Paar('o', "d8");
    public Paar d7 = new Paar('v', "d6");
    public Paar d8 = new Paar('r', "d9");
    public Paar d9= new Paar(' ',"e3");
    public Paar e1 = new Paar('a', "e2");
    public Paar e2 = new Paar('n', "e4");
    public Paar e3 = new Paar('m', "e1");
    public Paar e4= new Paar(' ',"e5");
    public Paar e5 = new Paar('s', "e7");
    public Paar e6 = new Paar('e', "e8");
    public Paar e7 = new Paar('i', "e6");
    public Paar e8 = new Paar(' ', "e9");
    public Paar e9= new Paar('v',"f3");
    public Paar f1 = new Paar('r', "f2");
    public Paar f2 = new Paar('d', "f4");
    public Paar f3 = new Paar('e', "f1");
    public Paar f4= new Paar('r',"f5");
    public Paar f5 = new Paar('e', "f7");
    public Paar f6 = new Paar('e', "f8");
    public Paar f7 = new Paar('h', "f6");
    public Paar f8 = new Paar('n', "f9");
    public Paar f9= new Paar(' ',"g3");
    public Paar g1 = new Paar('a', "g2");
    public Paar g2 = new Paar('n', "g4");
    public Paar g3= new Paar('k',"g1");
    public Paar g4 = new Paar('n', "g5");
    public Paar g5 = new Paar('.', null);

    public static void main(String argv[]) {
        Object o = new Secret();
        entwirre(o);
    }

    static void entwirre(Object o) {
        String feldname="start";
        System.out.println("Hier sollen Sie das Zitat entwirren!");
        
    }

}
```

O3 Aufgabe D.A2

Die run() -Methode Klasse ArcFillThread füllt alle zehn Millisekunden einen weiteren Grad eines vorgegebenen Kreissegments. Des Weiteren speichert eine Instanz des Typs ArcFillThread jeweils einen Verweis auf eine andere Instanz eines ArcFillThreads, so dass eine zirkuläre einfach verkettete Liste gebildet wird. In der Klasse ArcFrame werden alle nötigen Instanzen von ArcFillThread erzeugt und die Verkettung hergestellt.

Wenn Sie das Programm ausführen, werden Sie beobachten, dass der dargestellte Kreis in drei (Anzahl veraenderbar) gleich große Kreissegmente geteilt wird. Jeweils ein eigener ArcFillThread füllt ein Kreissegment mit einer entsprechenden Farbe. Außerdem arbeiten diese drei Threads nebenläufig und unabhängig voneinander. 

Synchronisieren Sie die ArFillThreads mithilfe von Java-Monitoren so, dass zu jedem Zeitpunkt genau ein ArFillThread aktiv ist. Sobald der gerade aktive ArFillThread das Füllen seines Kreissegments abgeschlossen hat, soll dieser seinen Nachfolger benachrichtigen und selbst warten, bis er von seinem Vorgänger wieder benachrichtigt wird.

Tipp: Nutzen Sie dazu die zirkuläre einfache Verkettung unter den Instanzen der ArcFillThreads aus. Sorgen Sie dafür, dass alle ArcFillThreads zunächst auf ihre Vorgänger warten. Der initiale Anstoß zum Füllen muss von einem anderen Thread erfolgen.

ArcFrame.java

``` java
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

public class ArcFrame extends Application {
    int width = 300; // inital width
    int height = 300; // initial height
    int margin = 20;
    Canvas canvas;
    private ArcFillThread[] arcFillThreads;
    private int numberOfThreads = 3;

    public static void main(String[] args) { launch(args); }
  
    public void start(Stage primaryStage) {                                                      
        primaryStage.setTitle("Filling Arcs");
        BorderPane root = new BorderPane();
        canvas = new Canvas(width,height);
        root.setCenter(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        arcFillThreads = new ArcFillThread[numberOfThreads];

        int angle = (int) (360d / numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++)  {
            int startAngle = (int) (360d / numberOfThreads * i);
            int mod = i % 3;
            int r = 255 * (1 & (1 << mod));
            int g = 255 * ((2 & (1 << mod)) >> 1);
            int b = 255 * ((4 & (1 << mod)) >> 2);

            Color color = new Color(r/255.0, g/255.0, b/255.0, 1.0);

            ArcFillThread current = new ArcFillThread(this, color, startAngle, angle);

            arcFillThreads[i] = current;

            if (i < 1) continue;

            // Registering a circular single linked list, i.e. a thread knows its successor.
            ArcFillThread predecessor = arcFillThreads[i - 1];
            if (predecessor != null) {
                // In particular, the successor of the predecessor thread is the current one.
                predecessor.successor = current;
            }
        }

        // The successor of the last one in the array will be the first one.
        ArcFillThread successor = arcFillThreads[0];
        if (successor != null) {
            arcFillThreads[numberOfThreads - 1].successor = successor;
        }
        
        // start animation timer and all threads
        final AnimationTimer timer = new AnimationTimer() {
        public void handle(long timestamp) { 
          for (int i = 0; i < numberOfThreads; i++) arcFillThreads[i].draw(); } };
        timer.start(); 
        for (int i = 0; i < numberOfThreads; i++) arcFillThreads[i].start();
        // we might want to wait until all threads are really started before
        // we access their state
        try { Thread.sleep(10); } catch(InterruptedException e){}
    }

}
``` 

ArcFillThread.java

``` java
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

class ArcFillThread extends Thread {
    ArcFillThread successor;
    private Color color;
    private ArcFrame frame;
    private int startAngle;
    private int angle;
    private int angleToFill=0;
    private boolean fillNotClear=true;
  
    ArcFillThread(ArcFrame frame, Color color, int startAngle, int angle) {
        this.frame = frame;
        this.color = color;
        this.startAngle = startAngle;
        this.angle = angle;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try { while (angleToFill <= angle) {
                    angleToFill++;
                    Thread.sleep(10);
                    }
                  fillNotClear=!fillNotClear;
                  angleToFill=0;
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
  
  public void draw() {
        GraphicsContext gc = frame.canvas.getGraphicsContext2D();
        gc.setFill(fillNotClear ? color : Color.WHITE);
        int w=(int) gc.getCanvas().getWidth();
        int h=(int) gc.getCanvas().getHeight();
        gc.fillArc(frame.margin, frame.margin,
                w - 2 * frame.margin,
                h - 2 * frame.margin,
                startAngle,
                angleToFill,
                ArcType.ROUND);
   }  
}
```
