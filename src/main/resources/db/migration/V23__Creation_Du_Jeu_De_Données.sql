-- =============================================================
--  SEED COMPLET — schéma biblio
--  Ordre : tables sans FK d'abord, puis dépendants
-- =============================================================

-- -------------------------------------------------------------
-- 1. ADRESSE
-- -------------------------------------------------------------
INSERT INTO biblio.adresse (rue, code_postal, ville) VALUES
                                                         ('12 rue Montorgueil',       '75002', 'Paris'),
                                                         ('3 allée des Acacias',      '69001', 'Lyon'),
                                                         ('7 place du Capitole',      '31000', 'Toulouse'),
                                                         ('22 rue de la Paix',        '06000', 'Nice'),
                                                         ('45 boulevard Victor-Hugo', '13001', 'Marseille'),
                                                         ('8 rue des Libraires',      '67000', 'Strasbourg'),
                                                         ('1 avenue Foch',            '59000', 'Lille'),
                                                         ('17 rue Jeanne-d''Arc',     '76000', 'Rouen');
-- IDs attendus : 1..8


-- -------------------------------------------------------------
-- 2. AUTEUR
-- -------------------------------------------------------------
-- Les 3 auteurs déjà insérés (id 1,2,3) → on les complète
UPDATE biblio.auteur SET
    ville_naissance  = 'Besançon',
    lien_wikipedia   = 'https://fr.wikipedia.org/wiki/Victor_Hugo'
WHERE id = 1;

UPDATE biblio.auteur SET
    ville_naissance  = 'Buenos Aires',
    lien_wikipedia   = 'https://fr.wikipedia.org/wiki/Jorge_Luis_Borges'
WHERE id = 2;

UPDATE biblio.auteur SET
    ville_naissance  = 'Langres',
    lien_wikipedia   = 'https://fr.wikipedia.org/wiki/Denis_Diderot'
WHERE id = 3;

-- Nouveaux auteurs
INSERT INTO biblio.auteur (nom, prenom, nationalite, date_naissance, date_deces, ville_naissance, lien_wikipedia) VALUES
('Flaubert',  'Gustave',   'Français',    '1821-12-12', '1880-05-08', 'Rouen',         'https://fr.wikipedia.org/wiki/Gustave_Flaubert'),
('Zola',      'Émile',     'Français',    '1840-04-02', '1902-09-29', 'Paris',          'https://fr.wikipedia.org/wiki/%C3%89mile_Zola'),
('Stendhal',  NULL,        'Français',    '1783-01-23', '1842-03-23', 'Grenoble',       'https://fr.wikipedia.org/wiki/Stendhal'),
('Camus',     'Albert',    'Français',    '1913-11-07', '1960-01-04', 'Mondovi',        'https://fr.wikipedia.org/wiki/Albert_Camus'),
('Dumas',     'Alexandre', 'Français',    '1802-07-24', '1870-12-05', 'Villers-Cotterêts', 'https://fr.wikipedia.org/wiki/Alexandre_Dumas');
-- IDs attendus : 4..8


-- -------------------------------------------------------------
-- 3. TYPE_AUTEUR
-- -------------------------------------------------------------
INSERT INTO biblio.type_auteur (nom) VALUES
('Romancier'),
('Poète'),
('Philosophe'),
('Dramaturge'),
('Nouvelliste');
-- IDs : 1..5


-- -------------------------------------------------------------
-- 4. AUTEUR_TYPE_AUTEUR  (table de liaison)
-- -------------------------------------------------------------
INSERT INTO biblio.auteur_type_auteur (auteur_id, type_auteur_id) VALUES
(1, 1), -- Hugo      → Romancier
(1, 2), -- Hugo      → Poète
(1, 4), -- Hugo      → Dramaturge
(2, 5), -- Borges    → Nouvelliste
(3, 3), -- Diderot   → Philosophe
(4, 1), -- Flaubert  → Romancier
(5, 1), -- Zola      → Romancier
(6, 1), -- Stendhal  → Romancier
(7, 1), -- Camus     → Romancier
(7, 3), -- Camus     → Philosophe
(8, 1), -- Dumas     → Romancier
(8, 4); -- Dumas     → Dramaturge


-- -------------------------------------------------------------
-- 5. EDITEUR  (adresse_id 1..6)
-- -------------------------------------------------------------
-- Les 3 existants (insérés par la migration depuis livre.editeur)
-- A. Lacroix → id 1, Sur → id 2, Garnier → id 3
UPDATE biblio.editeur SET
    lien_site_web   = NULL,
    lien_wikipedia  = 'https://fr.wikipedia.org/wiki/Albert_Lacroix_(éditeur)',
    adresse_id      = 1
WHERE nom = 'A. Lacroix';

UPDATE biblio.editeur SET
    lien_site_web   = NULL,
    lien_wikipedia  = NULL,
    adresse_id      = 2
WHERE nom = 'Sur';

UPDATE biblio.editeur SET
    lien_site_web   = NULL,
    lien_wikipedia  = 'https://fr.wikipedia.org/wiki/Librairie_Garnier',
    adresse_id      = 3
WHERE nom = 'Garnier';

-- Nouveaux éditeurs
INSERT INTO biblio.editeur (nom, lien_site_web, lien_wikipedia, adresse_id) VALUES
('Gallimard',      'https://www.gallimard.fr',      'https://fr.wikipedia.org/wiki/Gallimard',       4),
('Flammarion',     'https://www.flammarion.com',     'https://fr.wikipedia.org/wiki/Flammarion',      5),
('Le Livre de Poche', 'https://www.livredepoche.com', NULL,                                           6),
('Folio',          'https://www.folio-lesite.fr',    NULL,                                            7);
-- IDs : 4..7 (les 3 premiers sont ceux issus de la migration)


-- -------------------------------------------------------------
-- 6. GENRE_DOCUMENT
-- -------------------------------------------------------------
INSERT INTO biblio.genre_document (nom) VALUES
('Roman'),
('Nouvelle'),
('Poésie'),
('Philosophie'),
('Documentaire'),
('Jeunesse'),
('Bande dessinée'),
('Cinéma');
-- IDs : 1..8


-- -------------------------------------------------------------
-- 7. CODE_RAISON  (raison d'acquisition / déclassement)
-- -------------------------------------------------------------
    INSERT INTO biblio.code_raison (nom, description) VALUES
    ('ACHAT',     'Acquisition par achat direct'),
    ('DON',       'Document reçu en don'),
    ('ECHANGE',   'Échange inter-bibliothèques'),
    ('PILON',     'Document mis au pilon — usure'),
    ('PERDU',     'Document déclaré perdu par l''emprunteur');
-- IDs : 1..5


-- -------------------------------------------------------------
-- 8. BIBLIOTHEQUE  (adresse_id 5..8)
-- -------------------------------------------------------------
INSERT INTO biblio.bibliotheque (nom, adresse_id) VALUES
                                                      ('Bibliothèque Centrale de Lyon',    2),
                                                      ('Médiathèque du Capitole',          3),
                                                      ('Bibliothèque Universitaire Nice',  4),
                                                      ('Médiathèque de Marseille',         5);
-- IDs : 1..4


-- -------------------------------------------------------------
-- 9. TYPE_EVENEMENT
-- -------------------------------------------------------------
INSERT INTO biblio.type_evenement (nom) VALUES
                                            ('Atelier lecture'),
                                            ('Conférence'),
                                            ('Exposition'),
                                            ('Club de lecture'),
                                            ('Dédicace');
-- IDs : 1..5


-- -------------------------------------------------------------
-- 10. EVENEMENT
-- -------------------------------------------------------------
INSERT INTO biblio.evenement (bibliotheque_id, type_evenement_id, nom, date_debut, date_fin) VALUES
                                                                                                 (1, 1, 'Atelier Hugo — Les Misérables',             '2025-02-10', '2025-02-10'),
                                                                                                 (1, 4, 'Club polar — séance mensuelle',              '2025-03-05', '2025-03-05'),
                                                                                                 (2, 2, 'Conférence : Zola et le naturalisme',        '2025-04-12', '2025-04-12'),
                                                                                                 (2, 3, 'Exposition : L''art de la couverture',        '2025-04-15', '2025-04-30'),
                                                                                                 (3, 5, 'Dédicace — auteurs niçois',                  '2025-05-20', '2025-05-20'),
                                                                                                 (4, 1, 'Atelier écriture créative',                  '2025-06-03', '2025-06-03'),
                                                                                                 (1, 2, 'Les grandes utopies littéraires',            '2025-09-18', '2025-09-18'),
                                                                                                 (3, 4, 'Club SF — Verne & Cie',                      '2025-10-07', '2025-10-07');


-- -------------------------------------------------------------
-- 11. DOCUMENT  (les 3 premiers existent déjà via la migration)
--     document.id 1 = Les Misérables, 2 = Fictions, 3 = Jacques le fataliste
-- -------------------------------------------------------------

-- Compléter les 3 documents migrés
UPDATE biblio.document SET
                           bibliotheque_id  = 1,
                           genre_id         = 1,
                           code_raison_id   = 1,
                           description      = 'Chef-d''œuvre du roman social du XIXe siècle.',
                           date_acquisition = '2010-06-01',
                           code_emplacement = 'ROM-HUG-01',
                           empruntable      = TRUE
WHERE id = 1;

UPDATE biblio.document SET
                           bibliotheque_id  = 2,
                           genre_id         = 2,
                           code_raison_id   = 1,
                           description      = 'Recueil de nouvelles fantastiques de Borges.',
                           date_acquisition = '2012-03-15',
                           code_emplacement = 'NOU-BOR-01',
                           empruntable      = TRUE
WHERE id = 2;

UPDATE biblio.document SET
                           bibliotheque_id  = 3,
                           genre_id         = 4,
                           code_raison_id   = 2,
                           description      = 'Roman dialogué, reflet des idées des Lumières.',
                           date_acquisition = '2008-11-20',
                           code_emplacement = 'PHI-DID-01',
                           empruntable      = TRUE
WHERE id = 3;

-- Nouveaux documents (livres et DVDs)
INSERT INTO biblio.document
(auteur_id, bibliotheque_id, genre_id, code_raison_id, editeur_id,
 titre, date_publication, date_acquisition, code_emplacement, empruntable,
 description, created_at, updated_at)
VALUES
-- Livres
(4, 1, 1, 1, 4,  'Madame Bovary',         '1857-04-01', '2015-01-10', 'ROM-FLA-01', TRUE,  'Portrait d''une femme piégée par ses illusions romantiques.',        NOW(), NOW()),
(5, 2, 1, 1, 5,  'Germinal',              '1885-01-01', '2016-05-22', 'ROM-ZOL-01', TRUE,  'La misère des mineurs du Nord de la France au XIXe siècle.',         NOW(), NOW()),
(6, 1, 1, 1, 6,  'Le Rouge et le Noir',   '1830-11-01', '2014-09-08', 'ROM-STE-01', TRUE,  'Ascension sociale et passion d''un jeune ambitieux sous la Restauration.', NOW(), NOW()),
(7, 3, 1, 1, 4,  'L''Étranger',           '1942-06-01', '2013-02-14', 'ROM-CAM-01', TRUE,  'L''absurde incarné dans le personnage de Meursault.',                NOW(), NOW()),
(8, 4, 1, 1, 6,  'Les Trois Mousquetaires','1844-03-01', '2011-07-30', 'ROM-DUM-01', TRUE, 'Aventures de d''Artagnan et ses compagnons au service du roi.',       NOW(), NOW()),
(4, 2, 1, 3, 7,  'L''Éducation sentimentale','1869-11-17','2018-03-05','ROM-FLA-02', TRUE, 'Chronique de la génération de 1848 à travers Frédéric Moreau.',      NOW(), NOW()),
(7, 4, 4, 1, 4,  'Le Mythe de Sisyphe',   '1942-10-16', '2019-06-18', 'PHI-CAM-01', FALSE, 'Essai philosophique sur l''absurde — non empruntable (référence).', NOW(), NOW()),
-- DVDs
(NULL, 1, 8, 1, NULL, 'Germinal (film 1993)',  '1993-09-29', '2020-02-01', 'DVD-CIN-01', TRUE,  'Adaptation de Claude Berri du roman de Zola.',  NOW(), NOW()),
(NULL, 3, 8, 1, NULL, 'Les Misérables (film 2012)', '2012-12-07', '2021-04-15', 'DVD-CIN-02', TRUE, 'Comédie musicale de Tom Hooper, adapté du roman de Hugo.', NOW(), NOW());
-- IDs attendus : 4..12


-- -------------------------------------------------------------
-- 12. LIVRE  (id = document.id — spécialisation JOINED)
-- -------------------------------------------------------------
-- Les 3 livres existants n'ont pas encore de code_isbn → on les ajoute
UPDATE biblio.livre SET code_isbn = '978-2-07-040850-4' WHERE id = 1;
UPDATE biblio.livre SET code_isbn = '978-0-8021-3941-4' WHERE id = 2;
UPDATE biblio.livre SET code_isbn = '978-2-07-036135-3' WHERE id = 3;

INSERT INTO biblio.livre (id, code_isbn) VALUES
                                             (4,  '978-2-07-036024-0'),  -- Madame Bovary
                                             (5,  '978-2-07-040607-4'),  -- Germinal
                                             (6,  '978-2-07-036129-2'),  -- Le Rouge et le Noir
                                             (7,  '978-2-07-036024-7'),  -- L'Étranger
                                             (8,  '978-2-07-036125-4'),  -- Les Trois Mousquetaires
                                             (9,  '978-2-07-041025-5'),  -- L'Éducation sentimentale
                                             (10, '978-2-07-032888-2');  -- Le Mythe de Sisyphe


-- -------------------------------------------------------------
-- 13. DVD
-- -------------------------------------------------------------
INSERT INTO biblio.dvd (id, duree) VALUES
                                       (11, 158),  -- Germinal (film) : 2h38
                                       (12, 158);  -- Les Misérables (film) : 2h38


-- -------------------------------------------------------------
-- 14. UTILISATEUR
-- -------------------------------------------------------------
INSERT INTO biblio.utilisateur
(nom, prenom, email, date_naissance, date_fin_abonnement,
 numero_carte, role_utilisateur, hash_mot_de_passe, adresse_id)
VALUES
    ('Dupont',   'Marie',    'marie.dupont@mail.fr',    '1990-04-23', '2026-01-31', 'BIB0000001', 'BIBLIOTHECAIRE', '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 1),
    ('Martin',   'Paul',     'paul.martin@mail.fr',     '1985-11-07', '2025-12-31', 'BIB0000002', 'EMPRUNTEUR',     '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 2),
    ('Bernard',  'Sophie',   'sophie.bernard@mail.fr',  '1998-06-15', '2025-09-30', 'BIB0000003', 'EMPRUNTEUR',     '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 3),
    ('Leclerc',  'Antoine',  'antoine.leclerc@mail.fr', '1975-02-28', '2026-03-31', 'BIB0000004', 'EMPRUNTEUR',     '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 4),
    ('Moreau',   'Isabelle', 'isa.moreau@mail.fr',      '2001-08-10', '2025-06-30', 'BIB0000005', 'EMPRUNTEUR',     '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 5),
    ('Petit',    'Thomas',   'thomas.petit@mail.fr',    '1993-12-01', '2026-06-30', 'BIB0000006', 'BIBLIOTHECAIRE', '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 6),
    ('Roux',     'Camille',  'camille.roux@mail.fr',    '2003-03-19', '2025-11-30', 'BIB0000007', 'EMPRUNTEUR',     '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 7),
    ('Garnier',  'Lucie',    'lucie.garnier@mail.fr',   '1988-07-04', '2026-02-28', 'BIB0000008', 'EMPRUNTEUR',     '$2a$10$m/kiA2E6C3TWGcUriFsVY.bGYP4k2F0TezF6J.aTW38jgkFVxqxHW', 8);
-- IDs : 1..8


-- -------------------------------------------------------------
-- 15. EMPRUNT
--     Règle métier : emprunteur (role = EMPRUNTEUR) uniquement
--     date_fin = date_creation + 28 jours (règle bibliothèque)
-- -------------------------------------------------------------
INSERT INTO biblio.emprunt (utilisateur_id, document_id, date_creation, date_fin, prolongation) VALUES
-- Emprunts terminés (rendus)
(2, 1,  '2025-01-05', '2025-02-02', NULL),          -- Paul    → Les Misérables
(3, 4,  '2025-01-10', '2025-02-07', NULL),          -- Sophie  → Madame Bovary
(4, 7,  '2025-02-01', '2025-03-01', NULL),          -- Antoine → L'Étranger
(5, 2,  '2025-02-14', '2025-03-14', NULL),          -- Isabelle→ Fictions

-- Emprunts en cours
(2, 5,  '2025-04-01', '2025-04-29', NULL),          -- Paul    → Germinal
(3, 6,  '2025-04-10', '2025-05-08', NULL),          -- Sophie  → Le Rouge et le Noir
(4, 8,  '2025-04-15', '2025-05-13', NULL),          -- Antoine → Les Trois Mousquetaires
(7, 11, '2025-04-20', '2025-05-18', NULL),          -- Camille → DVD Germinal

-- Emprunt avec prolongation
(8, 3,  '2025-03-01', '2025-03-29', '2025-04-26'),  -- Lucie   → Jacques le fataliste (prolongé)
(5, 9,  '2025-03-18', '2025-04-15', '2025-05-13'),  -- Isabelle→ L'Éducation sentimentale (prolongé)

-- Emprunt en retard (date_fin dépassée, non rendu)
(2, 12, '2025-02-20', '2025-03-20', NULL);          -- Paul    → DVD Les Misérables (en retard)