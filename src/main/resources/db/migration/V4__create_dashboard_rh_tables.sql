-- Tabela de registros de humor
CREATE TABLE registros_humor (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    empresa_id      VARCHAR2(36 CHAR)        NOT NULL,
    usuario_id      VARCHAR2(36 CHAR)        NOT NULL,
    nivel_humor     NUMBER(1)                NOT NULL,
    setor           VARCHAR2(100 CHAR),
    observacoes     VARCHAR2(500 CHAR),
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE registros_humor
    ADD CONSTRAINT pk_registros_humor PRIMARY KEY (id);

ALTER TABLE registros_humor
    ADD CONSTRAINT fk_registros_humor_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresas (id);

ALTER TABLE registros_humor
    ADD CONSTRAINT fk_registros_humor_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id);

ALTER TABLE registros_humor
    ADD CONSTRAINT ck_nivel_humor CHECK (nivel_humor BETWEEN 1 AND 5);


-- Tabela de enquetes
CREATE TABLE enquetes (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    empresa_id      VARCHAR2(36 CHAR)        NOT NULL,
    criado_por_id   VARCHAR2(36 CHAR)        NOT NULL,
    pergunta        VARCHAR2(255 CHAR)       NOT NULL,
    ativa           NUMBER(1)                DEFAULT 1 NOT NULL,
    data_fim        TIMESTAMP,
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE enquetes
    ADD CONSTRAINT pk_enquetes PRIMARY KEY (id);

ALTER TABLE enquetes
    ADD CONSTRAINT fk_enquetes_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresas (id);

ALTER TABLE enquetes
    ADD CONSTRAINT fk_enquetes_criado_por
        FOREIGN KEY (criado_por_id)
        REFERENCES usuarios (id);


-- Tabela de respostas de enquetes
CREATE TABLE respostas_enquetes (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    enquete_id      VARCHAR2(36 CHAR)        NOT NULL,
    usuario_id     VARCHAR2(36 CHAR)        NOT NULL,
    resposta        VARCHAR2(50 CHAR)        NOT NULL,
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE respostas_enquetes
    ADD CONSTRAINT pk_respostas_enquetes PRIMARY KEY (id);

ALTER TABLE respostas_enquetes
    ADD CONSTRAINT fk_respostas_enquetes_enquete
        FOREIGN KEY (enquete_id)
        REFERENCES enquetes (id);

ALTER TABLE respostas_enquetes
    ADD CONSTRAINT fk_respostas_enquetes_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id);

ALTER TABLE respostas_enquetes
    ADD CONSTRAINT uk_resposta_enquete_usuario UNIQUE (enquete_id, usuario_id);


-- Tabela de atividades de bem-estar
CREATE TABLE atividades_bem_estar (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    empresa_id      VARCHAR2(36 CHAR)        NOT NULL,
    criado_por_id   VARCHAR2(36 CHAR)        NOT NULL,
    tipo            VARCHAR2(30 CHAR)        NOT NULL,
    titulo          VARCHAR2(200 CHAR)       NOT NULL,
    descricao       VARCHAR2(1000 CHAR),
    data_hora_inicio TIMESTAMP               NOT NULL,
    data_hora_fim   TIMESTAMP,
    local           VARCHAR2(200 CHAR),
    ativa           NUMBER(1)                DEFAULT 1 NOT NULL,
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE atividades_bem_estar
    ADD CONSTRAINT pk_atividades_bem_estar PRIMARY KEY (id);

ALTER TABLE atividades_bem_estar
    ADD CONSTRAINT fk_atividades_bem_estar_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresas (id);

ALTER TABLE atividades_bem_estar
    ADD CONSTRAINT fk_atividades_bem_estar_criado_por
        FOREIGN KEY (criado_por_id)
        REFERENCES usuarios (id);

ALTER TABLE atividades_bem_estar
    ADD CONSTRAINT ck_tipo_atividade CHECK (tipo IN (
        'ATIVIDADE_FISICA',
        'HAPPY_HOUR',
        'PALESTRA_BEM_ESTAR',
        'MEDITACAO_GUIADA',
        'INTERACAO_SOCIAL',
        'SESSAO_ANTI_BURNOUT'
    ));


-- Tabela de participações em atividades
CREATE TABLE participacoes_atividades (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    atividade_id    VARCHAR2(36 CHAR)        NOT NULL,
    usuario_id      VARCHAR2(36 CHAR)        NOT NULL,
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE participacoes_atividades
    ADD CONSTRAINT pk_participacoes_atividades PRIMARY KEY (id);

ALTER TABLE participacoes_atividades
    ADD CONSTRAINT fk_participacoes_atividades_atividade
        FOREIGN KEY (atividade_id)
        REFERENCES atividades_bem_estar (id);

ALTER TABLE participacoes_atividades
    ADD CONSTRAINT fk_participacoes_atividades_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id);

ALTER TABLE participacoes_atividades
    ADD CONSTRAINT uk_participacao_atividade_usuario UNIQUE (atividade_id, usuario_id);


-- Triggers para updated_at
CREATE OR REPLACE TRIGGER trg_registros_humor_updated_at
    BEFORE UPDATE ON registros_humor
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_enquetes_updated_at
    BEFORE UPDATE ON enquetes
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_respostas_enquetes_updated_at
    BEFORE UPDATE ON respostas_enquetes
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_atividades_bem_estar_updated_at
    BEFORE UPDATE ON atividades_bem_estar
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_participacoes_atividades_updated_at
    BEFORE UPDATE ON participacoes_atividades
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

