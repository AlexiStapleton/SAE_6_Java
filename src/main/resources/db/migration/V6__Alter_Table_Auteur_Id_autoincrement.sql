-- Create a sequence for auto-incrementing id
CREATE SEQUENCE IF NOT EXISTS auteur_id_seq;

-- Alter the id column to use the sequence as default
ALTER TABLE auteur
    ALTER COLUMN id SET DEFAULT nextval('auteur_id_seq'),
    ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE auteur_id_seq OWNED BY auteur.id;

-- Add primary key constraint if not already present
-- ALTER TABLE auteur
--    ADD PRIMARY KEY (id);
