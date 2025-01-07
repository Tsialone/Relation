package relation;

import java.util.ArrayList;
import java.util.Arrays;

import exception.*;

public class Ensemble {

    Object[] elements;
    String nom;
    int limite;
    int taille;

    public Ensemble(String nom, Object[] elements) throws Myexception {

        setNom(nom);
        setElements(elements);
        removeDuplicate();
    }

    
   public int  getTaille ()
    {
            return elements.length;
    }


    public void setNom(String nom) throws Myexception {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

   

    public Object[] getElements() {
        return elements;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public void afficheElements() {
        for (Object object : elements) {
            if (object instanceof Integer integer) {
                System.out.println("Integer: " + integer);
            } else if (object instanceof Double aDouble) {
                System.out.println("Double: " + aDouble);
            } else if (object == null) {
                System.out.println("Null value encountered");
            } else if (object instanceof String string) {
                System.out.println("String: " + string);
            }
        }
    }

    public int card() {
        return taille;
    }

    public boolean appartient(Object objet) {
        for (Object object : elements) {
            if (object.equals(objet)) {
                return true;
            }
        }
        return false;
    }

    public void setElements(Object[] elements) throws Myexception {
        
        this.elements = elements;
    }

    public void removeDuplicate() throws Myexception {
        Object[] obj = this.getElements();
        ArrayList<Object> neo = new ArrayList<>(Arrays.asList(obj));
        for (int i = 0; i < neo.size(); i++) {
            for (int j = i + 1; j < neo.size(); j++) {
                if (neo.get(i).equals(neo.get(j))) {
                    neo.remove(j);
                    i = 0;
                    j = 0;
                }
            }
        }

        this.setElements(neo.toArray(Object[]::new));
    }

    public Ensemble intersection(Ensemble e2) throws Myexception {
        Object[] e1ob = this.getElements();
        Object[] e2ob = e2.getElements();
        ArrayList<Object> nob = new ArrayList<>();
        for (Object e1ob1 : e1ob) {
            for (Object e2ob1 : e2ob) {
                if (e1ob1.equals(e2ob1)) {
                    nob.add(e1ob1);
                    break;
                }
            }
        }
        Ensemble newensemble = new Ensemble(getNom(), nob.toArray(Object[]::new));
        return newensemble;
    }

    public Ensemble union(Ensemble e2) throws Myexception {
        Object[] e1ob = this.getElements();
        Object[] e2ob = e2.getElements();
        ArrayList<Object> neo = new ArrayList<>();

        neo.addAll(Arrays.asList(e1ob));
        neo.addAll(Arrays.asList(e2ob));
        Ensemble nEnsemble = new Ensemble(null, neo.toArray(Object[]::new));
        return nEnsemble;
    }

    public Ensemble difference(Ensemble e2) throws Myexception {
        Object[] e1ob = this.getElements();
        Object[] e2ob = this.intersection(e2).getElements();

        ArrayList<Object> neo = new ArrayList<>(Arrays.asList(e1ob));

        for (int i = 0; i < e2ob.length; i++) {
            for (int j = 0; j < neo.size(); j++) {
                if (e2ob[i].equals(neo.get(j))) {
                    neo.remove(j);
                    i = 0;
                    j = 0;
                }
            }
        }

        Ensemble nEnsemble = new Ensemble(getNom(), neo.toArray(Object[]::new));
        return nEnsemble;
    }

}