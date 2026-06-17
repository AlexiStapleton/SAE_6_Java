

-- =====================================================
-- CLEAN SCHEMA (sécurité en test)
-- =====================================================
DROP TABLE IF EXISTS emprunt;
DROP TABLE IF EXISTS auteur_type_auteur;
DROP TABLE IF EXISTS dvd;
DROP TABLE IF EXISTS livre;
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS evenement;
DROP TABLE IF EXISTS type_evenement;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS editeur;
DROP TABLE IF EXISTS bibliotheque;
DROP TABLE IF EXISTS genre_document;
DROP TABLE IF EXISTS code_raison;
DROP TABLE IF EXISTS auteur;
DROP TABLE IF EXISTS adresse;
DROP TABLE IF EXISTS type_auteur;

DROP SEQUENCE IF EXISTS auteur_id_seq;
DROP SEQUENCE IF EXISTS livre_id_seq;
DROP SEQUENCE IF EXISTS document_id_seq;
DROP SEQUENCE IF EXISTS utilisateur_id_seq;
DROP SEQUENCE IF EXISTS bibliotheque_id_seq;
DROP SEQUENCE IF EXISTS evenement_id_seq;

CREATE TYPE role_utilisateur AS ENUM (
    'BIBLIOTHECAIRE',
    'EMPRUNTEUR'
    );

-- =====================================================
-- ADDRESS
-- =====================================================
CREATE TABLE adresse (
                                id INTEGER PRIMARY KEY,
                                rue VARCHAR(255),
                                code_postal VARCHAR(50),
                                ville VARCHAR(255)
);

CREATE SEQUENCE adresse_id_seq;
ALTER TABLE adresse ALTER COLUMN id SET DEFAULT NEXTVAL('adresse_id_seq');

-- =====================================================
-- AUTEUR
-- =====================================================
CREATE TABLE auteur (
                               id INTEGER PRIMARY KEY,
                               nom VARCHAR(255),
                               prenom VARCHAR(255),
                               nationalite VARCHAR(255),
                               date_naissance DATE,
                               date_deces DATE,
                               ville_naissance VARCHAR(255),
                               lien_wikipedia VARCHAR(255)
);

CREATE SEQUENCE auteur_id_seq;
ALTER TABLE auteur ALTER COLUMN id SET DEFAULT NEXTVAL('auteur_id_seq');

-- =====================================================
-- TYPE AUTEUR
-- =====================================================
CREATE TABLE type_auteur (
                                    id INTEGER PRIMARY KEY,
                                    nom VARCHAR(255) UNIQUE
);

CREATE TABLE auteur_type_auteur (
                                           auteur_id INTEGER,
                                           type_auteur_id INTEGER,
                                           PRIMARY KEY (auteur_id, type_auteur_id),
                                           FOREIGN KEY (auteur_id) REFERENCES auteur(id),
                                           FOREIGN KEY (type_auteur_id) REFERENCES type_auteur(id)
);

-- =====================================================
-- EDITEUR
-- =====================================================
CREATE TABLE editeur (
                                id INTEGER PRIMARY KEY,
                                nom VARCHAR(255) UNIQUE NOT NULL,
                                lien_site_web VARCHAR(255),
                                lien_wikipedia VARCHAR(255),
                                adresse_id INTEGER,
                                FOREIGN KEY (adresse_id) REFERENCES adresse(id)
);

-- =====================================================
-- BIBLIOTHEQUE
-- =====================================================
CREATE TABLE bibliotheque (
                                     id INTEGER PRIMARY KEY,
                                     nom VARCHAR(255),
                                     adresse_id INTEGER NOT NULL,
                                     FOREIGN KEY (adresse_id) REFERENCES adresse(id)
);

CREATE SEQUENCE bibliotheque_id_seq;
ALTER TABLE bibliotheque ALTER COLUMN id SET DEFAULT NEXTVAL('bibliotheque_id_seq');

-- =====================================================
-- UTILISATEUR + ENUM (H2-safe)
-- =====================================================
CREATE TABLE utilisateur (
                                    id INTEGER PRIMARY KEY,
                                    nom VARCHAR(255),
                                    prenom VARCHAR(255),
                                    email VARCHAR(255),
                                    date_naissance DATE,
                                    date_fin_abonnement DATE,
                                    numero_carte VARCHAR(50),
                                    role_utilisateur VARCHAR(30),
                                    hash_mot_de_passe VARCHAR(255),
                                    adresse_id INTEGER,
                                    FOREIGN KEY (adresse_id) REFERENCES adresse(id)
);

CREATE SEQUENCE utilisateur_id_seq;
ALTER TABLE utilisateur ALTER COLUMN id SET DEFAULT NEXTVAL('utilisateur_id_seq');

-- =====================================================
-- GENRE / RAISON
-- =====================================================
CREATE TABLE genre_document (
                                       id INTEGER PRIMARY KEY,
                                       nom VARCHAR(255)
);

CREATE SEQUENCE genre_document_id_seq;
ALTER TABLE genre_document ALTER COLUMN id SET DEFAULT NEXTVAL('genre_document_id_seq');

CREATE TABLE code_raison (
                                    id INTEGER PRIMARY KEY,
                                    nom VARCHAR(255),
                                    description VARCHAR(255)
);

CREATE SEQUENCE code_raison_id_seq;
ALTER TABLE code_raison ALTER COLUMN id SET DEFAULT NEXTVAL('code_raison_id_seq');

-- =====================================================
-- DOCUMENT (table centrale héritage)
-- =====================================================
CREATE TABLE document (
                                 id INTEGER PRIMARY KEY,
                                 auteur_id INTEGER,
                                 editeur_id INTEGER,
                                 bibliotheque_id INTEGER,
                                 genre_id INTEGER,
                                 code_raison_id INTEGER,
                                 titre VARCHAR(255),
                                 gif VARCHAR(255),
                                 format VARCHAR(255),
                                 description VARCHAR(255),
                                 date_acquisition DATE,
                                 date_publication DATE,
                                 code_emplacement VARCHAR(10),
                                 empruntable BOOLEAN DEFAULT TRUE,
                                 created_at DATE,
                                 updated_at DATE,

                                 FOREIGN KEY (auteur_id) REFERENCES auteur(id),
                                 FOREIGN KEY (editeur_id) REFERENCES editeur(id),
                                 FOREIGN KEY (bibliotheque_id) REFERENCES bibliotheque(id),
                                 FOREIGN KEY (genre_id) REFERENCES genre_document(id),
                                 FOREIGN KEY (code_raison_id) REFERENCES code_raison(id)
);

CREATE SEQUENCE document_id_seq;
ALTER TABLE document ALTER COLUMN id SET DEFAULT NEXTVAL('document_id_seq');

-- =====================================================
-- LIVRE (héritage)
-- =====================================================
CREATE TABLE livre (
                              id INTEGER PRIMARY KEY,
                              code_isbn VARCHAR(255),
                                nb_pages INTEGER,
                              FOREIGN KEY (id) REFERENCES document(id)
);

CREATE SEQUENCE livre_id_seq;
ALTER TABLE livre ALTER COLUMN id SET DEFAULT NEXTVAL('livre_id_seq');

-- =====================================================
-- DVD
-- =====================================================
CREATE TABLE dvd (
                            id INTEGER PRIMARY KEY,
                            duree INTEGER,
                            FOREIGN KEY (id) REFERENCES document(id)
);

-- =====================================================
-- EMPRUNT
-- =====================================================
CREATE TABLE emprunt (
                                utilisateur_id INTEGER,
                                document_id INTEGER,
                                date_creation DATE,
                                prolongation DATE,
                                PRIMARY KEY (utilisateur_id, document_id),
                                FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
                                FOREIGN KEY (document_id) REFERENCES document(id)
);

-- =====================================================
-- EVENEMENT
-- =====================================================
CREATE TABLE type_evenement (
                                       id INTEGER PRIMARY KEY,
                                       nom VARCHAR(255)
);

CREATE TABLE evenement (
                                  id INTEGER PRIMARY KEY,
                                  bibliotheque_id INTEGER,
                                  type_evenement_id INTEGER,
                                  nom VARCHAR(255),
                                  date_debut DATE,
                                  date_fin DATE,

                                  FOREIGN KEY (bibliotheque_id) REFERENCES bibliotheque(id),
                                  FOREIGN KEY (type_evenement_id) REFERENCES type_evenement(id)
);

CREATE SEQUENCE evenement_id_seq;
ALTER TABLE evenement ALTER COLUMN id SET DEFAULT NEXTVAL('evenement_id_seq');

-- =====================================================
-- ===================== DATA ==========================
-- =====================================================

-- ADRESSES
INSERT INTO adresse VALUES
                               (1,'10 rue Hugo','73000','Chambéry'),
                               (2,'5 avenue Alpes','38000','Grenoble');

-- AUTEURS
INSERT INTO auteur (id, nom, prenom, nationalite, date_naissance, date_deces, ville_naissance)
VALUES
    (1,'Hugo','Victor','FR','1802-02-26','1885-05-22','Paris'),
    (2,'Borges','Jorge','AR','1899-11-07','1986-01-04','Buenos Aires');

-- TYPES AUTEUR
INSERT INTO type_auteur VALUES (1,'Ecrivain');

INSERT INTO auteur_type_auteur VALUES
                                          (1,1),
                                          (2,1);

-- EDITEURS
INSERT INTO editeur VALUES
                               (1,'Garnier',NULL,NULL,1),
                               (2,'Lacroix',NULL,NULL,2);

-- BIBLIOTHEQUES
INSERT INTO bibliotheque VALUES
    (1,'Biblio Chambéry',1);

-- UTILISATEURS
INSERT INTO utilisateur VALUES
                                   (1,'Dupont','Alice','alice@mail.com','2000-01-01',NULL,'CARD001','EMPRUNTEUR','hash',1),
                                   (2,'Martin','Paul','paul@mail.com','1990-01-01',NULL,'CARD002','BIBLIOTHECAIRE','hash',1);

-- GENRES / RAISON
INSERT INTO genre_document VALUES (1,'Roman');
INSERT INTO code_raison VALUES (1,'ACHAT','Test');

-- DOCUMENTS
INSERT INTO document VALUES
                                (1,1,1,1,1,1,'Les Misérables',NULL,NULL,NULL,'2020-01-01','1862-01-01','A1',TRUE,CURRENT_DATE,CURRENT_DATE),
                                (2,2,2,1,1,1,'Fictions',NULL,NULL,NULL,'2020-01-01','1944-01-01','A2',TRUE,CURRENT_DATE,CURRENT_DATE);

-- LIVRES
INSERT INTO livre VALUES
                             (1,'ISBN1',450),
                             (2,'ISBN2', 320);

-- DVD
INSERT INTO dvd VALUES
    (2,120);

-- EMPRUNTS
INSERT INTO emprunt VALUES
    (1,1,CURRENT_DATE,NULL);

-- EVENEMENTS
INSERT INTO type_evenement VALUES (1,'Lecture');

INSERT INTO evenement VALUES
    (1,1,1,'Event test',CURRENT_DATE,CURRENT_DATE);


ALTER TABLE auteur ALTER COLUMN id RESTART WITH 3;
ALTER TABLE code_raison ALTER COLUMN id RESTART WITH 2;
ALTER TABLE editeur ALTER COLUMN id RESTART WITH 3;
ALTER TABLE evenement ALTER COLUMN id RESTART WITH 2;
ALTER TABLE genre_document ALTER COLUMN id RESTART WITH 2;
ALTER TABLE utilisateur ALTER COLUMN id RESTART WITH 3;
ALTER TABLE adresse ALTER COLUMN id RESTART WITH 3;
ALTER TABLE bibliotheque ALTER COLUMN id RESTART WITH 2;