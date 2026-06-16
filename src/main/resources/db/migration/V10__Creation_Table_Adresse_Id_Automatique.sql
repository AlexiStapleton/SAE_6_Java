CREATE TABLE IF NOT EXISTS adresse (
    id INTEGER PRIMARY KEY,
    rue VARCHAR(255),
    code_postal VARCHAR(255),
    ville VARCHAR(255)
);

CREATE SEQUENCE IF NOT EXISTS adresse_id_seq;

-- Alter the id column to use the sequence as default
ALTER TABLE adresse
    ALTER COLUMN id SET DEFAULT nextval('adresse_id_seq'),
ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE adresse_id_seq OWNED BY adresse.id;