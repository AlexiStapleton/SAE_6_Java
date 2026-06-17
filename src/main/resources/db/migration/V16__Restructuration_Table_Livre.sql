-- Supprimer les colonnes qui remontent dans document
ALTER TABLE biblio.livre
DROP CONSTRAINT fk_livre_auteur,
    DROP CONSTRAINT fk_livre_editeur,
    DROP COLUMN auteur_id,
    DROP COLUMN editeur_id,
    DROP COLUMN titre,
    DROP COLUMN date_publication,
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

-- Ajouter les nouveaux champs
ALTER TABLE biblio.livre
    ADD COLUMN code_isbn VARCHAR(255);

-- Ajouter la FK vers document (livre.id = document.id)
ALTER TABLE biblio.livre
    ADD CONSTRAINT fk_livre_document FOREIGN KEY (id) REFERENCES biblio.document(id);

CREATE TABLE IF NOT EXISTS biblio.dvd (
    id    INTEGER NOT NULL PRIMARY KEY
        CONSTRAINT fk_dvd_document REFERENCES biblio.document(id),
    duree INTEGER
    );

-- ALTER TABLE biblio.dvd OWNER TO admin;