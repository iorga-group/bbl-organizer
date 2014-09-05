--liquibase formatted sql

--changeset vloisel-aogier:2

DROP TABLE ACHIEVED_SESSION;

CREATE TABLE SESSION_METADATA (
  ID BIGINT AUTO_INCREMENT,
  BAGGER_NAME VARCHAR(200) NOT NULL,
  TITLE VARCHAR (4000) NOT NULL,
  ACHIEVEMENT_DATE DATE,
  CREATION_DATE DATE NOT NULL DEFAULT CURRENT_DATE(),
  PLANNED_DATE DATE,
);
ALTER TABLE SESSION_METADATA ADD CONSTRAINT PK_SESSION_METADATA PRIMARY KEY (ID);
ALTER TABLE SESSION_METADATA ADD CONSTRAINT UNIQUE_SESSION_METADATA UNIQUE (BAGGER_NAME, TITLE);

INSERT INTO SESSION_METADATA(BAGGER_NAME, TITLE) SELECT DISTINCT BAGGER_NAME, SESSION_TITLE FROM VOTE;

ALTER TABLE VOTE ADD COLUMN ID_SESSION_METADATA BIGINT;
ALTER TABLE VOTE ADD FOREIGN KEY(ID_SESSION_METADATA) REFERENCES SESSION_METADATA(ID);

UPDATE VOTE v SET ID_SESSION_METADATA = (SELECT s.ID FROM SESSION_METADATA s WHERE s.BAGGER_NAME = v.BAGGER_NAME AND s.TITLE = v.SESSION_TITLE);

ALTER TABLE VOTE DROP CONSTRAINT UNIQUE_VOTE;
ALTER TABLE VOTE DROP COLUMN BAGGER_NAME;
ALTER TABLE VOTE DROP COLUMN SESSION_TITLE;

ALTER TABLE VOTE ADD CONSTRAINT UNIQUE_VOTE UNIQUE (ID_SESSION_METADATA, USER_NAME);
ALTER TABLE VOTE ALTER COLUMN ID_SESSION_METADATA SET NOT NULL;
COMMIT;