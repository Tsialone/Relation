package relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Attribut {
    String nom;
    Domaine domaine;
    Object [] valeurs = new Object[0];
    Object valeur;
    public Attribut (String nom , Domaine domaine)
    {
        this.nom = nom;
        this.domaine = domaine;
        // if (domaine.getEnsemble() != null) {
        //     setValeurs(domaine.getEnsemble().getElements());
        // }
    }
    public Attribut (String nom )
    {
        this.nom = nom;
    }
    public void pushValeurs (Object [] valeur)
    {
        List<Object> insert = new ArrayList<>(Arrays.asList(valeur));
        List<Object> ini = new ArrayList<>(Arrays.asList(getValeurs()));
        ini.addAll(insert);
        setValeurs(ini.toArray(new Object[0]));
    }
    public Attribut (String nom , Object valeur)
    {
    setNom(nom);        
    setValeur(valeur);
       
    }
    public Object getValeur() {
        return valeur;
    }
    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setValeurs(Object[] valeurs) {
        this.valeurs = valeurs;
    }
    public Object[] getValeurs() {
        return valeurs;
    }
    public Domaine getDomaine() {
        return domaine;
    }
    public String getNom() {
        return nom;
    }
}
