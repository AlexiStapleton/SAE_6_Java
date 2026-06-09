CREATE TABLE if not exists livre (
id integer,
titre varchar(250),
auteur varchar(250),
nb_pages integer,
editeur varchar(250),
date_publication date,
created_at timestamp,
updated_at timestamp,
PRIMARY KEY (id)
);