ALTER TABLE biblio.utilisateur
ADD COLUMN adresse_id INTEGER;

ALTER TABLE biblio.utilisateur
    ADD CONSTRAINT fk_utilisateur_adresse
    FOREIGN KEY (adresse_id)
        REFERENCES adresse(id);