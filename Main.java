
package aff;

import relation.Condition;
import relation.Relation;
import relation.Domaine;
import relation.Ensemble;
import relation.Individu;
import relation.Attribut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.management.Attribute;

import exception.Myexception;

public class Main {
        public static void main(String[] args) throws Myexception, Exception {

                Ensemble nom = new Ensemble(
                                "nom",
                                new Object[] {
                                                "momo",
                                                "popo",
                                                "zozo",
                                                "Rakoto"

                                });
                Ensemble enaAge = new Ensemble(
                                "enaAge",
                                new Object[] {
                                                1,
                                                2,
                                                5.7

                                });
                Ensemble age = new Ensemble(
                                "age",
                                new Object[] {
                                                12,
                                                14,
                                                3
                                });

                Attribut[] cjhAtt = new Attribut[] {
                                new Attribut("Nom", new Domaine("Nom_domaine", String.class, nom, 10)),
                                new Attribut("Age", new Domaine("Age_domaine", Integer.class, age, null)),
                                new Attribut("Salaire", new Domaine("Salaire_domaine", Double.class, null, null)),
                                new Attribut("Hiredate", new Domaine("Date_domaine", LocalDate.class, null, null))

                };
                Attribut[] enaAtt = new Attribut[] {
                                new Attribut("Date of Skadi",
                                                new Domaine("Date of Skadi_domaine", LocalDate.class, null, null)),
                                new Attribut("Feet", new Domaine("Feet_domaine", Double.class, enaAge, null)),
                                new Attribut("Nationality",
                                                new Domaine("Nationality_domaine", String.class, null, null)),
                                new Attribut("Number", new Domaine("Number_domaine", Integer.class, null, null))

                };

                System.out.println(cjhAtt[0].getDomaine().getType().length);
                Relation r1 = new Relation("cjh", cjhAtt);
                r1.insert(
                                new Individu[] {
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("Nom", "zozo"),
                                                                                new Attribut("Age", 3),
                                                                                new Attribut("Salaire", 30.0),
                                                                                new Attribut("Hiredate",
                                                                                                LocalDate.now()) }

                                                )
                                });

                Relation r2 = new Relation("ena", enaAtt);
                r2.insert(
                                new Individu[] {
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("Date of Skadi",
                                                                                                LocalDate.of(12, 2,
                                                                                                                12)),
                                                                                new Attribut("Feet", 5.7),
                                                                                new Attribut("Nationality", "French"),
                                                                                new Attribut("Number", 0341) }

                                                )
                                }

                );

                Relation union = r1.union(r2, "intersti");
                union.show();

                union.insert(

                                new Individu[] {
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("Nom",
                                                                                                "Caca sosy"),
                                                                                new Attribut("Age", 5),
                                                                                new Attribut("Salaire", "French"),
                                                                                new Attribut("Hiredate", 5) }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("Nom",
                                                                                                "Caca sosy"),
                                                                                new Attribut("Age", 5),
                                                                                new Attribut("Salaire", "French"),
                                                                                new Attribut("Hiredate", 0341) }

                                                )
                                }

                );

                Relation select = union.select(
                                new Condition[] {
                                                new Condition(
                                                                new Attribut("Salaire"),
                                                                "=",
                                                                "French", "and"),
                                                new Condition(
                                                                new Attribut("Hiredate"),
                                                                ">=",
                                                                255, "or"),
                                                new Condition(
                                                                new Attribut("Age"),
                                                                "=",
                                                                5.7)

                                });

                union.show();
                select.show();
                Relation pro = select.produitCartesien(union, "popo");
                Relation join  =  select.join(
                   new Attribut("cjh.Age")     ,
                 union,
                   new Attribut("selected cjh.Age")
                   );

                pro.show();
                join.show();

                // System.out.println( Arrays.asList(pro.getAttributs()[0].getValeurs()) );

              

        }
}