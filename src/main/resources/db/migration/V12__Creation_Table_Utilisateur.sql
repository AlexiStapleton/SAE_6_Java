CREATE TYPE role_utilisateur AS ENUM (
    'BIBLIOTHECAIRE',
    'EMPRUNTEUR'
);


CREATE TABLE IF NOT EXISTS utilisateur (
    id INTEGER PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    date_naissance DATE NOT NULL,
    date_fin_abonnement DATE,
    numero_carte CHAR(10) NOT NULL,
    role_utilisateur role_utilisateur NOT NULL,
    hash_mot_de_passe VARCHAR(255) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS utilisateur_id_seq;

-- Alter the id column to use the sequence as default
ALTER TABLE utilisateur
    ALTER COLUMN id SET DEFAULT nextval('utilisateur_id_seq'),
    ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE utilisateur_id_seq OWNED BY utilisateur.id;
