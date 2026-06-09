-- Inserer les nom d'éditeur existant dans la table
INSERT INTO biblio.editeur (nom)
SELECT DISTINCT editeur
FROM biblio.livre
WHERE editeur IS NOT NULL;

-- Ajouter une colonne editeur id dans livre
ALTER TABLE biblio.livre
ADD COLUMN editeur_id INTEGER;

-- On fait le lien entre les 2
UPDATE biblio.livre l
SET editeur_id = e.id
FROM biblio.editeur e
WHERE l.editeur = e.nom;

-- On créer la clé étrangère sur livre
ALTER TABLE biblio.livre
ADD CONSTRAINT fk_livre_editeur
FOREIGN KEY (editeur_id)
REFERENCES biblio.editeur(id);

-- Rends la colonne NOT NULL
ALTER TABLE biblio.livre
ALTER COLUMN editeur_id SET NOT NULL;

-- Supprimer la colonne editeur dans la table livre
ALTER TABLE biblio.livre
DROP COLUMN editeur;