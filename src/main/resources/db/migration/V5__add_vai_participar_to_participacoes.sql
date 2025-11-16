-- Adicionar coluna vai_participar na tabela participacoes_atividades
ALTER TABLE participacoes_atividades
    ADD vai_participar NUMBER(1) DEFAULT 1 NOT NULL;

-- Atualizar registros existentes para true (vai participar)
UPDATE participacoes_atividades
SET vai_participar = 1
WHERE vai_participar IS NULL;

-- Adicionar constraint
ALTER TABLE participacoes_atividades
    ADD CONSTRAINT ck_vai_participar CHECK (vai_participar IN (0, 1));

