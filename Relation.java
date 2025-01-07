package relation;

import java.lang.reflect.Array;
import java.time.LocalDate;
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

    public Individu[] getIndividus() {
        return individus;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Relation select(Condition cd) throws Myexception {
        initializeAttByIndividu(this);
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
            constanteType = cd.getConstante().getClass();
        }
        
        List<Class<?>> attributType = new ArrayList<>(Arrays.asList(cd.getElement1().getDomaine().getType()));
        if (constanteType != null && !attributType.contains(constanteType)) {
            throw new Myexception("L'élément et la constante doivent être dans un mêmedomaine: " + constanteType
                    + " et " + attributType);
        }
        Relation resultRelation = new Relation(nom + " selected", attributs);
        List<String> validOperators = Arrays.asList(valableoperator);
        Attribut element1 = cd.getElement1();
        Attribut element2 = cd.getElement2();
        Object constante = cd.getConstante();
        String operator = cd.getOperator();
        List<Integer> matchingIndexes = new ArrayList<>();
        if (element1 != null && constante != null &&
                validOperators.contains(operator)) {
            Integer attributeIndex = findAttributeIndex(resultRelation.attributs, element1.getNom());
            Object[] attributeValues = resultRelation.attributs[attributeIndex].getValeurs();
            for (int i = 0; i < attributeValues.length; i++) {
                if (attributeValues[i] != null && compare(attributeValues[i], constante, operator, constanteType)) {
                    matchingIndexes.add(i);
                }
            }
        } else if (element1 != null && element2 != null &&
                validOperators.contains(operator)) {
            int attributeIndex1 = findAttributeIndex(resultRelation.attributs, element1.getNom());
            int attributeIndex2 = findAttributeIndex(resultRelation.attributs, element2.getNom());
            Object[] values1 = resultRelation.attributs[attributeIndex1].getValeurs();
            Object[] values2 = resultRelation.attributs[attributeIndex2].getValeurs();
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

        for (int index : matchingIndexes) {
            Object[] values = new Object[resultRelation.attributs.length];
            for (int i = 0; i < resultRelation.attributs.length; i++) {
                if (index < resultRelation.attributs[i].getValeurs().length) {
                    values[i] = resultRelation.attributs[i].getValeurs()[index];
                }
            }
            Individu individu = new Individu(resultRelation.attributs);
            individu.setValeurs(values);
            resultRelation.pushIndividu(individu);

        }

        // initializeAttByIndividu(resultRelation);
        return resultRelation;
    }

    private int findAttributeIndex(Attribut[] attributs, String attributeName)
            throws Myexception {
        for (int i = 0; i < attributs.length; i++) {
            if (attributs[i].getNom().equalsIgnoreCase(attributeName)) {
                return i;
            }
        }
        throw new Myexception("Attribut non trouvé: " + attributeName);
    }

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
                } else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                    List<Class<?>> typList = new ArrayList<>();
                    typList.add(Double.class);
                    typList.add(Integer.class);
                    if (typList.contains(obj1.getClass()) && typList.contains(obj2.getClass())) {
                        double val1 = Double.parseDouble(obj1.toString());
                        double val2 = Double.parseDouble(obj2.toString());
                        return evaluateNumericalComparison(val1, val2, operator);
                    }
                    if (obj1.getClass().equals(LocalDate.class) && obj2.getClass().equals(LocalDate.class)) {
                        LocalDate d1 = (LocalDate) obj1;
                        LocalDate d2 = (LocalDate) obj2;

                        return evaluateLocalDateComparison(d1, d2, operator);
                    }

                } else {
                    return false;
                }
            default:
                return false;
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

    private boolean evaluateLocalDateComparison(LocalDate date1, LocalDate date2, String operator) {
        return switch (operator) {
            case ">" -> date1.isAfter(date2);
            case ">=" -> date1.isAfter(date2) || date1.isEqual(date2);
            case "<" -> date1.isBefore(date2);
            case "<=" -> date1.isBefore(date2) || date1.isEqual(date2);
            case "==" -> date1.isEqual(date2);
            case "!=" -> !date1.isEqual(date2);
            default -> false;
        };
    }

    public Relation select(Condition[] conditions) throws Myexception, Exception {
        Relation resp = null;
        
        if (conditions.length == 1) {
            return select(conditions[0]);
        }
        for (int i = 0; i < conditions.length - 1; i++) {
            Condition cd = conditions[i];
            Condition cdplus = conditions[i + 1];
            Condition cdmoins = null;
            String liaison = cd.getLiaison();
            if (i > 0) {
                cdmoins = conditions[i - 1];
            }
            try {
               if (cd.ge) {
                
               }
                if (liaison.equalsIgnoreCase("and")) {
                    System.out.println("miditra and");
                    resp = select(cd);
                    resp = resp.select(cdplus);
                    resp.show();
                }
                if (liaison.equalsIgnoreCase("or")) {
                    System.out.println("miditra or");
                    if (resp != null) {
                        resp = resp.union(select(cdplus));
                    }
                    resp = select(cdplus);
                    resp.show();
                }
            } catch (Exception e) {
            }
        }
        return resp;
    }

    public Relation clone() {
        try {
            return (Relation) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // // jointure
    public Relation join(Attribut attribut1, Relation r2, Attribut attribut2) throws Myexception {
        Relation resp = this;
        Attribut att1 = null;
        Attribut att2 = null;
        for (Attribut attribut : resp.getAttributs()) {
            if (attribut.getNom().equalsIgnoreCase(attribut1.getNom())) {
                att1 = attribut;
            }
            if (attribut.getNom().equalsIgnoreCase(attribut2.getNom())) {

                att2 = attribut;
            }
        }
        Condition cd = new Condition(att1, "=", att2);
        resp = resp.select(cd);
        initializeAttByIndividu(resp);
        return resp;
    }

    // // tetha jointure
    public Relation join(Relation r2,
            Condition[] conditions) throws Myexception, Exception {
        Relation resp = produitCartesien(r2);
        return resp.select(conditions);
    }

    // // produit cartesien
    public Relation produitCartesien(Relation r2) throws Myexception {
        Individu[] r1Individus = getIndividus();
        Attribut[] r1Attributs = getAttributs();
        Individu[] r2Individus = r2.getIndividus();
        Attribut[] r2Attributs = r2.getAttributs();
        Attribut[] nouveauxAttributs = new Attribut[r1Attributs.length +
                r2Attributs.length];

        for (int i = 0; i < r1Attributs.length; i++) {
            nouveauxAttributs[i] = new Attribut(r1Attributs[i].getNom(),
                    r1Attributs[i].getDomaine());
        }

        for (int i = 0; i < r2Attributs.length; i++) {

            nouveauxAttributs[r1Attributs.length + i] = new Attribut(r2Attributs[i].getNom(),
                    r2Attributs[i].getDomaine());

        }

        Relation produitRelation = new Relation(nom, nouveauxAttributs);
        produitRelation.setNom("produit cartesienne entre (" + getNom() + " et " + r2.getNom() + ")");
        List<Object> valeursAtt = new ArrayList<>();
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
        produitRelation.SetRepetitionAtt();
        initializeAttByIndividu(produitRelation);
        return produitRelation;
    }

    public void SetRepetitionAtt() {
        List<String> nameAtt = new ArrayList<>();
        for (Attribut attribut : getAttributs()) {
            String attNom = attribut.getNom().toLowerCase();
            attribut.setNom(attNom);

            nameAtt.add(attNom);
        }
        removeTrait();
        nameAtt = nameAtt.stream().distinct().toList();
        HashMap<String, Integer> mapAtt = new HashMap<>();
        for (String string : nameAtt) {
            mapAtt.put(string, 0);
        }
        nameAtt = new ArrayList<>();
        for (Attribut attribut : getAttributs()) {
            String attNom = attribut.getNom().toLowerCase();
            attribut.setNom(attNom);
            if (nameAtt.contains(attNom)) {
                Integer repetition = mapAtt.get(attNom);
                repetition++;
                mapAtt.replace(attNom, repetition);
                attribut.setNom(attNom + "_" + mapAtt.get(attNom));

            }
            nameAtt.add(attNom);
        }
    }

    public void removeTrait() {
        for (int i = 0; i < attributs.length; i++) {

            String string = attributs[i].getNom();
            String[] explode = string.split("_");
            if (explode.length == 2) {
                string = explode[0];
                attributs[i].setNom(explode[0]);
            }
        }
    }

    public void renameAtt(Attribut[] attributs) {

    }

    public void initializeAttByIndividu(Relation rela) {
        Attribut[] att = rela.getAttributs();
        Individu[] indi = rela.getIndividus();
        if (att == null || indi == null) {
            throw new IllegalArgumentException("Attributes or Individuals cannot be null");
        }
        for (int j = 0; j < att.length; j++) {
            att[j].setValeurs(new Object[indi.length]);
            for (int i = 0; i < indi.length; i++) {
                Object[] individuValeurs = indi[i].getValeurs();
                att[j].getValeurs()[i] = individuValeurs[j];
            }
        }
    }

    public Relation projection(Attribut[] attributsProjection) throws Myexception {
        Attribut[] r1Attributs = getAttributs();
        Individu[] r1Individus = getIndividus();
        int can = 0;
        for (Attribut attrProj : attributsProjection) {
            for (Attribut attr : r1Attributs) {
                if (attr.getNom().equalsIgnoreCase(attrProj.getNom())) {
                    attrProj.setDomaine(attr.getDomaine());
                    can++;
                    break;
                }
            }
        }
        if (can == attributsProjection.length) {
            List<String> nameAtt = new ArrayList<>();
            for (Attribut att : attributsProjection) {
                nameAtt.add(att.getNom());
            }
            Relation pRelation = new Relation("projection de " + getNom() + ">>(" + nameAtt + ")", attributsProjection);

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

    public Relation difference(Relation r2) throws Myexception {
        if (r2 == null) {
            throw new Myexception("The second relation (r2) cannot be null.");
        }
        if (!checkAttribut(r2)) {
            throw new Myexception("Attributes of the two relations do not match.");
        }

        Set<Individu> r2IndividusSet = new HashSet<>(Arrays.asList(r2.getIndividus()));
        List<Individu> diffIndividus = new ArrayList<>();
        for (Individu r1Individu : getIndividus()) {
            if (!r2IndividusSet.contains(r1Individu)) {
                diffIndividus.add(r1Individu);
            }
        }
        Relation diffRelation = new Relation("diff(" + getNom() + "  " + r2.getNom() + ")", attributs);
        diffRelation.setIndividus(diffIndividus.toArray(new Individu[0]));
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
        Relation interRelation = new Relation("intersect(" + getNom() + "  " + r2.getNom() + ")", attributs);
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

    public Relation union(Relation r2) throws Myexception, Exception {
        Relation resp = (Relation) this.clone();
        Attribut[] r1Att = getAttributs();
        Attribut[] r2Att = r2.getAttributs();
        if (r1Att.length != r2Att.length) {
            throw new Myexception("Le nombre d'attribut de " + getNom() + "=  " + r1Att.length + " et  " + r2.getNom()
                    + "= " + r2Att.length + " doit etre le meme");
        }
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
                // r1Att[i].getDomaine().setNom(r1Att[i].getNom() + "_" + r2Att[i].getNom() +
                // "_Domaine");
                // r1Att[i].setNom(r1Att[i].getNom() + "_" + r2Att[i].getNom());
                r1Att[i].pushValeurs(r2Att[i].getValeurs());
            }
        }
        // System.out.println(Arrays.asList(r1Att[1].getDomaine().getEnsemble().getElements()));

        for (int i = 0; i < r2.getIndividus().length; i++) {
            resp.pushIndividu(r2.getIndividus()[i]);
        }
        resp.setNom("(" + resp.getNom() + ")_union_(" + r2.getNom() + ")");
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
