ALTER TABLE sei_db.jogo
    ADD COLUMN data_criacao DATE;

-- Preenche jogos já existentes com a data atual, já que não havia
-- registro de data de criação antes desta migration.
UPDATE sei_db.jogo
    SET data_criacao = CURRENT_DATE
    WHERE data_criacao IS NULL;
