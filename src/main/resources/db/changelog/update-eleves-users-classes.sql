--liquibase formatted sql

--changeset wilfried:2
ALTER TABLE users ADD COLUMN nom VARCHAR(255) NOT NULL;

--changeset wilfried:3
ALTER TABLE users ADD COLUMN prenom VARCHAR(255);

--changeset wilfried:4
ALTER TABLE eleves MODIFY classe_id BIGINT NULL;

--changeset wilfried:5
ALTER TABLE eleves MODIFY nom_tuteur VARCHAR(100) NULL;

--changeset wilfried:6
ALTER TABLE eleves MODIFY telephone_tuteur VARCHAR(20) NULL;

--changeset wilfried:remove-unique-nom
ALTER TABLE classes DROP INDEX UKsughss7m3ahsfmr2frd0d7clx;
