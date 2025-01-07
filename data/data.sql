DROP TABLE CJH;
DROP TABLE CS;
DROP TABLE ENA;
DROP TABLE CEN;



-- Création de la table CJH
CREATE TABLE CJH (
    IdCours VARCHAR2(20),
    Jour VARCHAR2(10),
    Heure VARCHAR2(10)
);

-- Création de la table CS
CREATE TABLE CS (
    IdCours VARCHAR2(20),
    IdSalle VARCHAR2(10)
);

-- Création de la table ENA
CREATE TABLE ENA (
    IdEtudiant NUMBER,
    Nom VARCHAR2(50),
    Adresse VARCHAR2(50)
);

-- Création de la table CEN
CREATE TABLE CEN (
    IdCours VARCHAR2(20),
    IdEtudiant NUMBER,
    Note CHAR(1)
);

-- Insertion des données dans la table CJH
INSERT INTO CJH (IdCours, Jour, Heure) VALUES ('Archi', 'Lu', '9h');
INSERT INTO CJH (IdCours, Jour, Heure) VALUES ('Algo', 'Ma', '9h');
INSERT INTO CJH (IdCours, Jour, Heure) VALUES ('Algo', 'Ve', '9h');
INSERT INTO CJH (IdCours, Jour, Heure) VALUES ('Syst', 'Ma', '14h');

-- Insertion des données dans la table CS
INSERT INTO CS (IdCours, IdSalle) VALUES ('Archi', 'S1');
INSERT INTO CS (IdCours, IdSalle) VALUES ('Algo', 'S2');
INSERT INTO CS (IdCours, IdSalle) VALUES ('Syst', 'S1');

-- Insertion des données dans la table ENA
INSERT INTO ENA (IdEtudiant, Nom, Adresse) VALUES (100, 'Toto', 'Nice');
INSERT INTO ENA (IdEtudiant, Nom, Adresse) VALUES (200, 'Tata', 'Paris');
INSERT INTO ENA (IdEtudiant, Nom, Adresse) VALUES (300, 'Titi', 'Rome');

-- Insertion des données dans la table CEN
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Archi', 100, 'A');
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Archi', 300, 'A');
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Syst', 100, 'B');
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Syst', 200, 'A');
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Syst', 300, 'B');
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Algo', 100, 'C');
INSERT INTO CEN (IdCours, IdEtudiant, Note) VALUES ('Algo', 200, 'A');
commit;


SELECT 
CJH.*  ,
CEN.* ,
CS.*
FROM CJH 
JOIN CEN ON CJH.IDCOURS = CEN.IDCOURS  JOIN  CS ON CJH.IDCOURS = CS.IDCOURS ; 


SELECT 
    CJH.IdCours AS IdCours_CJH, 
    CJH.Jour, 
    CJH.Heure, 
    CEN.IdCours AS IdCours_CEN, 
    CEN.IdEtudiant AS IdEtudiant_CEN, 
    CEN.Note AS Note_CEN
FROM 
    CJH, CEN  ;



SELECT 
    CJH.*, 
    CEN.*
FROM 
    CJH 
WHERE 
    CJH.IdCours = CEN.IdCours;

SELECT 
    * 
    FROM 
         CEN
    WHERE  
        NOTE = 'A'  OR IDETUDIANT >= 100  AND IDETUDIANT = 200; 