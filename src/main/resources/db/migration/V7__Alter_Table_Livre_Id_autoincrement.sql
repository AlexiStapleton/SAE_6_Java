-- Create a sequence for auto-incrementing id
CREATE SEQUENCE IF NOT EXISTS livre_id_seq;

-- Alter the id column to use the sequence as default
ALTER TABLE livre
    ALTER COLUMN id SET DEFAULT nextval('livre_id_seq'),
    ALTER COLUMN id SET NOT NULL;

-- Set the sequence's ownership to the id column
ALTER SEQUENCE livre_id_seq OWNED BY livre.id;