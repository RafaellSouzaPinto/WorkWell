CREATE TABLE consultas_psicologicas (
    id                              VARCHAR2(36 CHAR)    NOT NULL,
    empresa_id                      VARCHAR2(36 CHAR)    NOT NULL,
    funcionario_id                  VARCHAR2(36 CHAR)    NOT NULL,
    psicologo_id                    VARCHAR2(36 CHAR)    NOT NULL,
    criado_por_id                   VARCHAR2(36 CHAR)    NOT NULL,
    aguardando_confirmacao_de_id    VARCHAR2(36 CHAR),
    data_hora_inicio                TIMESTAMP            NOT NULL,
    data_hora_fim                   TIMESTAMP            NOT NULL,
    local_atendimento               VARCHAR2(20 CHAR)    NOT NULL,
    sala                            VARCHAR2(10 CHAR),
    observacoes                     VARCHAR2(255 CHAR),
    status                          VARCHAR2(30 CHAR)    NOT NULL,
    justificativa_cancelamento      VARCHAR2(255 CHAR),
    created_at                      TIMESTAMP            DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP            DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE consultas_psicologicas
    ADD CONSTRAINT pk_consultas_psicologicas PRIMARY KEY (id);

ALTER TABLE consultas_psicologicas
    ADD CONSTRAINT fk_consultas_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresas (id);

ALTER TABLE consultas_psicologicas
    ADD CONSTRAINT fk_consultas_funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES usuarios (id);

ALTER TABLE consultas_psicologicas
    ADD CONSTRAINT fk_consultas_psicologo
        FOREIGN KEY (psicologo_id)
        REFERENCES usuarios (id);

ALTER TABLE consultas_psicologicas
    ADD CONSTRAINT fk_consultas_criado_por
        FOREIGN KEY (criado_por_id)
        REFERENCES usuarios (id);

ALTER TABLE consultas_psicologicas
    ADD CONSTRAINT fk_consultas_confirmacao
        FOREIGN KEY (aguardando_confirmacao_de_id)
        REFERENCES usuarios (id);

CREATE OR REPLACE TRIGGER trg_consultas_psicologicas_updated_at
    BEFORE UPDATE ON consultas_psicologicas
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

