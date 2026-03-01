-- liquibase formatted sql

-- changeset ASSAM:1771794842073-1 splitStatements:false
CREATE TABLE classes (id BIGINT AUTO_INCREMENT NOT NULL, niveau VARCHAR(50) NOT NULL, nom VARCHAR(50) NOT NULL, professeur_principal VARCHAR(100) NULL, CONSTRAINT PK_CLASSES PRIMARY KEY (id), UNIQUE (nom));

-- changeset ASSAM:1771794842073-2 splitStatements:false
CREATE TABLE eleves (id BIGINT AUTO_INCREMENT NOT NULL, antecedents_medicaux VARCHAR(500) NULL, commentaire VARCHAR(500) NULL, date_inscription datetime(6) NOT NULL, date_naissance date NOT NULL, etat_sante VARCHAR(500) NULL, lieu_naissance VARCHAR(100) NOT NULL, matricule VARCHAR(30) NOT NULL, nom VARCHAR(50) NOT NULL, nom_tuteur VARCHAR(100) NOT NULL, prenom VARCHAR(50) NOT NULL, quartier VARCHAR(100) NOT NULL, redoublant BIT(1) NOT NULL, sexe ENUM('FEMININ', 'MASCULIN') NOT NULL, statut ENUM('ACTIF', 'ARCHIVE') NOT NULL, telephone_tuteur VARCHAR(20) NOT NULL, classe_id BIGINT NOT NULL, CONSTRAINT PK_ELEVES PRIMARY KEY (id), UNIQUE (matricule));

-- changeset ASSAM:1771794842073-3 splitStatements:false
CREATE TABLE liste_personnalisee_eleves (liste_id BIGINT NOT NULL, eleve_id BIGINT NOT NULL);

-- changeset ASSAM:1771794842073-4 splitStatements:false
CREATE TABLE listes_personnalisees (id BIGINT AUTO_INCREMENT NOT NULL, date_creation datetime(6) NOT NULL, nom VARCHAR(100) NOT NULL, CONSTRAINT PK_LISTES_PERSONNALISEES PRIMARY KEY (id));

-- changeset ASSAM:1771794842073-5 splitStatements:false
CREATE TABLE logs (id BIGINT AUTO_INCREMENT NOT NULL, created_date datetime(6) NOT NULL, field_value VARCHAR(255) NOT NULL, new_value VARCHAR(255) NOT NULL, old_value VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL, eleve_id BIGINT NOT NULL, CONSTRAINT PK_LOGS PRIMARY KEY (id));

-- changeset ASSAM:1771794842073-6 splitStatements:false
CREATE TABLE paiements (id BIGINT AUTO_INCREMENT NOT NULL, commentaire VARCHAR(255) NULL, statut ENUM('NON_PAYE', 'PARTIEL', 'PAYE') NOT NULL, eleve_id BIGINT NOT NULL, CONSTRAINT PK_PAIEMENTS PRIMARY KEY (id), UNIQUE (eleve_id));

-- changeset ASSAM:1771794842073-7 splitStatements:false
CREATE TABLE password_reset_tokens (id BIGINT AUTO_INCREMENT NOT NULL, expiration datetime(6) NOT NULL, token VARCHAR(200) NOT NULL, used BIT(1) NOT NULL, username VARCHAR(255) NOT NULL, CONSTRAINT PK_PASSWORD_RESET_TOKENS PRIMARY KEY (id), UNIQUE (token));

-- changeset ASSAM:1771794842073-8 splitStatements:false
CREATE TABLE refresh_token (id BIGINT AUTO_INCREMENT NOT NULL, expiration datetime(6) NOT NULL, revoked BIT(1) NOT NULL, token VARCHAR(255) NOT NULL, user_id BIGINT NOT NULL, CONSTRAINT PK_REFRESH_TOKEN PRIMARY KEY (id), UNIQUE (token), UNIQUE (user_id));

-- changeset ASSAM:1771794842073-9 splitStatements:false
CREATE TABLE users (id BIGINT AUTO_INCREMENT NOT NULL, enabled BIT(1) NOT NULL, password VARCHAR(255) NOT NULL, `role` ENUM('ADMINISTRATEUR', 'DIRECTEUR', 'ECONOME', 'SECRETAIRE') NOT NULL, username VARCHAR(255) NOT NULL, CONSTRAINT PK_USERS PRIMARY KEY (id), UNIQUE (username));

-- changeset ASSAM:1771794842073-10 splitStatements:false
CREATE INDEX FK4ts0411rjub6bl0bi8dt1yheb ON logs(eleve_id);

-- changeset ASSAM:1771794842073-11 splitStatements:false
CREATE INDEX FK9lbv50k0cfeemed9kgtmf5oj7 ON liste_personnalisee_eleves(eleve_id);

-- changeset ASSAM:1771794842073-12 splitStatements:false
CREATE INDEX FKb0n77om330mc4un5hnubl36bk ON eleves(classe_id);

-- changeset ASSAM:1771794842073-13 splitStatements:false
CREATE INDEX FKfr7ir4pl2hprduabpwye670fp ON liste_personnalisee_eleves(liste_id);

-- changeset ASSAM:1771794842073-14 splitStatements:false
ALTER TABLE logs ADD CONSTRAINT FK4ts0411rjub6bl0bi8dt1yheb FOREIGN KEY (eleve_id) REFERENCES eleves (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset ASSAM:1771794842073-15 splitStatements:false
ALTER TABLE liste_personnalisee_eleves ADD CONSTRAINT FK9lbv50k0cfeemed9kgtmf5oj7 FOREIGN KEY (eleve_id) REFERENCES eleves (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset ASSAM:1771794842073-16 splitStatements:false
ALTER TABLE eleves ADD CONSTRAINT FKb0n77om330mc4un5hnubl36bk FOREIGN KEY (classe_id) REFERENCES classes (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset ASSAM:1771794842073-17 splitStatements:false
ALTER TABLE liste_personnalisee_eleves ADD CONSTRAINT FKfr7ir4pl2hprduabpwye670fp FOREIGN KEY (liste_id) REFERENCES listes_personnalisees (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset ASSAM:1771794842073-18 splitStatements:false
ALTER TABLE paiements ADD CONSTRAINT FKj15g862j6xlnrf88qeajnalxs FOREIGN KEY (eleve_id) REFERENCES eleves (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset ASSAM:1771794842073-19 splitStatements:false
ALTER TABLE refresh_token ADD CONSTRAINT FKjtx87i0jvq2svedphegvdwcuy FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

