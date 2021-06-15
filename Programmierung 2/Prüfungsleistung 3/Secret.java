package com.company;

class Paar
{
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

    public static void main(String argv[])
    {
        Object o = new Secret();
        entwirre(o);
    }

    //Recursive approach
    static void recursion(Object o, String fieldName) throws IllegalAccessException, NoSuchFieldException
    {
        Paar cur = (Paar)o.getClass().getField(fieldName).get(o);

        System.out.print(cur.zeichen);

        //Check if there is more
        if (cur.naechstesFeld != null)
            recursion(o,cur.naechstesFeld);
    }

    static void entwirre(Object o)
    {
        try
        {
            //Loop approach
            for (Paar cur = (Paar)o.getClass().getField("start").get(o);;)
            {
                System.out.print(cur.zeichen);

                if (cur.naechstesFeld != null)
                    cur = (Paar)o.getClass().getField(cur.naechstesFeld).get(o);
                else
                    break;
            }
            System.out.println();

            //Recursive approach
            recursion(o, "start");

        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

}