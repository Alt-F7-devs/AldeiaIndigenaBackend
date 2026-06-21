-- Presença passa a ser por JOGO específico, não mais pela sala inteira.
-- Antes: um aluno presente "na sala" contava como presença em todos os
-- jogos daquela sala (incorreto, já que uma sala pode ter vários jogos).
-- Agora: presença é a combinação (aluno, jogo).

-- 1. Cria a nova coluna em presenca
ALTER TABLE sei_db.presenca
    ADD COLUMN jogo_id_jogo INTEGER;

-- 2. Migra os dados existentes: cada presença antiga (aluno + sala)
-- passa a apontar para o jogo mais antigo daquela sala (na prática,
-- antes desta mudança cada sala só tinha um jogo vinculado por vez).
UPDATE sei_db.presenca p
SET jogo_id_jogo = (
    SELECT j.id_jogo
    FROM sei_db.jogo j
    WHERE j.sala_id_sala = p.sala_id_sala
    ORDER BY j.id_jogo
    LIMIT 1
);

-- 3. Remove linhas que não puderam ser migradas (sala sem nenhum jogo
-- vinculado no momento da migration) para poder aplicar NOT NULL
DELETE FROM sei_db.presenca WHERE jogo_id_jogo IS NULL;

ALTER TABLE sei_db.presenca
    ALTER COLUMN jogo_id_jogo SET NOT NULL;

-- 4. Remove a PK e constraints antigas baseadas em sala
ALTER TABLE sei_db.presenca
    DROP CONSTRAINT IF EXISTS pk_presenca;

ALTER TABLE sei_db.presenca
    DROP CONSTRAINT IF EXISTS fk_presenca_sala;

DROP INDEX IF EXISTS sei_db.idx_presenca_sala;

ALTER TABLE sei_db.presenca
    DROP COLUMN IF EXISTS sala_id_sala;

-- 5. Cria a nova PK e FK baseadas em jogo
ALTER TABLE sei_db.presenca
    ADD CONSTRAINT pk_presenca PRIMARY KEY (aluno_id_aluno, jogo_id_jogo);

ALTER TABLE sei_db.presenca
    ADD CONSTRAINT fk_presenca_jogo FOREIGN KEY (jogo_id_jogo)
    REFERENCES sei_db.jogo (id_jogo)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

CREATE INDEX IF NOT EXISTS idx_presenca_jogo
    ON sei_db.presenca (jogo_id_jogo);
