-- Tabela de denúncias éticas
-- Criar tabela apenas se não existir
BEGIN
    EXECUTE IMMEDIATE '
    CREATE TABLE denuncias_eticas (
        id                  VARCHAR2(36 CHAR)        NOT NULL,
        empresa_id          VARCHAR2(36 CHAR)        NOT NULL,
        denunciante_id      VARCHAR2(36 CHAR)        NOT NULL,
        tipo_denuncia       VARCHAR2(100 CHAR)       NOT NULL,
        descricao           VARCHAR2(4000 CHAR)      NOT NULL,
        envolvidos         VARCHAR2(500 CHAR),
        local_ocorrencia    VARCHAR2(255 CHAR),
        data_ocorrencia     TIMESTAMP,
        anexos              VARCHAR2(2000 CHAR),
        status              VARCHAR2(50 CHAR)         DEFAULT ''PENDENTE'' NOT NULL,
        observacoes_admin   VARCHAR2(2000 CHAR),
        created_at          TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_at          TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
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
    ALTER TABLE denuncias_eticas
        ADD CONSTRAINT pk_denuncias_eticas PRIMARY KEY (id)';
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
    ALTER TABLE denuncias_eticas
        ADD CONSTRAINT fk_denuncias_eticas_empresa
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
    ALTER TABLE denuncias_eticas
        ADD CONSTRAINT fk_denuncias_eticas_denunciante
            FOREIGN KEY (denunciante_id)
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

-- Trigger para updated_at
CREATE OR REPLACE TRIGGER trg_denuncias_eticas_updated_at
    BEFORE UPDATE ON denuncias_eticas
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

