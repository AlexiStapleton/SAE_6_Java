CREATE TABLE IF NOT EXISTS editeur (
    id INTEGER PRIMARY KEY,
    adresse_id INTEGER,
    lien_site_web VARCHAR(255),
    lien_wikipedia VARCHAR(255)
);

CREATE SEQUENCE IF NOT EXISTS editeur_id_seq;

-- Alter the id column to use the sequence as default
ALTER TABLE editeur
    ALTER COLUMN id SET DEFAULT nextval('editeur_id_seq'),
ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE editeur_id_seq OWNED BY editeur.id;

ALTER TABLE editeur
    ADD COLUMN IF NOT EXISTS adresse_id INTEGER,
    ADD CONSTRAINT fk_editeur_adresse
    FOREIGN KEY (adresse_id)
    REFERENCES adresse(id);