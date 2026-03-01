--liquibase formatted sql

--changeset auteur:ajout-archivage
ALTER TABLE eleves ADD COLUMN date_archivage TIMESTAMP;

ALTER TABLE eleves ADD COLUMN motif_archivage VARCHAR(20);