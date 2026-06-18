CREATE SEQUENCE IF NOT EXISTS biblio.type_auteur_id_seq;

CREATE TABLE IF NOT EXISTS biblio.type_auteur (
    id INTEGER DEFAULT nextval('biblio.type_auteur_id_seq') NOT NULL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE
    );

ALTER SEQUENCE biblio.type_auteur_id_seq OWNED BY biblio.type_auteur.id;
-- ALTER TABLE biblio.type_auteur OWNER TO admin;

CREATE TABLE IF NOT EXISTS biblio.auteur_type_auteur (
    auteur_id INTEGER NOT NULL CONSTRAINT fk_ata_auteur REFERENCES biblio.auteur(id),
    type_auteur_id INTEGER NOT NULL CONSTRAINT fk_ata_type_auteur REFERENCES biblio.type_auteur(id),
    PRIMARY KEY (auteur_id, type_auteur_id)
    );

-- ALTER TABLE biblio.auteur_type_auteur OWNER TO admin;