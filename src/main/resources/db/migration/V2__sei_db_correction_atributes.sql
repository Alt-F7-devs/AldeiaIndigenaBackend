-- =============================================
-- 1. Remover FKs dependentes de admin antes de alterar
-- =============================================
ALTER TABLE sei_db.aluno     DROP CONSTRAINT fk_aluno_admin;
ALTER TABLE sei_db.professor DROP CONSTRAINT fk_professor_admin;
ALTER TABLE sei_db.jogo      DROP CONSTRAINT fk_jogo_admin;
ALTER TABLE sei_db.sala      DROP CONSTRAINT fk_sala_admin;

-- =============================================
-- 2. Alterar tabela admin
-- =============================================

ALTER TABLE sei_db.admin DROP CONSTRAINT pk_admin;

ALTER TABLE sei_db.admin
ALTER COLUMN login TYPE VARCHAR(11) USING login::VARCHAR(11);

ALTER TABLE sei_db.admin
    ADD COLUMN id_admin SERIAL NOT NULL;

ALTER TABLE sei_db.admin
    ADD CONSTRAINT pk_admin PRIMARY KEY (id_admin);

-- =============================================
-- 3. Alterar tabela professor
-- =============================================
ALTER TABLE sei_db.professor
DROP COLUMN materia;

ALTER TABLE sei_db.professor
    ADD COLUMN cpf   VARCHAR(11) NOT NULL DEFAULT '',
    ADD COLUMN senha VARCHAR(20) NOT NULL DEFAULT '';

ALTER TABLE sei_db.professor
    ALTER COLUMN cpf   DROP DEFAULT,
ALTER COLUMN senha DROP DEFAULT;

-- =============================================
-- 4. Recriar FKs apontando para admin(id_admin)
-- =============================================
ALTER TABLE sei_db.aluno
    ADD CONSTRAINT fk_aluno_admin
        FOREIGN KEY (admin_login)
            REFERENCES sei_db.admin (id_admin)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE sei_db.professor
    ADD CONSTRAINT fk_professor_admin
        FOREIGN KEY (admin_login)
            REFERENCES sei_db.admin (id_admin)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE sei_db.jogo
    ADD CONSTRAINT fk_jogo_admin
        FOREIGN KEY (admin_login)
            REFERENCES sei_db.admin (id_admin)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE sei_db.sala
    ADD CONSTRAINT fk_sala_admin
        FOREIGN KEY (admin_login)
            REFERENCES sei_db.admin (id_admin)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;