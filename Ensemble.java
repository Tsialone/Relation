import java.util.ArrayList;
import java.util.Arrays;

public class Ensemble {

    Object[] elements;
    String nom;
    Domaine domaine;
    int taille;

    public Ensemble(String nom, Object[] elements, Domaine domaine) throws Myexception {

        setNom(nom);
        setDomaine(domaine);
        setTaille(elements.length);
        setElements(elements);
        // removeDuplicate();
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public void setNom(String nom) throws Myexception {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public int getTaille() {
        return taille;
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

    public boolean appartient(Object objet, Ensemble ensemble) {
        for (Object object : elements) {
            if (object.equals(objet)) {
                return true;
            }
        }
        return false;
    }

    public void setElements(Object[] elements) throws Myexception {
        Integer globallimit = getDomaine().getLimit();
        Integer decimallimit = getDomaine().getDecimallimit();
        for (int i = 0; i < elements.length; i++) {
            String type = Domaine.quelType(elements[i]);

            // if (!Domaine.quelType(elements[i]).equalsIgnoreCase(getDomaine().getType())) {
            //     throw new Myexception(
            //             "L'element " + elements[i] + " ne correspond pas au domaine  " + getDomaine().getType());
            // }
            if ((type.equalsIgnoreCase("decimal"))) {

                String d = String.valueOf(elements[i]);
                String[] splited = d.split("\\.");

                if (decimallimit != null && decimallimit != 0) {
                    if (splited[0].length() > globallimit) {
                        throw new Myexception("Erreur de precision entier sur " + elements[i]);
                    }
                    if (splited[1].length() > decimallimit) {
                        throw new Myexception("Erreur de precision flottante sur " + elements[i]);

                    }
                } else {
                    if (globallimit != null && splited[0].length() > globallimit) {
                        throw new Myexception("Erreur de precision entier sur " + elements[i]);
                    }
                }

            }
            if (type.equalsIgnoreCase("varchar") || type.equalsIgnoreCase("number")) {
                if (globallimit != null) {
                    // System.out.println(globallimit);
                    if (elements[i].toString().length() > globallimit) {

                        throw new Myexception("Erreur de precision " + elements[i]);
                    }
                }

            }
            for (int j = i + 1; j < elements.length-1; j++) {
                if (!Domaine.quelType(elements[i]).equalsIgnoreCase(Domaine.quelType(elements[j]))) {
                    throw new Myexception(
                            "Les elements ne sont pas dans le meme domaine sur " + elements[i] + " et " + elements[j]);
                }

            }
        }
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
        Ensemble newensemble = new Ensemble(getNom(), nob.toArray(Object[]::new), getDomaine());
        return newensemble;
    }

    public Ensemble union(Ensemble e2) throws Myexception {
        Object[] e1ob = this.getElements();
        Object[] e2ob = e2.getElements();
        ArrayList<Object> neo = new ArrayList<>();

        neo.addAll(Arrays.asList(e1ob));
        neo.addAll(Arrays.asList(e2ob));
        Ensemble nEnsemble = new Ensemble(null, neo.toArray(Object[]::new), getDomaine());
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

        Ensemble nEnsemble = new Ensemble(getNom(), neo.toArray(Object[]::new), getDomaine());
        return nEnsemble;
    }

}