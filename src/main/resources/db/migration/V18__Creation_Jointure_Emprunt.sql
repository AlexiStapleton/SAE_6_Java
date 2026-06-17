CREATE TABLE IF NOT EXISTS biblio.emprunt (
    utilisateur_id INTEGER NOT NULL CONSTRAINT fk_emprunt_utilisateur REFERENCES biblio.utilisateur(id),
    document_id INTEGER NOT NULL CONSTRAINT fk_emprunt_document REFERENCES biblio.document(id),
    date_creation DATE,
    prolongation DATE,
    PRIMARY KEY (utilisateur_id, document_id)
    );

ALTER TABLE biblio.emprunt OWNER TO admin;