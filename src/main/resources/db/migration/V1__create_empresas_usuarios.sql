CREATE TABLE empresas (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    nome            VARCHAR2(120 CHAR)       NOT NULL,
    cnpj            VARCHAR2(18 CHAR)        NOT NULL,
    descricao       VARCHAR2(255 CHAR),
    token           VARCHAR2(60 CHAR)        NOT NULL,
    admin_user_id   VARCHAR2(36 CHAR),
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE empresas
    ADD CONSTRAINT pk_empresas PRIMARY KEY (id);

ALTER TABLE empresas
    ADD CONSTRAINT uk_empresas_cnpj UNIQUE (cnpj);

ALTER TABLE empresas
    ADD CONSTRAINT uk_empresas_token UNIQUE (token);


CREATE TABLE usuarios (
    id              VARCHAR2(36 CHAR)        NOT NULL,
    nome            VARCHAR2(120 CHAR)       NOT NULL,
    email           VARCHAR2(180 CHAR)       NOT NULL,
    senha           VARCHAR2(120 CHAR)       NOT NULL,
    role            VARCHAR2(20 CHAR)        NOT NULL,
    crp             VARCHAR2(20 CHAR),
    empresa_id      VARCHAR2(36 CHAR),
    ativo           NUMBER(1)                DEFAULT 1 NOT NULL,
    created_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE usuarios
    ADD CONSTRAINT pk_usuarios PRIMARY KEY (id);

ALTER TABLE usuarios
    ADD CONSTRAINT uk_usuarios_email UNIQUE (email);

ALTER TABLE usuarios
    ADD CONSTRAINT fk_usuarios_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresas (id);


ALTER TABLE empresas
    ADD CONSTRAINT fk_empresas_admin
        FOREIGN KEY (admin_user_id)
        REFERENCES usuarios (id);

ALTER TABLE empresas
    ADD CONSTRAINT uk_empresas_admin UNIQUE (admin_user_id);


CREATE OR REPLACE TRIGGER trg_empresas_updated_at
    BEFORE UPDATE ON empresas
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_usuarios_updated_at
    BEFORE UPDATE ON usuarios
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

