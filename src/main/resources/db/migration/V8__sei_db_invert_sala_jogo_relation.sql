-- Inverte a relação Sala <-> Jogo: antes 1 sala tinha no máximo 1 jogo
-- (FK em sala.jogo_id_jogo); agora 1 sala pode ter VÁRIOS jogos
-- (FK em jogo.sala_id_sala).

-- 1. Adiciona a nova coluna em jogo
ALTER TABLE sei_db.jogo
    ADD COLUMN sala_id_sala INTEGER;

ALTER TABLE sei_db.jogo
    ADD CONSTRAINT fk_jogo_sala FOREIGN KEY (sala_id_sala)
    REFERENCES sei_db.sala (id_sala)
    ON DELETE SET NULL
    ON UPDATE NO ACTION;

CREATE INDEX IF NOT EXISTS idx_jogo_sala
    ON sei_db.jogo (sala_id_sala);

-- 2. Migra os vínculos existentes: cada jogo que já estava
-- referenciado por uma sala passa a apontar para essa mesma sala
UPDATE sei_db.jogo j
SET sala_id_sala = s.id_sala
FROM sei_db.sala s
WHERE s.jogo_id_jogo = j.id_jogo;

-- 3. Remove a FK e a coluna antiga em sala
ALTER TABLE sei_db.sala
    DROP CONSTRAINT IF EXISTS fk_sala_jogo;

DROP INDEX IF EXISTS sei_db.idx_sala_jogo;

ALTER TABLE sei_db.sala
    DROP COLUMN IF EXISTS jogo_id_jogo;
