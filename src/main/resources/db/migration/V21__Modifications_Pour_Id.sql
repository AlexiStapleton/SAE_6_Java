CREATE SEQUENCE genre_document_id_seq;
ALTER TABLE genre_document ALTER COLUMN id SET DEFAULT NEXTVAL('genre_document_id_seq');


CREATE SEQUENCE code_raison_id_seq;
ALTER TABLE code_raison ALTER COLUMN id SET DEFAULT NEXTVAL('code_raison_id_seq');

CREATE SEQUENCE dvd_id_seq;
ALTER TABLE dvd ALTER COLUMN id SET DEFAULT NEXTVAL('dvd_id_seq');

CREATE SEQUENCE type_evenement_id_seq;
ALTER TABLE type_evenement ALTER COLUMN id SET DEFAULT NEXTVAL('type_evenement_id_seq');