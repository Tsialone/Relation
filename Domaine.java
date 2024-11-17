
package relation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exception.Myexception;

public class Domaine {

    String domaine;
    Class<?>[] valabletype = { String.class, Integer.class, Double.class, LocalDate.class };
    Class<?>[] types = new Class[0];
    Integer limit = null;
    Integer decimallimit = null;
    Ensemble ensemble;
    String nom;

    public Domaine(String nom, Class<?> type, Ensemble ensemble, Integer limit) throws Myexception {

        setEnsemble(ensemble);
        isValableType(type);
        setLimit(limit);
        setNom(nom);
        pushType(type);
        limited();
    }
    public void PushEnsembleElement (Object val) throws MatchException , Exception
    {   
        List<Object> EnsEle =  new ArrayList<>(Arrays.asList( ensemble.getElements()))  ;
        EnsEle.add(val);
        ensemble.setElements(EnsEle.toArray(new Object[0]));
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void pushType(Class<?> newClass) {
        if (types == null || newClass == null) {
            throw new IllegalArgumentException("Classes array or new class cannot be null.");
        }

        List<Class<?>> classList = new ArrayList<>(Arrays.asList(types));
        classList.add(newClass);
        types = classList.toArray(new Class<?>[0]);

    }

    public void limited() throws Myexception {
        if (limit != null && ensemble != null) {
            for (Object element : ensemble.getElements()) {
                if (element.toString().length() > limit) {
                    throw new Myexception("out of precision for " + element.toString() + " in domaine " + getNom()
                            + element.toString() + " > " + limit);
                }
            }
        }

    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public void setEnsemble(Ensemble ensemble) {
        this.ensemble = ensemble;
    }

    public Ensemble getEnsemble() {
        return ensemble;
    }

    public Integer getDecimallimit() {
        return decimallimit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void isValableType(Class<?> type) throws Myexception {
        if (!Arrays.asList(valabletype).contains(type)) {
            throw new Myexception(type + " not found");
        }

    }

    public static Class<?> quelType(Object objet) {
        Class<?>[] valabletype = { String.class, Integer.class, Double.class, LocalDate.class };
        try {
            if (objet instanceof String) {
                return valabletype[0];
            } else if (objet instanceof Integer) {
                return valabletype[1];
            } else if (objet instanceof Double) {
                return valabletype[2];
            } else if (objet instanceof LocalDate) {
                return valabletype[2];
            }
        } catch (Exception e) {
        }

        return null;
    }

    public Class<?>[] getType() {
        Set<Class<?>> seted = new HashSet<>(Arrays.asList(types));
        List<Class<?>> listed = new ArrayList<>(seted);
        return listed.toArray(new Class<?>[0]);
    }

}
