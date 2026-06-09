CREATE TABLE IF NOT EXISTS auteur (
    id INTEGER PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    nationalite VARCHAR(255),
    date_naissance DATE,
    date_deces DATE
);