-- Adicionar coluna opcoes_resposta na tabela enquetes
ALTER TABLE enquetes
    ADD opcoes_resposta VARCHAR2(2000 CHAR);

-- Atualizar enquetes existentes com opções padrão (SIM, NÃO, TALVEZ)
UPDATE enquetes
SET opcoes_resposta = '["SIM","NÃO","TALVEZ"]'
WHERE opcoes_resposta IS NULL;

