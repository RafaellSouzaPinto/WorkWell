-- Tabela de avaliações profundas (criadas por psicólogos)
-- Criar tabela apenas se não existir
BEGIN
    EXECUTE IMMEDIATE '
    CREATE TABLE avaliacoes_profundas (
        id              VARCHAR2(36 CHAR)        NOT NULL,
        empresa_id      VARCHAR2(36 CHAR)        NOT NULL,
        criado_por_id   VARCHAR2(36 CHAR)        NOT NULL,
        titulo          VARCHAR2(255 CHAR)        NOT NULL,
        descricao       VARCHAR2(1000 CHAR),
        perguntas       VARCHAR2(4000 CHAR),
        ativa           NUMBER(1)                DEFAULT 1 NOT NULL,
        data_inicio     TIMESTAMP                NOT NULL,
        data_fim        TIMESTAMP                NOT NULL,
        created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
    )';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN
            NULL; -- Tabela já existe
        ELSE
            RAISE;
        END IF;
END;
/

-- Adicionar constraints apenas se não existirem
BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE avaliacoes_profundas
        ADD CONSTRAINT pk_avaliacoes_profundas PRIMARY KEY (id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE avaliacoes_profundas
        ADD CONSTRAINT fk_avaliacoes_profundas_empresa
            FOREIGN KEY (empresa_id)
            REFERENCES empresas (id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE avaliacoes_profundas
        ADD CONSTRAINT fk_avaliacoes_profundas_criado_por
            FOREIGN KEY (criado_por_id)
            REFERENCES usuarios (id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/


-- Tabela de respostas de avaliações profundas
-- Criar tabela apenas se não existir
BEGIN
    EXECUTE IMMEDIATE '
    CREATE TABLE respostas_avaliacoes_profundas (
        id              VARCHAR2(36 CHAR)        NOT NULL,
        avaliacao_id    VARCHAR2(36 CHAR)        NOT NULL,
        usuario_id      VARCHAR2(36 CHAR)        NOT NULL,
        respostas       VARCHAR2(4000 CHAR),
        observacoes     VARCHAR2(1000 CHAR),
        created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
    )';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -955 THEN
            NULL; -- Tabela já existe
        ELSE
            RAISE;
        END IF;
END;
/

-- Adicionar constraints apenas se não existirem
BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE respostas_avaliacoes_profundas
        ADD CONSTRAINT pk_respostas_avaliacoes_profundas PRIMARY KEY (id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE respostas_avaliacoes_profundas
        ADD CONSTRAINT fk_respostas_avaliacoes_profundas_avaliacao
            FOREIGN KEY (avaliacao_id)
            REFERENCES avaliacoes_profundas (id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE respostas_avaliacoes_profundas
        ADD CONSTRAINT fk_respostas_avaliacoes_profundas_usuario
            FOREIGN KEY (usuario_id)
            REFERENCES usuarios (id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE '
    ALTER TABLE respostas_avaliacoes_profundas
        ADD CONSTRAINT uk_resposta_avaliacao_usuario UNIQUE (avaliacao_id, usuario_id)';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE = -2260 OR SQLCODE = -2275 THEN
            NULL;
        ELSE
            RAISE;
        END IF;
END;
/


-- Triggers para updated_at
CREATE OR REPLACE TRIGGER trg_avaliacoes_profundas_updated_at
    BEFORE UPDATE ON avaliacoes_profundas
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_respostas_avaliacoes_profundas_updated_at
    BEFORE UPDATE ON respostas_avaliacoes_profundas
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

