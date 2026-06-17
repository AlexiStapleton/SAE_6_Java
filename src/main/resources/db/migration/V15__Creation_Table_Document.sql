CREATE SEQUENCE IF NOT EXISTS biblio.document_id_seq;

CREATE TABLE IF NOT EXISTS biblio.document (
    id               INTEGER DEFAULT nextval('biblio.document_id_seq') NOT NULL PRIMARY KEY,
    auteur_id        INTEGER CONSTRAINT fk_document_auteur REFERENCES biblio.auteur,
    bibliotheque_id  INTEGER CONSTRAINT fk_document_bibliotheque REFERENCES biblio.bibliotheque,
    genre_id         INTEGER CONSTRAINT fk_document_genre_document REFERENCES biblio.genre_document,
    code_raison_id   INTEGER CONSTRAINT fk_document_code_raison REFERENCES biblio.code_raison,
    editeur_id       INTEGER CONSTRAINT fk_document_editeur REFERENCES biblio.editeur,
    titre            VARCHAR(255),
    gif              VARCHAR(255),
    format           VARCHAR(255),
    description      VARCHAR(255),
    date_acquisition DATE,
    date_publication DATE,
    code_emplacement CHAR(10),
    empruntable      BOOLEAN DEFAULT TRUE,
    created_at       DATE,
    updated_at       DATE
    );

ALTER SEQUENCE biblio.document_id_seq OWNED BY biblio.document.id;
-- ALTER TABLE biblio.document OWNER TO admin;

-- Migrer les livres existants vers document
-- On force les IDs pour que document.id = livre.id (indispensable pour JOINED)
INSERT INTO biblio.document (id, auteur_id, editeur_id, titre, date_publication, created_at, updated_at)
SELECT id, auteur_id, editeur_id, titre, date_publication, created_at, updated_at
FROM biblio.livre;

-- Resynchroniser la séquence après les insertions manuelles
SELECT setval('biblio.document_id_seq', (SELECT MAX(id) FROM biblio.document));