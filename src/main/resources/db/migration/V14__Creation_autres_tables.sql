CREATE TABLE IF NOT EXISTS biblio.bibliotheque (
    id INTEGER PRIMARY KEY,
    adresse_id INTEGER NOT NULL,
    nom VARCHAR(2552) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS bibliotheque_id_seq;

-- Alter the id column to use the sequence as default
ALTER TABLE bibliotheque
    ALTER COLUMN id SET DEFAULT nextval('bibliotheque_id_seq');

ALTER TABLE biblio.bibliotheque
    ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE bibliotheque_id_seq OWNED BY bibliotheque.id;

ALTER TABLE bibliotheque
    ADD CONSTRAINT fk_bibliotheque_adresse
        FOREIGN KEY (adresse_id)
            REFERENCES adresse(id);



CREATE TABLE IF NOT EXISTS biblio.type_evenement (
    id INTEGER PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS biblio.evenement(
    id INTEGER PRIMARY KEY,
    bibliotheque_id INTEGER NOT NULL,
    type_evenement_id INTEGER NOT NULL,
    nom VARCHAR(255) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS evenement_id_seq;
-- Alter the id column to use the sequence as default
ALTER TABLE evenement
    ALTER COLUMN id SET DEFAULT nextval('evenement_id_seq'),
ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE evenement_id_seq OWNED BY evenement.id;

ALTER TABLE evenement
ADD CONSTRAINT fk_evenement_bibliotheque
FOREIGN KEY (bibliotheque_id)
REFERENCES bibliotheque(id);

ALTER TABLE evenement
    ADD CONSTRAINT fk_evenement_type_evenement
        FOREIGN KEY (type_evenement_id)
            REFERENCES type_evenement(id);

CREATE TABLE IF NOT EXISTS biblio.genre_document (
    id INTEGER PRIMARY KEY,
    nom VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS biblio.code_raison (
    id INTEGER PRIMARY KEY,
    nom VARCHAR(255),
    description VARCHAR(255)
);