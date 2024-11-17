package relation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Attr;

import exception.*;

public class Relation implements Cloneable {
    String nom;
    Attribut[] attributs = new Attribut[0];
    Individu[] individus = new Individu[0];
    String[] valableoperator = { "=", "!=", "<", "<=", ">=", ">", "<" };
    Ensemble[] ensembles;
    Condition condition;

    public Relation(String nom, Attribut[] attributs) throws Myexception {
        setNom(nom);
        setAttributs(attributs);
        initializeEnsemble();
    }

    public void initializeEnsemble() throws Myexception {
        int indexattribut = getAttributs().length;
        Attribut[] attribut = getAttributs();
        ensembles = new Ensemble[indexattribut];
        Individu[] individus = getIndividus();
        for (int i = 0; i < attribut.length; i++) {
            Object[] elements = new Object[individus.length];
            for (int j = 0; j < individus.length; j++) {
                elements[j] = individus[j].getValeurs()[i];
            }
            if (elements.length != 0) {
                ensembles[i] = new Ensemble(getAttributs()[i].getNom(), elements);
            }

        }
    }

    public Relation() {

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

        List<Individu> individus = new ArrayList<>(Arrays.asList(getIndividus()));
        for (Individu individu : individus) {
            if (individu.equals(ob)) {
                individus.remove(individu);
                break;
            }
        }
        setIndividus(individus.toArray(new Individu[0]));
    }

    public void setIndividus(Individu[] individus) {
        this.individus = individus;
    }

    public void setAttributs(Attribut[] attributs) {
        this.attributs = attributs;
    }

    public void pushIndividu(Individu ob) throws Myexception {
        List<Individu> individus = new ArrayList<>(Arrays.asList(getIndividus()));
        individus.add(ob);
        setIndividus(individus.toArray(new Individu[0]));
    }

    // ajouter un individu
    public void insert(Individu[] individus) throws Myexception {
        for (Individu individu : individus) {
            inserable(individu);
            pushIndividu(individu);
        }

    }

    public void copyAttributValues(Individu individu) {
        // Attribut[] indAttributs = individu.getAttributs();
        // Attribut[] attributs = getAttributs();
        // for ( i = 0; i < attributs.length; i++) {
        // Attribut attInd = indAttributs[i];
        // Attribut att = attributs[i];

        // List<Object> indivAttOb = new ArrayList<>();
        // List<Object> AttOb = new ArrayList<>(attInd.ge);

        // }
    }

    public Individu[] getIndividus() {
        return individus;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Relation select(Condition cd) throws Myexception {
        // Vérification du type de constante et de l'attribut
        Class<?> constanteType = null;
        for (Attribut att : getAttributs()) {
            if (cd.getElement1() != null && att.getNom().equalsIgnoreCase(cd.getElement1().getNom())) {
                cd.setElement1(att);
            }
        }
        for (Attribut att : getAttributs()) {
            if (cd.getElement2() != null && att.getNom().equalsIgnoreCase(cd.getElement2().getNom())) {
                cd.setElement2(att);
            }
        }

        if (cd.getConstante() != null) {
            constanteType = Domaine.quelType(cd.getConstante());
        }
        List<Class<?>> attributType = new ArrayList<>(Arrays.asList(cd.getElement1().getDomaine().getType()));

        if (constanteType != null && !attributType.contains(constanteType)) {
            throw new Myexception("L'élément et la constante doivent être dans un mêmedomaine: " + constanteType
                    + " et " + attributType);
        }
        Relation resultRelation = new Relation(nom + " selected", attributs);

        // List of valid operators
        List<String> validOperators = Arrays.asList(valableoperator);
        Attribut element1 = cd.getElement1();

        Attribut element2 = cd.getElement2();
        Object constante = cd.getConstante();
        String operator = cd.getOperator();
        // System.out.println("1>" +element1.getNom() + " " + element2.getNom());
        // Liste pour les index qui remplissent la condition
        List<Integer> matchingIndexes = new ArrayList<>();

        if (element1 != null && constante != null &&
                validOperators.contains(operator)) {
            // Comparaison attribut vs constante
            Integer attributeIndex = findAttributeIndex(attributs, element1.getNom());

            Object[] attributeValues = attributs[attributeIndex].getValeurs();

            for (int i = 0; i < attributeValues.length; i++) {
                if (compare(attributeValues[i], constante, operator, constanteType)) {
                    matchingIndexes.add(i);
                }
            }
        } else if (element1 != null && element2 != null &&
                validOperators.contains(operator)) {
            // Comparaison attribut vs attribut
            int attributeIndex1 = findAttributeIndex(attributs, element1.getNom());
            int attributeIndex2 = findAttributeIndex(attributs, element2.getNom());

            System.out.println("index 1 "  + element1.getNom());
            System.out.println("index 2 "  + element2.getNom());

            Object[] values1 = attributs[attributeIndex1].getValeurs();
            Object[] values2 = attributs[attributeIndex2].getValeurs();

            for (int i = 0; i < values1.length; i++) {
                if (compare(values1[i], values2[i], operator,
                        values2[i].getClass())) {
                    matchingIndexes.add(i);
                }
            }
        } else {
            throw new Myexception("Erreur de condition " + cd.getOperator() + " ou" +
                    "élément null");
        }
        System.out.println(matchingIndexes);

        for (int index : matchingIndexes) {
            Object[] values = new Object[attributs.length];
            for (int i = 0; i < attributs.length; i++) {
                values[i] = attributs[i].getValeurs()[index];
                // values[i] = ensembles[i].getElements()[index];

            }
            Individu individu = new Individu(attributs);
            individu.setValeurs(values);
            resultRelation.pushIndividu(individu);
        }
        resultRelation.initializeEnsemble();

        return resultRelation;
    }

    // Méthode utilitaire pour trouver l'index d'un attribut
    private int findAttributeIndex(Attribut[] attributs, String attributeName)
            throws Myexception {
        for (int i = 0; i < attributs.length; i++) {
            if (attributs[i].getNom().equalsIgnoreCase(attributeName)) {
                return i;
            }
        }

        throw new Myexception("Attribut non trouvé: " + attributeName);
    }

    // // Méthode utilitaire pour comparer deux objets
    private boolean compare(Object obj1, Object obj2, String operator, Class<?> type) throws Myexception {
        switch (operator) {
            case "=":
                return obj1.toString().equalsIgnoreCase(obj2.toString());
            case "!=":
                return !obj1.toString().equalsIgnoreCase(obj2.toString());
            case ">":
            case ">=":
            case "<":
            case "<=":
                if (type == String.class) {
                    return false;
                    // throw new Myexception("Opération " + operator + " non valable pour String (varchar).");
                } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                    List<Class<?>> typList =  new ArrayList<>();
                    typList.add(Double.class);
                    typList.add(Integer.class);
                    if (typList.contains(obj1.getClass())  && typList.contains(obj2.getClass())) {
                    double val1 = Double.parseDouble(obj1.toString());
                    double val2 = Double.parseDouble(obj2.toString());
                    return evaluateNumericalComparison(val1, val2, operator);
                    }
                    
                } else {
                    return false;
                    // throw new Myexception(
                    //         "Opération " + operator + " non valable pour le type " + type.getSimpleName() + ".");
                }
            default:
                return false;
                // throw new Myexception("Opérateur non valide: " + operator);
        }
    }

    private boolean evaluateNumericalComparison(double val1, double val2, String operator) {
        return switch (operator) {
            case ">" -> val1 > val2;
            case ">=" -> val1 >= val2;
            case "<" -> val1 < val2;
            case "<=" -> val1 <= val2;
            default -> false;
        };
    }
    // // selected
    // public Relation select(Condition[] conditions) throws Myexception {
    // Relation resp = new Relation("lia", attributs);
    // if (resp.getIndividus().length == 0) {
    // for (Individu individu : individus) {
    // resp.insert(individu);
    // }
    // }
    // if (conditions.length == 1) {
    // return select(conditions[0]);
    // }
    // // System.out.println(resp.getEnsembles().length);
    // List<Relation> listand = new ArrayList<>();
    // List<Relation> listor = new ArrayList<>();

    // // System.out.println(conditions.length);
    // for (int i = 0; i < conditions.length - 1; i++) {
    // Condition cd = conditions[i];
    // Condition cdplus = conditions[i + 1];
    // String liaison = cd.getLiaison();
    // if (liaison != null) {
    // if (cd.getLiaison().equalsIgnoreCase("and")) {
    // if (resp.getIndividus().length != 0) {
    // resp = select(cd);
    // resp = resp.select(cdplus);
    // listand.add(resp);
    // }
    // }
    // if (cd.getLiaison().equalsIgnoreCase("or")) {
    // resp = new Relation("lia", attributs);
    // for (Individu individu : individus) {
    // resp.insert(individu);
    // }
    // }
    // }
    // }
    // int d = 1;
    // for (Relation relation : listand) {
    // // System.out.println("select " + d++);
    // // relation.show();
    // }
    // for (int i = 0; i < conditions.length - 1; i++) {
    // Condition cd = conditions[i];
    // Condition cdplus = conditions[i + 1];
    // Condition cdmoins = null;
    // if (i > 0) {
    // cdmoins = conditions[i - 1];
    // }
    // String cdmoinsliasions = null;
    // if (cdmoins != null) {
    // cdmoinsliasions = cdmoins.getLiaison();
    // }

    // String liaison = cd.getLiaison();
    // if (liaison != null) {
    // if (cdmoins != null && cdplus != null) {
    // // System.out.println("cdmoins.getLiaison()" + cdmoins.getLiaison());
    // // System.out.println("cdplus.getLiaison()" + cdplus.getLiaison());
    // if (cd.getLiaison().equalsIgnoreCase("or")) {
    // if ((cdmoins.getLiaison() == null ||
    // !cdmoins.getLiaison().equalsIgnoreCase("and"))) {
    // listor.add(select(cd));
    // }
    // if ((cdplus.getLiaison() == null ||
    // !cdplus.getLiaison().equalsIgnoreCase("and"))) {
    // listor.add(select(cdplus));
    // }
    // }
    // }

    // if (cdmoins == null && cd.getLiaison().equalsIgnoreCase("or")) {

    // resp = new Relation("lia", attributs);
    // for (Individu individu : individus) {
    // resp.insert(individu);
    // }
    // resp.initializeEnsemble();
    // Relation runioner = resp.select(cdplus);
    // listor.add(runioner);
    // resp = resp.union(runioner, "liaison");
    // }
    // }

    // }
    // // System.out.println("-----------or--------------");
    // for (Relation relation : listor) {
    // // System.out.println("select " + d++);
    // // relation.show();
    // }
    // listand.addAll(listor);
    // resp = new Relation("selected " + getNom(), attributs);
    // for (int i = 0; i < listand.size(); i++) {
    // resp = resp.union(listand.get(i), nom);
    // }
    // return resp;
    // }

    // // jointure
    // public Relation join(Attribut attribut1, Relation r2, Attribut attribut2)
    // throws Myexception {
    // // if
    // (!attribut1.getDomaine().getType().equalsIgnoreCase(attribut2.getDomaine().getType()))
    // {
    // // throw new Myexception(" colonne de type different sur " +
    // attribut1.getDomaine().getType() + " et "
    // // + attribut2.getDomaine().getType());
    // // }
    // // List<Attribut> attr1 = new ArrayList<>(Arrays.asList(getAttributs()));
    // // List<Attribut> attr2 = new ArrayList<>(Arrays.asList(r2.getAttributs()));
    // // attr1.addAll(attr2);
    // // Attribut[] respAttributs = attr1.toArray(new Attribut[0]);
    // // Relation resp = new Relation(
    // // getNom() + " join " + r2.getNom() + " on " + getNom() + "." +
    // attribut1.getNom() + " = " + r2.getNom()
    // // + "." + attribut2.getNom(),
    // // respAttributs);
    // // List<Integer> listvalide1 = new ArrayList<>();
    // // List<Integer> listvalide2 = new ArrayList<>();
    // // int indexAattribut1 = 0;
    // // int indexAattribut2 = 0;
    // // for (int i = 0; i < attributs.length; i++) {
    // // if (attributs[i].getNom().equalsIgnoreCase(attribut1.getNom())) {
    // // indexAattribut1 = i;
    // // }
    // // }
    // // for (int i = 0; i < r2.getAttributs().length; i++) {
    // // if (r2.getAttributs()[i].getNom().equalsIgnoreCase(attribut2.getNom())) {
    // // indexAattribut2 = i;
    // // }
    // // }
    // // Ensemble ensemble1[] = getEnsembles();
    // // Ensemble ensemble2[] = r2.getEnsembles();
    // // Object[] elemObjects1 = ensemble1[indexAattribut1].getElements();
    // // Object[] elemObjects2 = ensemble2[indexAattribut2].getElements();
    // // Ensemble inter =
    // ensemble1[indexAattribut1].intersection(ensemble2[indexAattribut2]);
    // // List<Object> dr = Arrays.asList(inter.getElements());
    // // for (int i = 0; i < elemObjects1.length; i++) {
    // // if (dr.contains(elemObjects1[i])) {
    // // listvalide1.add(i);
    // // }
    // // }
    // // for (int i = 0; i < elemObjects2.length; i++) {
    // // if (dr.contains(elemObjects2[i])) {
    // // listvalide2.add(i);
    // // }
    // // }

    // // for (int i = 0; i < listvalide1.size(); i++) {
    // // List<Object> indiObjects = new ArrayList<>();
    // // for (int j = 0; j < ensemble1.length; j++) {
    // // indiObjects.add(ensemble1[j].getElements()[listvalide1.get(i)]);
    // // }
    // // for (int j = 0; j < ensemble2.length; j++) {
    // // if ( i < listvalide2.size()) {

    // // indiObjects.add(ensemble2[j].getElements()[listvalide2.get(i)]);
    // // }
    // // else
    // // {
    // // indiObjects.add("null");

    // // }
    // // }
    // // Individu individu = new Individu(resp.getAttributs(),
    // indiObjects.toArray(new Object[0]));
    // // resp.insert(individu);
    // // }
    // Relation resp = produitCartesien(r2,getNom() + " join "+ r2.getNom() +" on "
    // +getNom()+ ".idcours = "+r2.getNom()+".idcours");
    // Condition cd = new Condition(attribut1, "=", attribut2);
    // resp = resp.select(cd);
    // return resp;
    // }

    // // tetha jointure
    // public Relation join(Attribut attribut1, Relation r2, Attribut attribut2,
    // Condition[] conditions)
    // throws Myexception {
    // // if
    // (!attribut1.getDomaine().getType().equalsIgnoreCase(attribut2.getDomaine().getType()))
    // {
    // // throw new Myexception(" colonne de type different sur " +
    // attribut1.getDomaine().getType() + " et "
    // // + attribut2.getDomaine().getType());
    // // }
    // // List<Attribut> attr1 = new ArrayList<>(Arrays.asList(getAttributs()));
    // // List<Attribut> attr2 = new ArrayList<>(Arrays.asList(r2.getAttributs()));
    // // attr1.addAll(attr2);
    // // Attribut[] respAttributs = attr1.toArray(new Attribut[0]);
    // // Relation resp = new Relation(
    // // getNom() + " join " + r2.getNom() + " on " + getNom() + "." +
    // attribut1.getNom() + " = " + r2.getNom()
    // // + "." + attribut2.getNom(),
    // // respAttributs);
    // // List<Integer> listvalide1 = new ArrayList<>();
    // // List<Integer> listvalide2 = new ArrayList<>();
    // // int indexAattribut1 = 0;
    // // int indexAattribut2 = 0;
    // // for (int i = 0; i < attributs.length; i++) {
    // // if (attributs[i].getNom().equalsIgnoreCase(attribut1.getNom())) {
    // // indexAattribut1 = i;
    // // }
    // // }
    // // for (int i = 0; i < r2.getAttributs().length; i++) {
    // // if (r2.getAttributs()[i].getNom().equalsIgnoreCase(attribut2.getNom())) {
    // // indexAattribut2 = i;
    // // }
    // // }
    // // Ensemble ensemble1[] = getEnsembles();
    // // Ensemble ensemble2[] = r2.getEnsembles();
    // // Object[] elemObjects1 = ensemble1[indexAattribut1].getElements();
    // // Object[] elemObjects2 = ensemble2[indexAattribut2].getElements();
    // // Ensemble inter =
    // ensemble1[indexAattribut1].intersection(ensemble2[indexAattribut2]);
    // // List<Object> dr = Arrays.asList(inter.getElements());
    // // for (int i = 0; i < elemObjects1.length; i++) {
    // // if (dr.contains(elemObjects1[i])) {
    // // listvalide1.add(i);
    // // }
    // // }
    // // for (int i = 0; i < elemObjects2.length; i++) {
    // // if (dr.contains(elemObjects2[i])) {
    // // listvalide2.add(i);
    // // }
    // // }
    // // for (int i = 0; i < listvalide1.size(); i++) {
    // // List<Object> indiObjects = new ArrayList<>();
    // // for (int j = 0; j < ensemble1.length; j++) {
    // // indiObjects.add(ensemble1[j].getElements()[listvalide1.get(i)]);
    // // }

    // // for (int j = 0; j < ensemble2.length; j++) {
    // // if ( i < listvalide2.size()) {

    // // indiObjects.add(ensemble2[j].getElements()[listvalide2.get(i)]);
    // // }
    // // else
    // // {
    // // indiObjects.add("null");

    // // }
    // // }
    // // Individu individu = new Individu(respAttributs, indiObjects.toArray(new
    // Object[0]));
    // // resp.insert(individu);
    // // }

    // Relation resp = join(attribut1, r2, attribut2);
    // resp = resp.select(conditions);
    // return resp;
    // }

    // // produit cartesien
    public Relation produitCartesien(Relation r2, String nom) throws Myexception {
        Individu[] r1Individus = getIndividus();
        Attribut[] r1Attributs = getAttributs();
        Individu[] r2Individus = r2.getIndividus();
        Attribut[] r2Attributs = r2.getAttributs();
        Attribut[] nouveauxAttributs = new Attribut[r1Attributs.length +
                r2Attributs.length];

        for (int i = 0; i < r1Attributs.length; i++) {
            nouveauxAttributs[i] = new Attribut(getNom() + "." + r1Attributs[i].getNom(),
                    r1Attributs[i].getDomaine());
        }

        for (int i = 0; i < r2Attributs.length; i++) {
            nouveauxAttributs[r1Attributs.length + i] = new Attribut(r2.getNom() + "." +
                    r2Attributs[i].getNom(), r2Attributs[i].getDomaine());
        }

        Relation produitRelation = new Relation(nom, nouveauxAttributs);

        for (Individu r1Individu : r1Individus) {
            for (Individu r2Individu : r2Individus) {
                Object[] valeursCombinees = new Object[nouveauxAttributs.length];
                System.arraycopy(r1Individu.getValeurs(), 0, valeursCombinees, 0,
                        r1Attributs.length);
                System.arraycopy(r2Individu.getValeurs(), 0, valeursCombinees,
                        r1Attributs.length, r2Attributs.length);

                Individu nouveauIndividu = new Individu(nouveauxAttributs);
                nouveauIndividu.setValeurs(valeursCombinees);
                produitRelation.pushIndividu(
                        nouveauIndividu);
            }
        }
        // produitRelation.distinct();
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

                Individu nouveauIndividu = new Individu(attributsProjection);
                nouveauIndividu.setValeurs(valeursProjetes);
                pRelation.pushIndividu(nouveauIndividu);
            }
            pRelation.distinct();
            return pRelation;
        } else {
            throw new Myexception("Colonne non trouver " + Arrays.asList(attributsProjection));
        }
    }

    public void addAttribut(Attribut att) {
        List<Attribut> attt = new ArrayList<>(Arrays.asList(getAttributs()));
        attt.add(att);
        setAttributs(attt.toArray(new Attribut[0]));
    }

    public Relation difference(Relation r2, String name) throws Myexception {
        if (r2 == null) {
            throw new Myexception("The second relation (r2) cannot be null.");
        }
        if (!checkAttribut(r2)) {
            throw new Myexception("Attributes of the two relations do not match.");
        }
        List<Individu> diffIndividus = new ArrayList<>(Arrays.asList(getIndividus()));
        List<Individu> resp = new ArrayList<>(diffIndividus);
        int i = 0;
        for (Individu r1Individu : diffIndividus) {
            for (Individu r2Individu : r2.getIndividus()) {
                if (r1Individu.equals(r2Individu)) {
                    resp.remove(i);
                }
            }
            i++;
        }
        Relation diffRelation = new Relation(name, attributs);
        diffRelation.setIndividus(resp.toArray(new Individu[0]));
        return diffRelation;
    }

    public boolean checkAttribut(Relation r2) {
        Attribut[] r1Attributes = this.getAttributs();
        Attribut[] r2Attributes = r2.getAttributs();
        if (r1Attributes.length != r2Attributes.length) {
            return false;
        }
        return true;
    }

    // // methode intersection
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

    // // voir si lindividu peut etre inserer dans une relation
    public void inserable(Individu individu) throws Myexception {
        if (individu == null) {
            throw new IllegalArgumentException("Individu cannot be null.");
        }

        Attribut[] indivAttributs = individu.getAttributs();
        Attribut[] relationAttributs = getAttributs();

        if (indivAttributs.length != relationAttributs.length) {
            throw new Myexception("Mismatch in the number of attributes for " + nom + ".");
        }

        for (int i = 0; i < indivAttributs.length; i++) {
            Attribut att1 = indivAttributs[i];
            Attribut att2 = relationAttributs[i];
            validateDomainLimitConstraints(att1, att2);
            validateAttributeNames(att1, att2);
            validateAttributeType(att1, att2);
            validateDomainConstraints(att1, att2);
            insert(att2, att1);
        }
    }

    // Helper method to validate attribute names
    private void validateAttributeNames(Attribut att1, Attribut att2) throws Myexception {
        if (!att1.getNom().equalsIgnoreCase(att2.getNom())) {
            throw new Myexception("In " + getNom() + ", attribute names do not match: "
                    + att1.getNom() + " vs " + att2.getNom());
        }
    }

    private void validateAttributeType(Attribut att1, Attribut att2) throws Myexception {
        if (att2.getDomaine().getType().length != 0
                && !Arrays.asList(att2.getDomaine().getType()).contains(att1.getValeur().getClass())) {

            throw new Myexception("In " + getNom() + ", type mismatch for attribute " + att1.getNom()
                    + ". Expected: " + Arrays.asList(att2.getDomaine().getType())
                    + ", Found: " + Domaine.quelType(att1.getValeur()) + " ->" + att1.getValeur());
        }
    }

    // Helper method to validate domain constraints
    private void validateDomainConstraints(Attribut att1, Attribut att2) throws Myexception {
        Domaine domaine = att2.getDomaine();
        if (domaine != null && domaine.getEnsemble() != null && domaine.getEnsemble().getElements() != null) {
            Object[] allowedValues = domaine.getEnsemble().getElements();
            List<Class<?>> types = new ArrayList<>(Arrays.asList(att2.getDomaine().getType()));
            boolean contain = false;
            if (types.contains(att1.getValeur().getClass())) {
                contain = true;
            }
            if (!Arrays.asList(allowedValues).contains(att1.getValeur()) && !contain) {
                throw new Myexception("Value '" + att1.getValeur() + "' is not allowed for attribute "
                        + att1.getNom() + " in domain constraints "
                        + Arrays.asList(att2.getDomaine().getEnsemble().getElements()));
            }
        }
    }

    private void validateDomainLimitConstraints(Attribut att1, Attribut att2) throws Myexception {
        Domaine domaine = att2.getDomaine();
        if (domaine != null && domaine.getLimit() != null) {
            Object value = att1.getValeur();
            String stringValue = value != null ? value.toString() : "";

            if (stringValue.length() > domaine.getLimit()) {
                throw new Myexception("Value for attribute " + att1.getNom()
                        + " exceeds the allowed limit of " + domaine.getLimit()
                        + " characters. Current length: " + stringValue.length() + " '" + att1.getValeur() + "'");
            }
        }
    }

    public void insert(Attribut att1, Attribut att2) {
        if (att1 == null || att2 == null) {
            throw new IllegalArgumentException("Attributs cannot be null");
        }
        List<Object> att = new ArrayList<>(Arrays.asList(att1.getValeurs()));
        att.add(att2.getValeur());
        att1.setValeurs(att.toArray(new Object[0]));
    }

    // // voir si la valeur inbriquer est conforme a une taille donne
    // public boolean limited(Individu individu) throws Myexception {
    // Attribut[] indivAttributs = individu.getAttributs();
    // Object[] indivValeur = individu.getValeurs();
    // int i = 0;
    // int plage = indivAttributs.length;
    // while (i < plage) {

    // Integer limit = indivAttributs[i].getDomaine().getLimit();
    // if (limit != null) {
    // String valeur = indivValeur[i].toString();
    // if (valeur.length() > limit) {
    // throw new Myexception ("Out of precision for " + nom + "....");
    // // System.out.println("Out of precision for " + nom + "....");
    // // return false;
    // }
    // }

    // i++;
    // }
    // return true;
    // }

    // // voir si les valeurs dun individu se conforme a lattribut
    // public boolean alignerAV(Individu individu) throws Myexception {
    // Attribut[] indivAttributs = individu.getAttributs();
    // Object[] indivValeur = individu.getValeurs();
    // int i = 0;
    // int plage = indivAttributs.length;
    // while (i < plage) {
    // String attributType = indivAttributs[i].getDomaine().getType();
    // String valuerType = Domaine.quelType(indivValeur[i]);
    // if (!attributType.equalsIgnoreCase(valuerType)) {

    // throw new Myexception("the attribut type " + attributType + " from " +
    // this.nom
    // + " not match for values of type " + valuerType + " from individu." +
    // indivAttributs[i].getNom()
    // + " please check your insert....");
    // }
    // i++;
    // }
    // return true;
    // }

    // // affiche la relation
    public void show() {
        System.out.printf("%-20s%n", "(" + getNom() + ")");
        for (Attribut attribut : attributs) {
            System.out.printf("%-20s", attribut.getNom());
        }
        System.out.println();
        for (Individu individu : individus) {
            if (individu != null) {
                for (Object ob : individu.getValeurs()) {
                    System.out.printf("%-20s", ob);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // // enlever les doublures dindividu dune relation
    public void distinct() throws Myexception {
        Individu[] indiv = getIndividus();
        List<Individu> indi = new ArrayList<>(Arrays.asList(indiv));
        for (int i = 0; i < indiv.length; i++) {
            for (int j = i + 1; j < indiv.length; j++) {
                if (indiv[i] != null && indiv[i].equals(indiv[j])) {
                    indi.remove(indiv[j]);
                }
            }
        }
        setIndividus(indi.toArray(new Individu[0]));
    }

    // public void setIndividus(Individu[] individus) throws Myexception {
    // this.individus = individus;
    // }

    // // une methode pour avoir une relation qui est l'union de cette relation avec
    // r2

    public Relation union(Relation r2, String nameUnion) throws Myexception, Exception {
        Relation resp = (Relation) this.clone();
        Attribut[] r1Att = getAttributs();
        Attribut[] r2Att = r2.getAttributs();
        List<Individu> individus = new ArrayList<>();
        if (checkAttribut(r2)) {
            for (int i = 0; i < r1Att.length; i++) {
                if (r2Att[i].getDomaine() != null) {
                    for (Class<?> type : r2Att[i].getDomaine().getType()) {
                        r1Att[i].getDomaine().pushType(type);
                    }
                    if (r2Att[i].getDomaine().getEnsemble() != null) {
                        for (Object ele : r2Att[i].getDomaine().getEnsemble().getElements()) {
                            r1Att[i].getDomaine().PushEnsembleElement(ele);
                        }
                    }

                }
                r1Att[i].getDomaine().setNom(r1Att[i].getNom() + "_" + r2Att[i].getNom() + "_Domaine");
                // r1Att[i].setNom(r1Att[i].getNom() + "_" + r2Att[i].getNom());
                r1Att[i].pushValeurs(r2Att[i].getValeurs());
            }
        }
        // System.out.println(Arrays.asList(r1Att[1].getDomaine().getEnsemble().getElements()));

        for (int i = 0; i < r2.getIndividus().length; i++) {
            resp.pushIndividu(r2.getIndividus()[i]);
        }
        resp.setNom(resp.getNom() + "_union_" + r2.getNom());
        resp.distinct();
        return resp;
    }

    // // voir si deux relation on le meme nombre et type dattribut
    // public boolean checkAttribut(Relation r2) throws Myexception {
    // Attribut[] r1Att = getAttributs();
    // Attribut[] r2Att = r2.getAttributs();

    // if (r1Att.length != r2Att.length) {
    // throw new Myexception ("Number of column doesn't match for " + nom + " with "
    // + r2.getNom());
    // // System.out.println("Number of column doesn't match for " + nom + " with "
    // + r2.getNom());
    // // return false;
    // }
    // int i = 0;
    // int plage = r1Att.length;
    // while (i < plage) {
    // String r1attType = r1Att[i].getDomaine().getType();
    // String r2attType = r2Att[i].getDomaine().getType();
    // if (!r1attType.equalsIgnoreCase(r2attType)) {
    // return false;
    // }

    // i++;
    // }
    // return true;

    // }

    public String getNom() {
        return nom;
    }

    public Attribut[] getAttributs() {
        return attributs;
    }
}
