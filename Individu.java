public class Individu {
    Attribut[] attributs;
    Object[] valeurs;

    public Individu(Attribut[] attributs, Object[] valeurs) {
        this.attributs = attributs;
        this.valeurs = valeurs;

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
