import java.util.Arrays;

public class Main {

        public static void main(String[] args) throws Myexception {
               // attribut cjh
                Attribut[] cjhpAtt = {
                                new Attribut("idCours", new Domaine("VARCHAR")),
                                new Attribut("jour", new Domaine("VARCHAR")),
                                new Attribut("heure", new Domaine("VARCHAR"))
                };

                // attribut cs
                Attribut[] csAtt = {
                                new Attribut("idCours", new Domaine("VARCHAR")),
                                new Attribut("idSalle", new Domaine("VARCHAR"))
                };
                // attribut ena
                Attribut[] enaAtt = {
                                new Attribut("idEtudiant", new Domaine("NUMBER")),
                                new Attribut("Nom", new Domaine("VARCHAR")),
                                new Attribut("Adresse", new Domaine("VARCHAR"))
                };
                // attribut cen
                Attribut[] cenAtt = {
                                new Attribut("idCours", new Domaine("VARCHAR")),
                                new Attribut("idEtudiant", new Domaine("NUMBER")),
                                new Attribut("Note", new Domaine("VARCHAR"))
                };

                // cjh
                Relation cjh = new Relation("cjh", cjhpAtt);
                Individu[] cjhIndividus = {
                                // individu 1
                                new Individu(
                                                cjhpAtt,
                                                new Object[] {
                                                                "Archi",
                                                                "Lu",
                                                                "9h"
                                                }),
                                // individu 2
                                new Individu(
                                                cjhpAtt,
                                                new Object[] {
                                                                "Algo",
                                                                "Ma",
                                                                "9h"
                                                }),
                                // individu 3
                                new Individu(
                                                cjhpAtt,
                                                new Object[] {
                                                                "Algo",
                                                                "Ve",
                                                                "9h"
                                                }),
                                // individu 4
                                new Individu(
                                                cjhpAtt,
                                                new Object[] {
                                                                "Syst",
                                                                "Ma",
                                                                "14h"
                                                }),
                };

                // cjh
                Relation cs = new Relation("cs", csAtt);
                Individu[] csindividus = {
                                // individu 1
                                new Individu(
                                                csAtt,
                                                new Object[] {
                                                                "Archi",
                                                                "S1",
                                                }),
                                // individu 2
                                new Individu(
                                                csAtt,
                                                new Object[] {
                                                                "Algo",
                                                                "S2",
                                                }),
                                // individu 3
                                new Individu(
                                                csAtt,
                                                new Object[] {
                                                                "Syst",
                                                                "S1"
                                                }),
                };
                // ena
                Relation ena = new Relation("ena", enaAtt);
                Individu[] enaindividus = {
                                // individu 1
                                new Individu(
                                                enaAtt,
                                                new Object[] {
                                                                100,
                                                                "Toto",
                                                                "Nice",

                                                }),
                                // individu 2
                                new Individu(
                                                enaAtt,
                                                new Object[] {
                                                                200,
                                                                "Tata",
                                                                "Paris",
                                                }),
                                // individu 3
                                new Individu(
                                                enaAtt,
                                                new Object[] {
                                                                300,
                                                                "Titi",
                                                                "Rome",
                                                }),
                };
                // cen
                Relation cena = new Relation("cena", cenAtt);
                Individu[] cenaindividu = {
                                // individu 1
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Archi",
                                                                100,
                                                                "A",
                                                }),
                                // individu 2
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Archi",
                                                                300,
                                                                "A",
                                                }),
                                // individu 3
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Syst",
                                                                100,
                                                                "B",
                                                }),
                                // individu 4
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Syst",
                                                                200,
                                                                "A",
                                                }),
                                // individu 5
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Syst",
                                                                300,
                                                                "B",
                                                }),
                                // individu 6
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Algo",
                                                                100,
                                                                "C",
                                                }),
                                // individu 7
                                new Individu(
                                                cenAtt,
                                                new Object[] {
                                                                "Algo",
                                                                200,
                                                                "A",
                                                }),
                };

                // insertion et initialisation
                cjh.setIndividus(cjhIndividus);
                cjh.initializeEnsemble();
                cs.setIndividus(csindividus);
                cs.initializeEnsemble();
                ena.setIndividus(enaindividus);
                ena.initializeEnsemble();
                cena.setIndividus(cenaindividu);
                cena.initializeEnsemble();

                cjh.show();
                cs.show();
                ena.show();
                cena.show();

                //projection cjh
                Relation projectioncjh = cjh.projection(new Attribut[]{
                        new Attribut("idcours" , new Domaine("varchar")),
                });
                projectioncjh.show();
                 //projection ena
                 Relation projectionena = ena.projection(new Attribut[]{
                        new Attribut("idetudiant" , new Domaine("NUMBER")),
                });
                projectionena.show();
                // selection cen where  idCours = "algo"
                Relation selectcen = cena.select(new Condition(
                        new Attribut ("idCours"  , new Domaine ("VARCHAR")),
                        "=",
                        "Algo"
                ));
                selectcen.show();

                //theta jointure 
                Relation jointurecjhcs  = cjh.join( 
                        new Attribut("cjh.idCours",new Domaine("VARCHAR")),
                         cs,
                          new Attribut ("cs.idCours" , new Domaine("VARCHAR"))
                        );
                jointurecjhcs.show();

                //Trouver les étudiants qui ont suivi un cours le lundi.
                Relation etudiantlundi = cjh.join(
                        new  Attribut("cjh.idCours" , new Domaine ("VARCHAR")) ,
                        cena,
                        new  Attribut("cena.idCours" , new Domaine ("VARCHAR")) ,
                        new Condition[]{
                                new Condition(
                                        new  Attribut("cjh.jour" , new Domaine ("VARCHAR")),
                                         "=",
                                         "lu")
                        }
                        );

               etudiantlundi.show();

               // Trouver la liste des étudiants ayant une note supérieure à 15 dans un cours
               Relation etudiantsup = ena.join(
                new  Attribut("ena.idetudiant" , new Domaine ("number")) ,
                cena,
                new  Attribut("cena.idetudiant" , new Domaine ("number")) ,
                new Condition[]{
                        new Condition(
                                new  Attribut("cena.note" , new Domaine ("VARCHAR")),
                                 "=",
                                 "B")
                }
                );
                etudiantsup.show();
                //Trouver les cours qui se déroulent dans la salle S1
                Relation courderoule = cjh.join(
                new  Attribut("cjh.idcours" , new Domaine ("varchar")) ,
                cs,
                new  Attribut("cs.idcours" , new Domaine ("varchar")) ,
                new Condition[]{
                        new Condition(
                                new  Attribut("cs.idsalle" , new Domaine ("VARCHAR")),
                                 "=",
                                 "s1")
                }
                );
                courderoule.show();

                //Trouver les salles dans lesquelles un cours a lieu le vendredi
                Relation courjour = cjh.join(
                new  Attribut("cjh.idcours" , new Domaine ("varchar")) ,
                cs,
                new  Attribut("cs.idcours" , new Domaine ("varchar")) ,
                new Condition[]{
                        new Condition(
                                new  Attribut("cjh.jour" , new Domaine ("VARCHAR")),
                                 "=",
                                 "ve")
                }
                ).projection(new Attribut[]{
                        new Attribut("cs.idsalle", new Domaine("varchar"))
                }
                );
                courjour.show();       
                //Donner les noms des étudiants qui suivent le cours 'Algo'
                Relation etudiantcour = cena.join(
                new  Attribut("cena.idetudiant" , new Domaine ("number")) ,
                ena,
                new  Attribut("ena.idetudiant" , new Domaine ("number")) ,
                new Condition[]{
                        new Condition(
                                new  Attribut("cena.idcours" , new Domaine ("VARCHAR")),
                                 "=",
                                 "algo")
                }
                ).projection(new Attribut[]{
                        new Attribut("ena.nom", new Domaine("varchar"))
                }
                );
                etudiantcour.show();   
                //Donner les notes en 'Archi' des étudiants dont le nom est 'Titi'
                Relation noteetudiant = cena.join(
                new  Attribut("cena.idetudiant" , new Domaine ("number")) ,
                ena,
                new  Attribut("ena.idetudiant" , new Domaine ("number")) ,
                new Condition[]{
                        new Condition(
                                new  Attribut("ena.nom" , new Domaine ("VARCHAR")),
                                 "=",
                                 "titi" , "and"),
                                 new Condition(
                                        new  Attribut("cena.idcours" , new Domaine ("VARCHAR")),
                                         "=",
                                         "Archi"),
                }
                );
                noteetudiant.show(); 
                //Donner les couples (jour, heure) pour lesquels la salle 'S1' est occupée par un cours
                Relation jhsalle = cjh.join(
                        new  Attribut("cjh.idcours" , new Domaine ("varchar")) ,
                        cs,
                        new  Attribut("cs.idcours" , new Domaine ("varchar")) ,
                        new Condition[]{
                                new Condition(
                                        new  Attribut("cs.idsalle" , new Domaine ("VARCHAR")),
                                         "=",
                                         "s1")
                        }
                        
                        );
                jhsalle.show();
                 //Donner les identiants des étudiants qui n'ont que des notes 'A'
                 Relation etudiannote = ena.join(
                        new  Attribut("ena.idetudiant" , new Domaine ("number")) ,
                        cena,
                        new  Attribut("cena.idetudiant" , new Domaine ("number")) ,
                        new Condition[]{
                                new Condition(
                                        new  Attribut("cena.note" , new Domaine ("VARCHAR")),
                                         "=",
                                         "A")
                        }
                        
                        );
                        etudiannote.show();

                //Donner la salle où se trouve 'Toto' le lundi à 9h
                Relation totoh = ena.join(
                        new  Attribut("ena.idetudiant" , new Domaine ("number")) ,
                        cena,
                        new  Attribut("cena.idetudiant" , new Domaine ("number")) ,
                        new Condition[]{
                                new Condition(
                                        new  Attribut("ena.nom" , new Domaine ("VARCHAR")),
                                         "=",
                                         "toto")
                        }
                        );
                        totoh.setNom("totoh");
                Relation totoh2 = totoh.join(
                         new Attribut("totoh.cena.idcours", new Domaine("varchar")) , 
                         cjh,
                         new Attribut("cjh.idcours", new Domaine("varchar"))
                         );
                        totoh2.show();


                // Relation projected = etudiantlundi.projection(new Attribut[]
                // {
                //         new Attribut("Note", new Domaine("varchar")),
                //         new Attribut("idetudiant", new Domaine("varchar")),
                // });



                // etudiantlundi.show();

                
        }
}