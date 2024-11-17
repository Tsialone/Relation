package relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exception.*;

public class Individu {
    Attribut[] attributs;
    Object[] valeurs = new Object[0];

    public Individu(Attribut[] attributs) {
        setAttributs(attributs);
        initializValeurs();
    }
    public void initializValeurs  ()
    {
        List<Object> val = new ArrayList<>(Arrays.asList(valeurs));
        for (Attribut attribut : attributs) {
            val.add(attribut.getValeur());
        }
        setValeurs(val.toArray(new Object[0]));
        
    }
    public void setValeurs(Object[] valeurs) {
        this.valeurs = valeurs;
    }

    public void setAttributs(Attribut[] attributs) {
        this.attributs = attributs;
    }
    public Attribut[] getAttributs() {
        return attributs;
    }

    public Object[] getValeurs() {
        return valeurs;
    }

    public boolean equals(Individu individu) {
        
        Object[] individu1objet = getValeurs();
        Object[] individu2objet = individu.getValeurs();
        String compareindividu1 ="";
        String compareindividu2 ="";
        for (int i = 0; i < individu1objet.length; i++) {
             compareindividu1 += individu1objet[i].toString();
            
        }
        for (int i = 0; i < individu2objet.length; i++) {
            compareindividu2 += individu2objet[i].toString();
            
        }
        if (!compareindividu1.equals(compareindividu2)) {
            return false;
        }
        return true;
    }

}
