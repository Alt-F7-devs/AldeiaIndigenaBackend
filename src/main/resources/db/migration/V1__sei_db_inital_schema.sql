CREATE SCHEMA IF NOT EXISTS sei_db;

SET search_path TO sei_db;

CREATE TABLE IF NOT EXISTS sei_db.admin (
    login   INTEGER      NOT NULL,
    senha   VARCHAR(60)  NOT NULL,
    CONSTRAINT pk_admin  PRIMARY KEY (login),
    CONSTRAINT uq_admin_senha UNIQUE (senha)
    );

CREATE TABLE IF NOT EXISTS sei_db.aluno (
    id_aluno    INTEGER      NOT NULL,
    nome        VARCHAR(45)  NOT NULL,
    senha       VARCHAR(45)  NOT NULL,
    cgm         INTEGER      NOT NULL,
    admin_login INTEGER      NOT NULL,
    CONSTRAINT pk_aluno       PRIMARY KEY (id_aluno),
    CONSTRAINT uq_aluno_cgm   UNIQUE (cgm),
    CONSTRAINT fk_aluno_admin FOREIGN KEY (admin_login)
    REFERENCES sei_db.admin (login)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX IF NOT EXISTS idx_aluno_admin_login
    ON sei_db.aluno (admin_login);

CREATE TABLE IF NOT EXISTS sei_db.professor (
    id_professor INTEGER      NOT NULL,
    nome         VARCHAR(45)  NOT NULL,
    materia      VARCHAR(45)  NOT NULL,
    admin_login  INTEGER      NOT NULL,
    CONSTRAINT pk_professor       PRIMARY KEY (id_professor),
    CONSTRAINT fk_professor_admin FOREIGN KEY (admin_login)
    REFERENCES sei_db.admin (login)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX IF NOT EXISTS idx_professor_admin_login
    ON sei_db.professor (admin_login);

CREATE TABLE IF NOT EXISTS sei_db.jogo (
    id_jogo INTEGER      NOT NULL,
    nome    VARCHAR(45)  NOT NULL,
    admin_login INTEGER  NOT NULL,
    CONSTRAINT pk_jogo PRIMARY KEY (id_jogo),
    CONSTRAINT fk_jogo_admin FOREIGN KEY (admin_login)
    REFERENCES sei_db.admin (login)
    );

CREATE TABLE IF NOT EXISTS sei_db.sala (
    id_sala                INTEGER      NOT NULL,
    data                   TIMESTAMPTZ  NOT NULL,
    num_sa                 VARCHAR(45)  NOT NULL,
    professor_id_professor INTEGER      NOT NULL,
    admin_login            INTEGER      NOT NULL,
    jogo_id_jogo           INTEGER      NOT NULL,
    CONSTRAINT pk_sala           PRIMARY KEY (id_sala),
    CONSTRAINT uq_sala_num_sa    UNIQUE (num_sa),
    CONSTRAINT fk_sala_professor FOREIGN KEY (professor_id_professor)
    REFERENCES sei_db.professor (id_professor)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_sala_admin FOREIGN KEY (admin_login)
    REFERENCES sei_db.admin (login)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_sala_jogo FOREIGN KEY (jogo_id_jogo)
    REFERENCES sei_db.jogo (id_jogo)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX IF NOT EXISTS idx_sala_professor
    ON sei_db.sala (professor_id_professor);

CREATE INDEX IF NOT EXISTS idx_sala_admin
    ON sei_db.sala (admin_login);

CREATE INDEX IF NOT EXISTS idx_sala_jogo
    ON sei_db.sala (jogo_id_jogo);

CREATE TABLE IF NOT EXISTS sei_db.presenca (
    aluno_id_aluno INTEGER NOT NULL,
    sala_id_sala   INTEGER NOT NULL,
    CONSTRAINT pk_presenca PRIMARY KEY (aluno_id_aluno, sala_id_sala),

    CONSTRAINT fk_presenca_aluno FOREIGN KEY (aluno_id_aluno)
    REFERENCES sei_db.aluno (id_aluno)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_presenca_sala FOREIGN KEY (sala_id_sala)
    REFERENCES sei_db.sala (id_sala)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX IF NOT EXISTS idx_presenca_sala
    ON sei_db.presenca (sala_id_sala);

CREATE INDEX IF NOT EXISTS idx_presenca_aluno
    ON sei_db.presenca (aluno_id_aluno);

INSERT INTO sei_db.admin (login, senha)
VALUES (1, '$2a$10$5rqy3ciNiSfNCcsCsnMFvOIOlYukVFQzaDjG2Pqo2bjoi0ayPbt6e');