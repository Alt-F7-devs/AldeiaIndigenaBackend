CREATE SEQUENCE IF NOT EXISTS sei_db.aluno_id_aluno_seq;

ALTER TABLE sei_db.aluno
    ALTER COLUMN id_aluno SET DEFAULT nextval('sei_db.aluno_id_aluno_seq');

SELECT setval('sei_db.aluno_id_aluno_seq', COALESCE(MAX(id_aluno), 0) + 1, false)
FROM sei_db.aluno;

ALTER TABLE sei_db.aluno
ALTER COLUMN senha TYPE VARCHAR(255);

ALTER TABLE sei_db.admin
ALTER COLUMN senha TYPE VARCHAR(255);

CREATE SEQUENCE IF NOT EXISTS sei_db.professor_id_professor_seq;

ALTER TABLE sei_db.professor
    ALTER COLUMN id_professor SET DEFAULT nextval('sei_db.professor_id_professor_seq');

SELECT setval('sei_db.professor_id_professor_seq', COALESCE(MAX(id_professor), 0) + 1, false)
FROM sei_db.professor;

ALTER TABLE sei_db.professor
ALTER COLUMN senha TYPE VARCHAR(255);

CREATE SEQUENCE IF NOT EXISTS sei_db.jogo_id_jogo_seq;

ALTER TABLE sei_db.jogo
    ALTER COLUMN id_jogo SET DEFAULT nextval('sei_db.jogo_id_jogo_seq');

SELECT setval('sei_db.jogo_id_jogo_seq', COALESCE(MAX(id_jogo), 0) + 1, false)
FROM sei_db.jogo;

CREATE SEQUENCE IF NOT EXISTS sei_db.sala_id_sala_seq;

ALTER TABLE sei_db.sala
    ALTER COLUMN id_sala SET DEFAULT nextval('sei_db.sala_id_sala_seq');

SELECT setval('sei_db.sala_id_sala_seq', COALESCE(MAX(id_sala), 0) + 1, false)
FROM sei_db.sala;