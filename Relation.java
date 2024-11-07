import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Attr;

public class Relation {
    String nom;
    Attribut[] attributs;
    Individu[] individus = new Individu[0];
    String[] valableoperator = { "=", "!=", "<", "<=", ">=", ">", "<" };
    Condition condition;
    Ensemble[] ensembles;

    public Relation(String nom, Attribut[] attributs) throws Myexception {
        setNom(nom);
        setAttributs(attributs);
    }

    public void initializeEnsemble() throws Myexception {
        int indexattribut = getAttributs().length;
        ensembles = new Ensemble[indexattribut];
        Individu[] individus = getIndividus();
        for (int i = 0; i < indexattribut; i++) {
            Object[] elements = new Object[individus.length];
            for (int j = 0; j < individus.length; j++) {
                elements[j] = individus[j].getValeurs()[i];
            }
            if (elements.length != 0) {
                ensembles[i] = new Ensemble(getAttributs()[i].getNom(), elements,
                        new Domaine(Domaine.quelType(elements[0])));
            }

        }
    }

    public Ensemble[] getEnsembles() {
        return ensembles;
    }

    public boolean hisAttribut(Attribut attribut) {

        List<Attribut> attributs = Arrays.asList(getAttributs());
        if (attributs.contains(attribut)) {
            return true;
        }
        return false;
    }

    public void setCondition(Condition condition) throws Myexception {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void delIndividu(Individu ob) throws Myexception {
        
        int taille = getIndividus().length ;
        boolean found = false;
        Individu[] newtab = new Individu[taille];
        for (int i = 0; i < getIndividus().length; i++) {
            if (getIndividus()[i].equals(ob)) {
                getIndividus()[i] = null;
                found = true;
                break;
            }
        }
        if (found) {
            newtab = new Individu[taille-1];
        }
        int newi = 0;
        for (int i = 0; i < getIndividus().length; i++) {
            if (getIndividus()[i] != null) {
                newtab[newi] = getIndividus()[i];
                newi++;
            }
        }
        setIndividus(newtab);
    }

    public void setAttributs(Attribut[] attributs) {
        this.attributs = attributs;
    }

    public void pushIndividu(Individu[] tab, Individu ob) throws Myexception {
        int taille = tab.length + 1;

        Individu[] newtab = new Individu[taille];
        for (int i = 0; i < tab.length; i++) {
            newtab[i] = tab[i];
        }
        newtab[taille - 1] = ob;
        setIndividus(newtab);
    }

    // ajouter un individu
    public void insert(Individu individu) throws Myexception {
        if (isValableIndividu(individu)) {
            pushIndividu(individus, individu);
        }
    }

    public Individu[] getIndividus() {
        return individus;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Relation select(Condition cd) throws Myexception {
        // Vérification du type de constante et de l'attribut
        String constanteType = null;
        if (cd.getConstante() != null) {
            constanteType = Domaine.quelType(cd.getConstante());
        }
        String attributType = cd.getElement1().getDomaine().getType();
    
        if (constanteType != null && !constanteType.equalsIgnoreCase(attributType) && !constanteType.equalsIgnoreCase("null")) {
            throw new Myexception("L'élément et la constante doivent être dans un même domaine: " + constanteType + " et " + attributType);
        }
        Relation resultRelation = new Relation(nom + " selected", attributs);
    
        // List of valid operators
        List<String> validOperators = Arrays.asList(valableoperator);
        Attribut element1 = cd.getElement1();
        Attribut element2 = cd.getElement2();
        Object constante = cd.getConstante();
        String operator = cd.getOperator();
        // System.out.println("1>" +element1.getNom() + " " +  element2.getNom());
        // Liste pour les index qui remplissent la condition
        List<Integer> matchingIndexes = new ArrayList<>();
    
        if (element1 != null && constante != null && validOperators.contains(operator)) {
            // Comparaison attribut vs constante
            int attributeIndex = findAttributeIndex(attributs, element1.getNom());

    
            Ensemble[] ensembles = getEnsembles();
            Object[] attributeValues = ensembles[attributeIndex].getElements();
    
            for (int i = 0; i < attributeValues.length; i++) {
                if (compare(attributeValues[i], constante, operator, constanteType)) {
                    matchingIndexes.add(i);
                }
            }
        } else if (element1 != null && element2 != null && validOperators.contains(operator)) {
            // Comparaison attribut vs attribut
            int attributeIndex1 = findAttributeIndex(attributs, element1.getNom());
            int attributeIndex2 = findAttributeIndex(attributs, element2.getNom());
    
            if (!attributs[attributeIndex1].getDomaine().getType().equalsIgnoreCase(attributs[attributeIndex2].getDomaine().getType())) {
                throw new Myexception("Les attributs ne sont pas dans le même domaine");
            }
            initializeEnsemble();
            Ensemble[] ensembles = getEnsembles();
            Object[] values1 = ensembles[attributeIndex1].getElements();
            Object[] values2 = ensembles[attributeIndex2].getElements();
    
            for (int i = 0; i < values1.length; i++) {
                if (compare(values1[i], values2[i], operator, attributs[attributeIndex1].getDomaine().getType())) {
                    matchingIndexes.add(i);
                }
            }
        } else {
            throw new Myexception("Erreur de condition " + cd.getOperator() + " ou élément null");
        }
    
        // Ajouter les individus qui satisfont la condition dans la relation de résultat
        for (int index : matchingIndexes) {
            Object[] values = new Object[getAttributs().length];
            for (int i = 0; i < ensembles.length; i++) {
                values[i] = ensembles[i].getElements()[index];
            }
            resultRelation.insert(new Individu(attributs, values));
        }
        resultRelation.initializeEnsemble();
    
        return resultRelation;
    }
    
    // Méthode utilitaire pour trouver l'index d'un attribut
    private int findAttributeIndex(Attribut[] attributs, String attributeName) throws Myexception {
        for (int i = 0; i < attributs.length; i++) {
            if (attributs[i].getNom().equalsIgnoreCase(attributeName)) {
                return i;
            }
        }
        
        throw new Myexception("Attribut non trouvé: " + attributeName);
    }
    
    // Méthode utilitaire pour comparer deux objets
    private boolean compare(Object obj1, Object obj2, String operator, String type) throws Myexception {
        switch (operator) {
            case "=":
                return obj1.toString().equalsIgnoreCase(obj2.toString());
            case "!=":
                return !obj1.toString().equalsIgnoreCase(obj2.toString());
            case ">":
                if (type.equalsIgnoreCase("varchar")) {
                    throw new Myexception("Opération > non valable pour varchar");
                } else {
                    return Double.valueOf(obj1.toString()) > Double.valueOf(obj2.toString());
                }
            case ">=":
                if (type.equalsIgnoreCase("varchar")) {
                    throw new Myexception("Opération >= non valable pour varchar");
                } else {
                    return Double.valueOf(obj1.toString()) >= Double.valueOf(obj2.toString());
                }
            case "<":
                if (type.equalsIgnoreCase("varchar")) {
                    throw new Myexception("Opération < non valable pour varchar");
                } else {
                    return Double.valueOf(obj1.toString()) < Double.valueOf(obj2.toString());
                }
            case "<=":
                if (type.equalsIgnoreCase("varchar")) {
                    throw new Myexception("Opération <= non valable pour varchar");
                } else {
                    return Double.valueOf(obj1.toString()) <= Double.valueOf(obj2.toString());
                }
            default:
                throw new Myexception("Opérateur non valide: " + operator);
        }
    }

    // selected
    public Relation select(Condition[] conditions) throws Myexception {
        Relation resp = new Relation("lia", attributs);
        if (resp.getIndividus().length == 0) {
            for (Individu individu : individus) {
                resp.insert(individu);
            }
        }
        if (conditions.length == 1) {
            return select(conditions[0]);
        }
        // System.out.println(resp.getEnsembles().length);
        List<Relation> listand = new ArrayList<>();
        List<Relation> listor = new ArrayList<>();

        // System.out.println(conditions.length);
        for (int i = 0; i < conditions.length - 1; i++) {
            Condition cd = conditions[i];
            Condition cdplus = conditions[i + 1];
            String liaison = cd.getLiaison();
            if (liaison != null) {
                if (cd.getLiaison().equalsIgnoreCase("and")) {
                    if (resp.getIndividus().length != 0) {
                        resp = select(cd);
                        resp = resp.select(cdplus);
                        listand.add(resp);
                    }
                }
                if (cd.getLiaison().equalsIgnoreCase("or")) {
                    resp = new Relation("lia", attributs);
                    for (Individu individu : individus) {
                        resp.insert(individu);
                    }
                }
            }
        }
        int d = 1;
        for (Relation relation : listand) {
            // System.out.println("select " + d++);
            // relation.show();
        }
        for (int i = 0; i < conditions.length - 1; i++) {
            Condition cd = conditions[i];
            Condition cdplus = conditions[i + 1];
            Condition cdmoins = null;
            if (i > 0) {
                cdmoins = conditions[i - 1];
            }
            String cdmoinsliasions = null;
            if (cdmoins != null) {
                cdmoinsliasions = cdmoins.getLiaison();
            }

            String liaison = cd.getLiaison();
            if (liaison != null) {
                if (cdmoins != null && cdplus != null) {
                    // System.out.println("cdmoins.getLiaison()" + cdmoins.getLiaison());
                    // System.out.println("cdplus.getLiaison()" + cdplus.getLiaison());
                    if (cd.getLiaison().equalsIgnoreCase("or")) {
                        if ((cdmoins.getLiaison() == null || !cdmoins.getLiaison().equalsIgnoreCase("and"))) {
                            listor.add(select(cd));
                        }
                        if ((cdplus.getLiaison() == null || !cdplus.getLiaison().equalsIgnoreCase("and"))) {
                            listor.add(select(cdplus));
                        }
                    }
                }

                if (cdmoins == null && cd.getLiaison().equalsIgnoreCase("or")) {

                    resp = new Relation("lia", attributs);
                    for (Individu individu : individus) {
                        resp.insert(individu);
                    }
                    resp.initializeEnsemble();
                    Relation runioner = resp.select(cdplus);
                    listor.add(runioner);
                    resp = resp.union(runioner, "liaison");
                }
            }

        }
        // System.out.println("-----------or--------------");
        for (Relation relation : listor) {
            // System.out.println("select " + d++);
            // relation.show();
        }
        listand.addAll(listor);
        resp = new Relation("selected " + getNom(), attributs);
        for (int i = 0; i < listand.size(); i++) {
            resp = resp.union(listand.get(i), nom);
        }
        return resp;
    }

    // jointure
    public Relation join(Attribut attribut1, Relation r2, Attribut attribut2) throws Myexception {
        // if (!attribut1.getDomaine().getType().equalsIgnoreCase(attribut2.getDomaine().getType())) {
        //     throw new Myexception(" colonne de type different sur  " + attribut1.getDomaine().getType() + " et "
        //             + attribut2.getDomaine().getType());
        // }
        // List<Attribut> attr1 = new ArrayList<>(Arrays.asList(getAttributs()));
        // List<Attribut> attr2 = new ArrayList<>(Arrays.asList(r2.getAttributs()));
        // attr1.addAll(attr2);
        // Attribut[] respAttributs = attr1.toArray(new Attribut[0]);
        // Relation resp = new Relation(
        //         getNom() + " join " + r2.getNom() + " on " + getNom() + "." + attribut1.getNom() + " = " + r2.getNom()
        //                 + "." + attribut2.getNom(),
        //         respAttributs);
        // List<Integer> listvalide1 = new ArrayList<>();
        // List<Integer> listvalide2 = new ArrayList<>();
        // int indexAattribut1 = 0;
        // int indexAattribut2 = 0;
        // for (int i = 0; i < attributs.length; i++) {
        //     if (attributs[i].getNom().equalsIgnoreCase(attribut1.getNom())) {
        //         indexAattribut1 = i;
        //     }
        // }
        // for (int i = 0; i < r2.getAttributs().length; i++) {
        //     if (r2.getAttributs()[i].getNom().equalsIgnoreCase(attribut2.getNom())) {
        //         indexAattribut2 = i;
        //     }
        // }
        // Ensemble ensemble1[] = getEnsembles();
        // Ensemble ensemble2[] = r2.getEnsembles();
        // Object[] elemObjects1 = ensemble1[indexAattribut1].getElements();
        // Object[] elemObjects2 = ensemble2[indexAattribut2].getElements();
        // Ensemble inter = ensemble1[indexAattribut1].intersection(ensemble2[indexAattribut2]);
        // List<Object> dr = Arrays.asList(inter.getElements());
        // for (int i = 0; i < elemObjects1.length; i++) {
        //     if (dr.contains(elemObjects1[i])) {
        //         listvalide1.add(i);
        //     }
        // }
        // for (int i = 0; i < elemObjects2.length; i++) {
        //     if (dr.contains(elemObjects2[i])) {
        //         listvalide2.add(i);
        //     }
        // }

        // for (int i = 0; i < listvalide1.size(); i++) {
        //     List<Object> indiObjects = new ArrayList<>();
        //     for (int j = 0; j < ensemble1.length; j++) {
        //         indiObjects.add(ensemble1[j].getElements()[listvalide1.get(i)]);
        //     }
        //     for (int j = 0; j < ensemble2.length; j++) {
        //         if ( i < listvalide2.size()) {
                    
        //             indiObjects.add(ensemble2[j].getElements()[listvalide2.get(i)]);
        //         }
        //         else 
        //         {
        //             indiObjects.add("null");

        //         }
        //     }
        //     Individu individu = new Individu(resp.getAttributs(), indiObjects.toArray(new Object[0]));
        //     resp.insert(individu);
        // }
        Relation  resp = produitCartesien(r2,getNom() + " join "+ r2.getNom() +"  on  " +getNom()+ ".idcours = "+r2.getNom()+".idcours");
        Condition cd = new Condition(attribut1, "=", attribut2);
        resp =   resp.select(cd);
        return resp;
    }

    // tetha jointure
    public Relation join(Attribut attribut1, Relation r2, Attribut attribut2, Condition[] conditions)
            throws Myexception {
        // if (!attribut1.getDomaine().getType().equalsIgnoreCase(attribut2.getDomaine().getType())) {
        //     throw new Myexception(" colonne de type different sur  " + attribut1.getDomaine().getType() + " et "
        //             + attribut2.getDomaine().getType());
        // }
        // List<Attribut> attr1 = new ArrayList<>(Arrays.asList(getAttributs()));
        // List<Attribut> attr2 = new ArrayList<>(Arrays.asList(r2.getAttributs()));
        // attr1.addAll(attr2);
        // Attribut[] respAttributs = attr1.toArray(new Attribut[0]);
        // Relation resp = new Relation(
        //         getNom() + " join " + r2.getNom() + " on " + getNom() + "." + attribut1.getNom() + " = " + r2.getNom()
        //                 + "." + attribut2.getNom(),
        //         respAttributs);
        // List<Integer> listvalide1 = new ArrayList<>();
        // List<Integer> listvalide2 = new ArrayList<>();
        // int indexAattribut1 = 0;
        // int indexAattribut2 = 0;
        // for (int i = 0; i < attributs.length; i++) {
        //     if (attributs[i].getNom().equalsIgnoreCase(attribut1.getNom())) {
        //         indexAattribut1 = i;
        //     }
        // }
        // for (int i = 0; i < r2.getAttributs().length; i++) {
        //     if (r2.getAttributs()[i].getNom().equalsIgnoreCase(attribut2.getNom())) {
        //         indexAattribut2 = i;
        //     }
        // }
        // Ensemble ensemble1[] = getEnsembles();
        // Ensemble ensemble2[] = r2.getEnsembles();
        // Object[] elemObjects1 = ensemble1[indexAattribut1].getElements();
        // Object[] elemObjects2 = ensemble2[indexAattribut2].getElements();
        // Ensemble inter = ensemble1[indexAattribut1].intersection(ensemble2[indexAattribut2]);
        // List<Object> dr = Arrays.asList(inter.getElements());
        // for (int i = 0; i < elemObjects1.length; i++) {
        //     if (dr.contains(elemObjects1[i])) {
        //         listvalide1.add(i);
        //     }
        // }
        // for (int i = 0; i < elemObjects2.length; i++) {
        //     if (dr.contains(elemObjects2[i])) {
        //         listvalide2.add(i);
        //     }
        // }
        // for (int i = 0; i < listvalide1.size(); i++) {
        //     List<Object> indiObjects = new ArrayList<>();
        //     for (int j = 0; j < ensemble1.length; j++) {
        //         indiObjects.add(ensemble1[j].getElements()[listvalide1.get(i)]);
        //     }
            
        //     for (int j = 0; j < ensemble2.length; j++) {
        //         if ( i < listvalide2.size()) {
                    
        //             indiObjects.add(ensemble2[j].getElements()[listvalide2.get(i)]);
        //         }
        //         else 
        //         {
        //             indiObjects.add("null");

        //         }
        //     }
        //     Individu individu = new Individu(respAttributs, indiObjects.toArray(new Object[0]));
        //     resp.insert(individu);
        // }
        Relation  resp = join(attribut1, r2, attribut2);
        resp = resp.select(conditions);
        return resp;
    }

    // produit cartesien
    public Relation produitCartesien(Relation r2, String nom) throws Myexception {
        Individu[] r1Individus = getIndividus();
        Attribut[] r1Attributs = getAttributs();
        Individu[] r2Individus = r2.getIndividus();
        Attribut[] r2Attributs = r2.getAttributs();
    
        // Création des nouveaux attributs avec noms concaténés
        Attribut[] nouveauxAttributs = new Attribut[r1Attributs.length + r2Attributs.length];
        
        // Ajouter les attributs de la première relation avec les noms modifiés
        for (int i = 0; i < r1Attributs.length; i++) {
            nouveauxAttributs[i] = new Attribut(getNom() + "." + r1Attributs[i].getNom(), r1Attributs[i].getDomaine());
        }
        
        // Ajouter les attributs de la deuxième relation avec les noms modifiés
        for (int i = 0; i < r2Attributs.length; i++) {
            nouveauxAttributs[r1Attributs.length + i] = new Attribut(r2.getNom() + "." + r2Attributs[i].getNom(), r2Attributs[i].getDomaine());
        }
        
        Relation produitRelation = new Relation(nom, nouveauxAttributs);
        
        // Effectuer le produit cartésien des individus
        for (Individu r1Individu : r1Individus) {
            for (Individu r2Individu : r2Individus) {
                Object[] valeursCombinees = new Object[nouveauxAttributs.length];
                System.arraycopy(r1Individu.getValeurs(), 0, valeursCombinees, 0, r1Attributs.length);
                System.arraycopy(r2Individu.getValeurs(), 0, valeursCombinees, r1Attributs.length, r2Attributs.length);
    
                Individu nouveauIndividu = new Individu(nouveauxAttributs, valeursCombinees);
                produitRelation.pushIndividu(produitRelation.getIndividus(), nouveauIndividu);
            }
        }
        
        produitRelation.distinct();
        return produitRelation;
    }
    

    public Relation projection(Attribut[] attributsProjection) throws Myexception {
        Attribut[] r1Attributs = getAttributs();
        Individu[] r1Individus = getIndividus();

        int can = 0;
        for (Attribut attrProj : attributsProjection) {
            for (Attribut attr : r1Attributs) {
                if (attr.getNom().equalsIgnoreCase(attrProj.getNom())) {
                    can++;
                    break;
                }
            }
        }
        if (can == attributsProjection.length) {
            Relation pRelation = new Relation("projection", attributsProjection);

            for (Individu individu : r1Individus) {
                Object[] valeursProjetes = new Object[attributsProjection.length];

                for (int i = 0; i < attributsProjection.length; i++) {
                    for (int j = 0; j < r1Attributs.length; j++) {
                        if (attributsProjection[i].getNom().equalsIgnoreCase(r1Attributs[j].getNom())) {
                            valeursProjetes[i] = individu.getValeurs()[j];
                            break;
                        }
                    }
                }

                Individu nouveauIndividu = new Individu(attributsProjection, valeursProjetes);
                pRelation.pushIndividu(pRelation.getIndividus(), nouveauIndividu);
            }
            pRelation.distinct();
            return pRelation;
        } else {
            System.out.println("Attributes not found");
            return null;
        }
    }

    public Relation difference(Relation r2, String name) throws Myexception {
        Individu[] r1Individus = getIndividus();
        Individu[] r2Individus = r2.getIndividus();
        ArrayList<Individu> diffIndividus = new ArrayList<>();
        Relation diffRelation = new Relation(name, attributs);
        if (checkAttribut(r2)) {
            for (Individu r1Individu : r1Individus) {
                boolean found = false;
                for (Individu r2Individu : r2Individus) {
                    if (r1Individu.equals(r2Individu)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    diffIndividus.add(r1Individu);
                }
            }
            diffRelation.setIndividus(diffIndividus.toArray(new Individu[0]));
        }
        diffRelation.distinct();
        return diffRelation;
    }

    // methode intersection
    public Relation intersect(Relation r2, String name) throws Myexception {
        Individu[] r1Individus = getIndividus();
        Individu[] r2Individus = r2.getIndividus();
        ArrayList<Individu> interseIndividus = new ArrayList<>();
        Relation interRelation = new Relation(name, attributs);
        if (checkAttribut(r2)) {
            for (int i = 0; i < r1Individus.length; i++) {
                for (int j = 0; j < r2Individus.length; j++) {
                    if (r1Individus[i].equals(r2Individus[j])) {
                        interseIndividus.add(r1Individus[i]);
                    }
                }
            }
            interRelation.setIndividus(interseIndividus.toArray(new Individu[0]));
        }
        interRelation.distinct();
        return interRelation;
    }

    // voir si lindividu peut etre inserer dans une relation
    public boolean inserable(Individu individu) {
        Attribut[] indivAttributs = individu.getAttributs();
        if (indivAttributs.length != attributs.length) {
            return false;
        }
        int i = 0;
        int plage = indivAttributs.length;

        while (i < plage) {
            String attributType = indivAttributs[i].getDomaine().getType();
            String attributname = indivAttributs[i].getNom();

            String relationtype = attributs[i].getDomaine().getType();
            String relationname = attributs[i].getNom();

            if (!attributType.equalsIgnoreCase(relationtype) || !attributname.equalsIgnoreCase(relationname)) {
                System.out.println("Number of column or colomn not match for " + nom + "...");
                return false;
            }

            i++;
        }
        return true;

    }

    // voir si lindividu peut etre inserer dans une relation
    public boolean isValableIndividu(Individu individu) throws Myexception {
        Attribut[] indivAttributs = individu.getAttributs();
        Object[] indivValeur = individu.getValeurs();
        if (indivAttributs.length != indivValeur.length) {
            System.out.println("number of column doesn't match for " + nom + "....");
            return false;
        }
        if (!limited(individu)) {
            return false;
        }
        if (!inserable(individu)) {
            return false;
        }

        return alignerAV(individu);
    }

    // voir si la valeur inbriquer est conforme a une taille donne
    public boolean limited(Individu individu) {
        Attribut[] indivAttributs = individu.getAttributs();
        Object[] indivValeur = individu.getValeurs();
        int i = 0;
        int plage = indivAttributs.length;
        while (i < plage) {

            Integer limit = indivAttributs[i].getDomaine().getLimit();
            if (limit != null) {
                String valeur = indivValeur[i].toString();
                if (valeur.length() > limit) {
                    System.out.println("Out of precision for " + nom + "....");
                    return false;
                }
            }

            i++;
        }
        return true;
    }

    // voir si les valeurs dun individu se conforme a lattribut
    public boolean alignerAV(Individu individu) throws Myexception {
        Attribut[] indivAttributs = individu.getAttributs();
        Object[] indivValeur = individu.getValeurs();
        int i = 0;
        int plage = indivAttributs.length;
        while (i < plage) {
            String attributType = indivAttributs[i].getDomaine().getType();
            String valuerType = Domaine.quelType(indivValeur[i]);
            if (!attributType.equalsIgnoreCase(valuerType)) {

                throw new Myexception("the attribut type " + attributType + " from " + this.nom
                        + " not match for values of type " + valuerType + " from individu." + indivAttributs[i].getNom()
                        + " please check your insert....");
            }
            i++;
        }
        return true;
    }

    // affiche la relation
    public void show() {
        System.out.println("(" + getNom() + ")");
        for (Attribut attribut : attributs) {
            System.out.print(attribut.getNom() + "             ");
        }
        System.out.println();
        for (Individu individu : individus) {
            if (individu != null) {
                for (Object ob : individu.getValeurs()) {
                    System.out.print(ob +  "             ");
                }
            }

            System.out.println();
        }
        System.out.println();
    }

    // enlever les doublures dindividu dune relation
    public void distinct() throws Myexception {
        Individu[] indiv = getIndividus();
        List<Individu> indi =  new ArrayList<>(Arrays.asList(indiv)) ;
        for (int i = 0; i < indiv.length; i++) {
            for (int j = i + 1; j < indiv.length; j++) {
                    if ( indiv[i] != null &&  indiv[i].equals(indiv[j])) {
                        indi.remove(indiv[j]);
                    }
            }
        }
        setIndividus(indi.toArray(new Individu[0]));
    }

    public void setIndividus(Individu[] individus) throws Myexception {
        this.individus = individus;
    }

    // une methode pour avoir une relation qui est l'union de cette relation avec r2
    public Relation union(Relation r2, String nameUnion) throws Myexception {
        Relation resp = null;
        Individu[] r2Individus = r2.getIndividus();
        if (checkAttribut(r2)) {
            resp = new Relation(nameUnion, attributs);
            resp.setIndividus(individus);
            for (int i = 0; i < r2.getIndividus().length; i++) {
                resp.pushIndividu(resp.getIndividus(), r2Individus[i]);
            }

        }
        if (resp != null) {
            resp.distinct();
        }
        return resp;
    }

    // voir si deux relation on le meme nombre et type dattribut
    public boolean checkAttribut(Relation r2) {
        Attribut[] r1Att = getAttributs();
        Attribut[] r2Att = r2.getAttributs();

        if (r1Att.length != r2Att.length) {
            System.out.println("Number of column doesn't match for " + nom + " with " + r2.getNom());
            return false;
        }
        int i = 0;
        int plage = r1Att.length;
        while (i < plage) {
            String r1attType = r1Att[i].getDomaine().getType();
            String r2attType = r2Att[i].getDomaine().getType();
            if (!r1attType.equalsIgnoreCase(r2attType)) {
                return false;
            }

            i++;
        }
        return true;

    }

    public String getNom() {
        return nom;
    }

    public Attribut[] getAttributs() {
        return attributs;
    }
}
