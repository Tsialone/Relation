
package aff;

import relation.Condition;
import relation.Relation;
import relation.Domaine;
import relation.Ensemble;
import relation.Individu;
import relation.Attribut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Arrays;

import javax.management.Attribute;

import exception.Myexception;

public class Main {
        public static void main(String[] args) throws Myexception, Exception {

                // ensemble
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
                // les attributs
                Attribut[] cjhAtt = new Attribut[] {
                                new Attribut("IdCours", new Domaine("IdCours_domaine", String.class, nom, null)),
                                new Attribut("Jour", new Domaine("Jour_domaine", String.class, null, null)),
                                new Attribut("Heure", new Domaine("Heure_domaine", String.class, null, null))

                };
                Attribut[] cenAtt = new Attribut[] {
                                new Attribut("idCours",
                                                new Domaine("idCours_domaine", String.class, null, null)),
                                new Attribut("idEtudiant",
                                                new Domaine("idEtudiant_domaine", Integer.class, null, null)),
                                new Attribut("Note",
                                                new Domaine("Note_domaine", String.class, null, null)),

                };
                Attribut[] enaAtt = new Attribut[] {
                                new Attribut("idEtudiant",
                                                new Domaine("idEtudiant_domaine", Integer.class, null, null)),
                                new Attribut("Nom",
                                                new Domaine("Nom_domaine", String.class, null, null)),
                                new Attribut("Adresse",
                                                new Domaine("Adresse_domaine", String.class, null, null)),
                };

                // les relations
                Relation cjh = new Relation("cjh", cjhAtt);
                cjh.insert(
                                new Individu[] {
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Archi"),
                                                                                new Attribut("Jour", "Lu"),
                                                                                new Attribut("Heure", "9h")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Algo"),
                                                                                new Attribut("Jour", "Ma"),
                                                                                new Attribut("Heure", "9h")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Algo"),
                                                                                new Attribut("Jour", "Ve"),
                                                                                new Attribut("Heure", "9h")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Syst"),
                                                                                new Attribut("Jour", "Ma"),
                                                                                new Attribut("Heure", "14h")
                                                                }

                                                )

                                });

                Relation cen = new Relation("cen", cenAtt);
                cen.insert(
                                new Individu[] {
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Archi"),
                                                                                new Attribut("idEtudiant", 100),
                                                                                new Attribut("Note", "A")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Archi"),
                                                                                new Attribut("idEtudiant", 300),
                                                                                new Attribut("Note", "A")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Syst"),
                                                                                new Attribut("idEtudiant", 100),
                                                                                new Attribut("Note", "B")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Syst"),
                                                                                new Attribut("idEtudiant", 200),
                                                                                new Attribut("Note", "A")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Syst"),
                                                                                new Attribut("idEtudiant", 300),
                                                                                new Attribut("Note", "B")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Algo"),
                                                                                new Attribut("idEtudiant", 100),
                                                                                new Attribut("Note", "C")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idCours", "Algo"),
                                                                                new Attribut("idEtudiant", 200),
                                                                                new Attribut("Note", "A")
                                                                }

                                                )
                                });
                Relation ena = new Relation("ena", enaAtt);
                ena.insert(
                                new Individu[] {
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idEtudiant", 100),
                                                                                new Attribut("Nom", "Toto"),
                                                                                new Attribut("Adresse", "Nice")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idEtudiant", 200),
                                                                                new Attribut("Nom", "Tata"),
                                                                                new Attribut("Adresse", "Paris")
                                                                }

                                                ),
                                                new Individu(
                                                                new Attribut[] {
                                                                                new Attribut("idEtudiant", 300),
                                                                                new Attribut("Nom", "Titi"),
                                                                                new Attribut("Adresse", "Rome")
                                                                }

                                                )

                                });

                cjh.show();

                Relation select = cen.select(
                                new Condition[] {
                                                new Condition(
                                                                new Attribut("Note"),
                                                                "=",
                                                                "A",
                                                                "or"),
                                                new Condition(
                                                                new Attribut("idEtudiant"),
                                                                ">=",
                                                                100,
                                                                "and"),
                                                new Condition(
                                                                new Attribut("idEtudiant"),
                                                                "=",
                                                                200,
                                                                "and"),

                                                new Condition(
                                                                new Attribut("idCours"),
                                                                "=",
                                                                "Syst"),
                                });
                System.out.println("valiny");
                select.show();

                Relation jointure = cjh.join(
                                cen,
                                new Condition[] {
                                                new Condition(
                                                                new Attribut("idCours_1"),
                                                                "=",
                                                                new Attribut("idCours"))
                                });
                        jointure.show();
        }
}