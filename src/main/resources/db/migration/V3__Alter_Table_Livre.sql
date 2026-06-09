ALTER TABLE livre
ADD COLUMN IF NOT EXISTS auteur_id INTEGER,
ADD CONSTRAINT fk_livre_auteur
    FOREIGN KEY (auteur_id)
    REFERENCES auteur(id);