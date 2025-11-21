# WorkWell 🌟

<div align="center">
  <img src="src/main/resources/static/img/logopng.png" alt="WorkWell Logo" width="200"/>
  
  ### Plataforma Completa de Gestão de Saúde Mental e Bem-Estar Corporativo
  
  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
  [![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
  [![Oracle](https://img.shields.io/badge/Oracle-Database-red.svg)](https://www.oracle.com/database/)
  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do Sistema](#-arquitetura-do-sistema)
- [Modelo de Dados](#-modelo-de-dados)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Executando o Projeto](#-executando-o-projeto)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Endpoints da API](#-endpoints-da-api)
- [Autenticação e Segurança](#-autenticação-e-segurança)
- [Internacionalização](#-internacionalização)
- [Sistema de Cache](#-sistema-de-cache)
- [Mensageria e Notificações](#-mensageria-e-notificações)
- [Integração com IA](#-integração-com-ia)
- [Migrações de Banco de Dados](#-migrações-de-banco-de-dados)
- [Testes](#-testes)
- [Deploy](#-deploy)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)
- [Contato](#-contato)

---

## 🎯 Sobre o Projeto

**WorkWell** é uma plataforma corporativa completa desenvolvida para promover e gerenciar a saúde mental e o bem-estar dos funcionários nas empresas. O sistema oferece funcionalidades robustas para:

- **Empresas**: Gestão centralizada de saúde mental corporativa
- **Funcionários**: Acesso a apoio psicológico, atividades de bem-estar e acompanhamento de humor
- **Psicólogos**: Gerenciamento de consultas e atendimentos
- **RH**: Dashboard analítico com insights estratégicos e estatísticas detalhadas

### 🌈 Por que WorkWell?

O bem-estar no ambiente de trabalho é fundamental para a produtividade, satisfação e retenção de talentos. WorkWell oferece:

✅ **Sistema de Agendamento** de consultas psicológicas online e presenciais  
✅ **Registro de Humor** diário com análise de sentimentos por IA  
✅ **Atividades de Bem-Estar** personalizadas (físicas, mentais, sociais)  
✅ **Enquetes e Avaliações** profundas para mensurar clima organizacional  
✅ **Canal de Denúncias Éticas** 100% sigiloso e independente  
✅ **Dashboard Analítico** com insights de IA para tomada de decisão  
✅ **Notificações por Email** via RabbitMQ para todos os eventos importantes  
✅ **Multilíngue** com suporte a Português, Inglês e Espanhol

---

## 🚀 Funcionalidades Principais

### 1. **Gestão de Empresas e Usuários**

- Cadastro de empresas com token único para colaboradores
- Sistema de roles (Admin, RH, Psicólogo, Funcionário)
- Autenticação segura com JWT
- Vinculação de usuários via token corporativo

### 2. **Apoio Psicológico**

- Agendamento de consultas psicológicas
- Modalidades: Online (videoconferência) e Presencial
- Status: Aguardando confirmação, Confirmada, Cancelada, Concluída
- Notificações automáticas por email
- Sistema de confirmação bidirecional (funcionário e psicólogo)

### 3. **Mood Check - Registro de Humor**

- Escala de 1 a 5 para avaliar humor diário
- Registro por setor/departamento
- Observações opcionais
- Análise de sentimentos via IA (OpenAI)
- Histórico temporal de humor

### 4. **Atividades de Bem-Estar**

- Tipos: Atividade Física, Happy Hour, Palestra, Meditação, Interação Social, Anti-Burnout
- Sistema de participação: "Vou participar" / "Não vou"
- Métricas de aderência
- Calendário de atividades

### 5. **Enquetes Rápidas**

- Criação de enquetes customizadas
- Opções de resposta flexíveis
- Sistema de única resposta por usuário
- Estatísticas de participação em tempo real

### 6. **Avaliações Profundas (NR-1)**

- Questionários detalhados de saúde ocupacional
- Criadas por psicólogos
- Período definido (data início/fim)
- Relatórios confidenciais
- Análise estatística agregada

### 7. **Canal de Denúncias Éticas**

- 100% sigiloso e independente
- Tipos: Assédio Moral, Sexual, Discriminação, Violação de Dados, etc.
- Anexos e evidências
- Status: Pendente, Em andamento, Resolvida
- Acesso exclusivo para administradores

### 8. **Dashboard RH**

- Nível médio de humor da empresa
- Frequência de consultas psicológicas
- Aderência a atividades de bem-estar
- Setores com maior estresse
- Insights estratégicos gerados por IA
- Estatísticas por período (30, 60, 90 dias)
- Exportação de relatórios

### 9. **Assistente Virtual de IA**

- Chat integrado com OpenAI GPT-4o-mini
- Análise de sentimentos baseada em registros de humor
- Sugestões personalizadas de atividades
- Insights estratégicos para RH
- Contextualização baseada no perfil do usuário

### 10. **Sistema de Notificações**

- Emails transacionais via RabbitMQ
- Templates HTML responsivos
- Notificações de agendamento, confirmação, cancelamento e conclusão
- Email de boas-vindas para novas empresas
- Lembretes de consultas

---

## 🛠️ Tecnologias Utilizadas

### Backend

- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Web** - API RESTful
- **Spring AMQP** - Integração com RabbitMQ
- **Spring Mail** - Envio de emails
- **Spring Cache** - Sistema de cache
- **Thymeleaf** - Template engine para views e emails

### Banco de Dados

- **Oracle Database** - Banco de dados relacional
- **Flyway** - Versionamento e migrações de schema
- **HikariCP** - Pool de conexões

### Segurança

- **JWT (JSON Web Tokens)** - Autenticação stateless
- **BCrypt** - Hash de senhas
- **Spring Security** - Framework de segurança

### Inteligência Artificial

- **Spring AI** - Framework de IA
- **OpenAI GPT-4o-mini** - Modelo de linguagem
- Análise de sentimentos
- Geração de insights
- Chat assistente

### Mensageria

- **RabbitMQ (CloudAMQP)** - Message broker
- Sistema de filas para emails
- Retry pattern configurado

### Cache

- **Caffeine** - Cache em memória de alta performance
- Caches: dashboard, enquetes, atividades, estatísticas, insights de IA

### Observabilidade

- **Spring Boot Actuator** - Métricas e health checks
- Logging configurável

### Utilitários

- **Lombok** - Redução de boilerplate
- **Jackson** - Serialização JSON
- **Validation API** - Validação de dados

### DevOps

- **Maven** - Gerenciamento de dependências e build
- **Spring DevTools** - Hot reload em desenvolvimento

---

## 🏗️ Arquitetura do Sistema

WorkWell segue uma arquitetura em camadas (Layered Architecture) bem definida:

```
┌─────────────────────────────────────────────┐
│           CLIENT LAYER                      │
│  (Browser - Thymeleaf Templates + JS)       │
└──────────────────┬──────────────────────────┘
                   │ HTTP/HTTPS
┌──────────────────▼──────────────────────────┐
│         CONTROLLER LAYER                    │
│  - AuthController                           │
│  - ApoioPsicologicoController              │
│  - DashboardRhController                    │
│  - AIController                             │
│  - DenunciaEticaController                  │
│  - EmpresaController                        │
│  - UsuarioController                        │
│  - AvaliacaoProfundaController             │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          SERVICE LAYER                      │
│  - AuthService (JWT)                        │
│  - ApoioPsicologicoService                 │
│  - DashboardRhService                       │
│  - AIService (OpenAI)                       │
│  - DenunciaEticaService                     │
│  - EmpresaService                           │
│  - UsuarioService                           │
│  - AvaliacaoProfundaService                │
│  - EmailService                             │
│  - EmailNotificationService                 │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│       REPOSITORY LAYER                      │
│  - Spring Data JPA Repositories             │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         DATABASE LAYER                      │
│  - Oracle Database                          │
│  - Flyway Migrations                        │
└─────────────────────────────────────────────┘

        CROSS-CUTTING CONCERNS
┌─────────────────────────────────────────────┐
│  - Security (JWT Filter)                    │
│  - Exception Handling                       │
│  - Validation                               │
│  - Caching (Caffeine)                       │
│  - Messaging (RabbitMQ)                     │
│  - Internationalization (i18n)              │
└─────────────────────────────────────────────┘
```

### Padrões Utilizados

- **MVC (Model-View-Controller)** - Separação de responsabilidades
- **Repository Pattern** - Abstração de acesso a dados
- **Service Layer Pattern** - Lógica de negócio centralizada
- **DTO Pattern** - Transfer objects para APIs
- **Strategy Pattern** - Diferentes estratégias de notificação
- **Observer Pattern** - Eventos e mensageria
- **Dependency Injection** - IoC do Spring
- **Builder Pattern** - Construção de objetos complexos (via Lombok)

---

## 🗄️ Modelo de Dados

### Entidades Principais

#### 1. **EMPRESAS**

```sql
- id (VARCHAR2, PK)
- nome (VARCHAR2)
- cnpj (VARCHAR2, UNIQUE)
- descricao (VARCHAR2)
- token (VARCHAR2, UNIQUE)
- admin_user_id (FK -> usuarios)
- created_at, updated_at (TIMESTAMP)
```

#### 2. **USUARIOS**

```sql
- id (VARCHAR2, PK)
- nome (VARCHAR2)
- email (VARCHAR2, UNIQUE)
- senha (VARCHAR2, BCrypt)
- role (VARCHAR2: ADMIN, RH, PSICOLOGO, FUNCIONARIO)
- crp (VARCHAR2, nullable)
- empresa_id (FK -> empresas)
- ativo (NUMBER: 0/1)
- created_at, updated_at (TIMESTAMP)
```

#### 3. **CONSULTAS_PSICOLOGICAS**

```sql
- id (VARCHAR2, PK)
- empresa_id (FK)
- funcionario_id (FK -> usuarios)
- psicologo_id (FK -> usuarios)
- criado_por_id (FK -> usuarios)
- aguardando_confirmacao_de_id (FK -> usuarios)
- data_hora_inicio, data_hora_fim (TIMESTAMP)
- local_atendimento (ONLINE, PRESENCIAL)
- sala (VARCHAR2)
- observacoes (VARCHAR2)
- status (AGUARDANDO_CONFIRMACAO, CONFIRMADA, CANCELADA, CONCLUIDA)
- justificativa_cancelamento (VARCHAR2)
- link_call (VARCHAR2)
- created_at, updated_at (TIMESTAMP)
```

#### 4. **REGISTROS_HUMOR**

```sql
- id (VARCHAR2, PK)
- empresa_id (FK)
- usuario_id (FK)
- nivel_humor (NUMBER: 1-5)
- setor (VARCHAR2)
- observacoes (VARCHAR2)
- created_at, updated_at (TIMESTAMP)
```

#### 5. **ENQUETES**

```sql
- id (VARCHAR2, PK)
- empresa_id (FK)
- criado_por_id (FK -> usuarios)
- pergunta (VARCHAR2)
- opcoes_resposta (VARCHAR2, JSON array)
- ativa (NUMBER: 0/1)
- data_fim (TIMESTAMP)
- created_at, updated_at (TIMESTAMP)
```

#### 6. **RESPOSTAS_ENQUETES**

```sql
- id (VARCHAR2, PK)
- enquete_id (FK)
- usuario_id (FK)
- resposta (VARCHAR2)
- created_at, updated_at (TIMESTAMP)
- UNIQUE(enquete_id, usuario_id)
```

#### 7. **ATIVIDADES_BEM_ESTAR**

```sql
- id (VARCHAR2, PK)
- empresa_id (FK)
- criado_por_id (FK -> usuarios)
- tipo (ATIVIDADE_FISICA, HAPPY_HOUR, PALESTRA_BEM_ESTAR,
        MEDITACAO_GUIADA, INTERACAO_SOCIAL, SESSAO_ANTI_BURNOUT)
- titulo (VARCHAR2)
- descricao (VARCHAR2)
- data_hora_inicio, data_hora_fim (TIMESTAMP)
- local (VARCHAR2)
- ativa (NUMBER: 0/1)
- created_at, updated_at (TIMESTAMP)
```

#### 8. **PARTICIPACOES_ATIVIDADES**

```sql
- id (VARCHAR2, PK)
- atividade_id (FK)
- usuario_id (FK)
- vai_participar (NUMBER: 0/1)
- created_at, updated_at (TIMESTAMP)
- UNIQUE(atividade_id, usuario_id)
```

#### 9. **AVALIACOES_PROFUNDAS**

```sql
- id (VARCHAR2, PK)
- empresa_id (FK)
- criado_por_id (FK -> usuarios, role=PSICOLOGO)
- titulo (VARCHAR2)
- descricao (VARCHAR2)
- perguntas (VARCHAR2, JSON array)
- ativa (NUMBER: 0/1)
- data_inicio, data_fim (TIMESTAMP)
- created_at, updated_at (TIMESTAMP)
```

#### 10. **RESPOSTAS_AVALIACOES_PROFUNDAS**

```sql
- id (VARCHAR2, PK)
- avaliacao_id (FK)
- usuario_id (FK)
- respostas (VARCHAR2, JSON array)
- observacoes (VARCHAR2)
- created_at, updated_at (TIMESTAMP)
- UNIQUE(avaliacao_id, usuario_id)
```

#### 11. **DENUNCIAS_ETICAS**

```sql
- id (VARCHAR2, PK)
- empresa_id (FK)
- denunciante_id (FK -> usuarios)
- tipo_denuncia (VARCHAR2)
- descricao (VARCHAR2)
- envolvidos (VARCHAR2)
- local_ocorrencia (VARCHAR2)
- data_ocorrencia (TIMESTAMP)
- anexos (VARCHAR2)
- status (PENDENTE, EM_ANDAMENTO, RESOLVIDA)
- observacoes_admin (VARCHAR2)
- created_at, updated_at (TIMESTAMP)
```

### Diagrama ER Simplificado

```
EMPRESAS (1) ──────────── (N) USUARIOS
    │                           │
    │                           ├─── (N) CONSULTAS_PSICOLOGICAS
    │                           │
    │                           ├─── (N) REGISTROS_HUMOR
    │                           │
    └─── (N) ENQUETES ────────  └─── (N) RESPOSTAS_ENQUETES
    │
    └─── (N) ATIVIDADES_BEM_ESTAR ─── (N) PARTICIPACOES_ATIVIDADES
    │
    └─── (N) AVALIACOES_PROFUNDAS ─── (N) RESPOSTAS_AVALIACOES_PROFUNDAS
    │
    └─── (N) DENUNCIAS_ETICAS
```

### Triggers Oracle

Todas as tabelas possuem triggers `BEFORE UPDATE` para atualizar automaticamente o campo `updated_at`:

```sql
CREATE OR REPLACE TRIGGER trg_[tabela]_updated_at
    BEFORE UPDATE ON [tabela]
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
```

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- ☑️ **Java JDK 17** ou superior ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- ☑️ **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- ☑️ **Oracle Database** (ou acesso a uma instância remota)
- ☑️ **RabbitMQ** (ou conta no CloudAMQP)
- ☑️ **Git** ([Download](https://git-scm.com/downloads))
- ☑️ **IDE** recomendada: IntelliJ IDEA, Eclipse ou VS Code

### Opcional

- ☑️ **Docker** para executar RabbitMQ localmente
- ☑️ **Postman** ou **Insomnia** para testar APIs

---

## ⚙️ Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/workwell.git
cd workwell
```

### 2. Configure o Banco de Dados Oracle

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:oracle:thin:@SEU_HOST:1521/SEU_SERVICE
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

### 3. Configure o RabbitMQ

Você pode usar o CloudAMQP (recomendado) ou uma instância local.

#### Opção A: CloudAMQP (Gratuito)

1. Crie uma conta em [CloudAMQP](https://www.cloudamqp.com/)
2. Crie uma instância gratuita
3. Copie as credenciais para o `application.properties`:

```properties
# RabbitMQ Configuration (CloudAMQP)
spring.rabbitmq.host=SEU_HOST.rmq.cloudamqp.com
spring.rabbitmq.port=5672
spring.rabbitmq.username=SEU_USUARIO
spring.rabbitmq.password=SUA_SENHA
spring.rabbitmq.virtual-host=SEU_USUARIO
```

#### Opção B: RabbitMQ Local (Docker)

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

```properties
# RabbitMQ Configuration (Local)
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.virtual-host=/
```

### 4. Configure o Email (SMTP)

Para envio de notificações por email, configure um servidor SMTP:

```properties
# Email Configuration (Gmail example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Nota**: Para Gmail, você precisa criar uma [senha de app](https://support.google.com/accounts/answer/185833).

### 5. Configure a OpenAI API

Para funcionalidades de IA, obtenha uma chave de API da OpenAI:

```properties
# OpenAI Configuration
spring.ai.openai.api-key=sk-proj-SUA_CHAVE_AQUI
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.7
```

**Obter chave**: [OpenAI API Keys](https://platform.openai.com/api-keys)

### 6. Configure o JWT Secret

Para produção, gere um secret forte:

```properties
# JWT Configuration
security.jwt.secret=SUA_CHAVE_SECRETA_FORTE_AQUI_MIN_256_BITS
security.jwt.expiration=3600000
```

**Dica**: Gere uma chave forte com:

```bash
openssl rand -base64 64
```

### 7. Variáveis de Ambiente (Opcional - Recomendado para Produção)

Em vez de colocar credenciais diretamente no `application.properties`, use variáveis de ambiente:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.rabbitmq.host=${RABBITMQ_HOST}
spring.rabbitmq.username=${RABBITMQ_USERNAME}
spring.rabbitmq.password=${RABBITMQ_PASSWORD}

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

spring.ai.openai.api-key=${OPENAI_API_KEY}

security.jwt.secret=${JWT_SECRET}
```

E defina-as no sistema:

```bash
export DB_URL=jdbc:oracle:thin:@host:1521/service
export DB_USERNAME=usuario
export DB_PASSWORD=senha
# ... etc
```

---

## 🚀 Executando o Projeto

### Via Maven

```bash
# Limpar e compilar
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### Via JAR

```bash
# Gerar o JAR
mvn clean package

# Executar o JAR
java -jar target/WorkWell-0.0.1-SNAPSHOT.jar
```

### Via IDE

1. Abra o projeto na sua IDE
2. Localize a classe `WorkWellApplication.java`
3. Execute como aplicação Java

### Acessando a Aplicação

Após iniciar, acesse:

- **Interface Web**: [http://localhost:8081](http://localhost:8081)
- **Actuator**: [http://localhost:8081/actuator](http://localhost:8081/actuator)
- **Health Check**: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)

**Nota**: A porta padrão é **8081** (configurada em `application.properties`).

---

## 📁 Estrutura de Pastas

```
WorkWell/
│
├── src/
│   ├── main/
│   │   ├── java/workwell/WorkWell/
│   │   │   ├── config/              # Configurações do Spring
│   │   │   │   ├── CacheConfig.java
│   │   │   │   ├── JacksonConfig.java
│   │   │   │   ├── LocaleConfig.java
│   │   │   │   ├── OracleUpperCaseNamingStrategy.java
│   │   │   │   └── RabbitMQConfig.java
│   │   │   │
│   │   │   ├── controller/          # Controllers REST e MVC
│   │   │   │   ├── advice/
│   │   │   │   │   └── ApiExceptionHandler.java
│   │   │   │   ├── AIController.java
│   │   │   │   ├── ApoioPsicologicoController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── AvaliacaoProfundaController.java
│   │   │   │   ├── DashboardRhController.java
│   │   │   │   ├── DenunciaEticaController.java
│   │   │   │   ├── EmpresaController.java
│   │   │   │   ├── LocaleController.java
│   │   │   │   ├── PageController.java
│   │   │   │   └── UsuarioController.java
│   │   │   │
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── [51 DTOs]
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── entity/              # Entidades JPA
│   │   │   │   ├── enums/
│   │   │   │   │   ├── ConsultaStatus.java
│   │   │   │   │   ├── LocalAtendimento.java
│   │   │   │   │   ├── RoleType.java
│   │   │   │   │   ├── SalaAtendimento.java
│   │   │   │   │   └── TipoAtividadeBemEstar.java
│   │   │   │   ├── AtividadeBemEstar.java
│   │   │   │   ├── AvaliacaoProfunda.java
│   │   │   │   ├── ConsultaPsicologica.java
│   │   │   │   ├── DenunciaEtica.java
│   │   │   │   ├── Empresa.java
│   │   │   │   ├── Enquete.java
│   │   │   │   ├── ParticipacaoAtividade.java
│   │   │   │   ├── RegistroHumor.java
│   │   │   │   ├── RespostaAvaliacaoProfunda.java
│   │   │   │   ├── RespostaEnquete.java
│   │   │   │   └── Usuario.java
│   │   │   │
│   │   │   ├── exception/           # Exceções customizadas
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── ValidationException.java
│   │   │   │
│   │   │   ├── messaging/           # Mensageria RabbitMQ
│   │   │   │   ├── EmailMessage.java
│   │   │   │   └── EmailMessagePublisher.java
│   │   │   │
│   │   │   ├── repository/          # Repositórios JPA
│   │   │   │   ├── AtividadeBemEstarRepository.java
│   │   │   │   ├── AvaliacaoProfundaRepository.java
│   │   │   │   ├── ConsultaPsicologicaRepository.java
│   │   │   │   ├── DenunciaEticaRepository.java
│   │   │   │   ├── EmpresaRepository.java
│   │   │   │   ├── EnqueteRepository.java
│   │   │   │   ├── ParticipacaoAtividadeRepository.java
│   │   │   │   ├── RegistroHumorRepository.java
│   │   │   │   ├── RespostaAvaliacaoProfundaRepository.java
│   │   │   │   ├── RespostaEnqueteRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   │
│   │   │   ├── security/            # Segurança e JWT
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   │
│   │   │   ├── service/             # Serviços de negócio
│   │   │   │   ├── AIService.java
│   │   │   │   ├── ApoioPsicologicoService.java
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── AvaliacaoProfundaService.java
│   │   │   │   ├── DashboardRhService.java
│   │   │   │   ├── DenunciaEticaService.java
│   │   │   │   ├── EmailNotificationService.java
│   │   │   │   ├── EmailService.java
│   │   │   │   ├── EmpresaService.java
│   │   │   │   └── UsuarioService.java
│   │   │   │
│   │   │   ├── validation/          # Validações customizadas
│   │   │   │   └── ...
│   │   │   │
│   │   │   └── WorkWellApplication.java  # Classe principal
│   │   │
│   │   └── resources/
│   │       ├── application.properties    # Configurações
│   │       ├── messages_pt_BR.properties # Mensagens PT-BR
│   │       ├── messages_en_US.properties # Mensagens EN
│   │       ├── messages_es_ES.properties # Mensagens ES
│   │       │
│   │       ├── db/migration/        # Migrações Flyway
│   │       │   ├── V1__create_empresas_usuarios.sql
│   │       │   ├── V2__create_consultas_psicologicas.sql
│   │       │   ├── V3__add_link_call_consultas.sql
│   │       │   ├── V4__create_dashboard_rh_tables.sql
│   │       │   ├── V5__add_vai_participar_to_participacoes.sql
│   │       │   ├── V6__add_opcoes_resposta_to_enquetes.sql
│   │       │   ├── V7__create_avaliacoes_profundas.sql
│   │       │   └── V8__create_denuncias_eticas.sql
│   │       │
│   │       ├── static/              # Recursos estáticos
│   │       │   ├── css/
│   │       │   │   └── app.css
│   │       │   ├── img/
│   │       │   │   ├── logopng.png
│   │       │   │   └── letreiropng.png
│   │       │   └── js/
│   │       │
│   │       └── templates/           # Templates Thymeleaf
│   │           ├── email/
│   │           │   ├── agendamento-consulta.html
│   │           │   ├── boas-vindas-empresa.html
│   │           │   ├── cancelamento-consulta.html
│   │           │   ├── conclusao-consulta.html
│   │           │   └── confirmacao-consulta.html
│   │           ├── fragments/
│   │           │   └── language-selector.html
│   │           ├── apoio_psicologico.html
│   │           ├── cadastro_empresa.html
│   │           ├── cadastro_funcionario.html
│   │           ├── cadastro_psicologo.html
│   │           ├── cadastro_rh.html
│   │           ├── dashboard_rh.html
│   │           ├── dashboard.html
│   │           ├── index.html
│   │           └── login.html
│   │
│   └── test/                        # Testes
│       └── java/workwell/WorkWell/
│           ├── service/
│           └── WorkWellApplicationTests.java
│
├── target/                          # Build output (gerado)
├── .gitignore
├── HELP.md
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
├── pom.xml                          # Configuração Maven
└── README.md                        # Este arquivo
```

---

## 🌐 Endpoints da API

### Autenticação

| Método | Endpoint             | Descrição                    | Auth |
| ------ | -------------------- | ---------------------------- | ---- |
| POST   | `/api/auth/login`    | Login e geração de JWT       | ❌   |
| POST   | `/api/auth/register` | Registro de novo usuário     | ❌   |
| GET    | `/api/auth/me`       | Dados do usuário autenticado | ✅   |

### Empresas

| Método | Endpoint                      | Descrição                | Auth | Role  |
| ------ | ----------------------------- | ------------------------ | ---- | ----- |
| POST   | `/api/empresas`               | Criar nova empresa       | ❌   | -     |
| GET    | `/api/empresas/{id}`          | Buscar empresa por ID    | ✅   | ADMIN |
| GET    | `/api/empresas/token/{token}` | Buscar empresa por token | ❌   | -     |
| PUT    | `/api/empresas/{id}`          | Atualizar empresa        | ✅   | ADMIN |
| DELETE | `/api/empresas/{id}`          | Deletar empresa          | ✅   | ADMIN |

### Usuários

| Método | Endpoint                            | Descrição                  | Auth | Role      |
| ------ | ----------------------------------- | -------------------------- | ---- | --------- |
| POST   | `/api/usuarios`                     | Criar usuário              | ❌   | -         |
| GET    | `/api/usuarios/{id}`                | Buscar usuário             | ✅   | ANY       |
| GET    | `/api/usuarios/empresa/{empresaId}` | Listar usuários da empresa | ✅   | RH, ADMIN |
| PUT    | `/api/usuarios/{id}`                | Atualizar usuário          | ✅   | OWNER     |
| DELETE | `/api/usuarios/{id}`                | Deletar usuário            | ✅   | ADMIN     |
| GET    | `/api/usuarios/psicologos`          | Listar psicólogos          | ✅   | ANY       |

### Consultas Psicológicas

| Método | Endpoint                                     | Descrição                   | Auth | Role      |
| ------ | -------------------------------------------- | --------------------------- | ---- | --------- |
| POST   | `/api/consultas`                             | Agendar consulta            | ✅   | ANY       |
| GET    | `/api/consultas/{id}`                        | Buscar consulta             | ✅   | INVOLVED  |
| GET    | `/api/consultas/empresa/{empresaId}`         | Listar consultas da empresa | ✅   | RH, ADMIN |
| GET    | `/api/consultas/funcionario/{funcionarioId}` | Consultas do funcionário    | ✅   | OWNER     |
| GET    | `/api/consultas/psicologo/{psicologoId}`     | Consultas do psicólogo      | ✅   | OWNER     |
| PUT    | `/api/consultas/{id}/confirmar`              | Confirmar consulta          | ✅   | INVOLVED  |
| PUT    | `/api/consultas/{id}/cancelar`               | Cancelar consulta           | ✅   | INVOLVED  |
| PUT    | `/api/consultas/{id}/concluir`               | Concluir consulta           | ✅   | PSICOLOGO |
| GET    | `/api/consultas/agenda/{usuarioId}/hoje`     | Agenda do dia               | ✅   | OWNER     |

### Registro de Humor

| Método | Endpoint                         | Descrição          | Auth | Role      |
| ------ | -------------------------------- | ------------------ | ---- | --------- |
| POST   | `/api/humor`                     | Registrar humor    | ✅   | ANY       |
| GET    | `/api/humor/usuario/{usuarioId}` | Histórico de humor | ✅   | OWNER, RH |
| GET    | `/api/humor/empresa/{empresaId}` | Humores da empresa | ✅   | RH, ADMIN |
| GET    | `/api/humor/media/{empresaId}`   | Média de humor     | ✅   | RH, ADMIN |

### Enquetes

| Método | Endpoint                            | Descrição         | Auth | Role      |
| ------ | ----------------------------------- | ----------------- | ---- | --------- |
| POST   | `/api/enquetes`                     | Criar enquete     | ✅   | RH, ADMIN |
| GET    | `/api/enquetes/{id}`                | Buscar enquete    | ✅   | ANY       |
| GET    | `/api/enquetes/empresa/{empresaId}` | Listar enquetes   | ✅   | ANY       |
| GET    | `/api/enquetes/ativas/{empresaId}`  | Enquetes ativas   | ✅   | ANY       |
| POST   | `/api/enquetes/{id}/responder`      | Responder enquete | ✅   | ANY       |
| PUT    | `/api/enquetes/{id}`                | Atualizar enquete | ✅   | RH, ADMIN |
| DELETE | `/api/enquetes/{id}`                | Deletar enquete   | ✅   | RH, ADMIN |
| GET    | `/api/enquetes/{id}/resultados`     | Resultados        | ✅   | RH, ADMIN |

### Atividades de Bem-Estar

| Método | Endpoint                              | Descrição            | Auth | Role      |
| ------ | ------------------------------------- | -------------------- | ---- | --------- |
| POST   | `/api/atividades`                     | Criar atividade      | ✅   | RH, ADMIN |
| GET    | `/api/atividades/{id}`                | Buscar atividade     | ✅   | ANY       |
| GET    | `/api/atividades/empresa/{empresaId}` | Listar atividades    | ✅   | ANY       |
| GET    | `/api/atividades/ativas/{empresaId}`  | Atividades ativas    | ✅   | ANY       |
| POST   | `/api/atividades/{id}/participar`     | Marcar participação  | ✅   | ANY       |
| PUT    | `/api/atividades/{id}`                | Atualizar atividade  | ✅   | RH, ADMIN |
| DELETE | `/api/atividades/{id}`                | Deletar atividade    | ✅   | RH, ADMIN |
| GET    | `/api/atividades/{id}/participantes`  | Listar participantes | ✅   | RH, ADMIN |

### Avaliações Profundas

| Método | Endpoint                              | Descrição              | Auth | Role             |
| ------ | ------------------------------------- | ---------------------- | ---- | ---------------- |
| POST   | `/api/avaliacoes`                     | Criar avaliação        | ✅   | PSICOLOGO        |
| GET    | `/api/avaliacoes/{id}`                | Buscar avaliação       | ✅   | ANY              |
| GET    | `/api/avaliacoes/empresa/{empresaId}` | Listar avaliações      | ✅   | ANY              |
| GET    | `/api/avaliacoes/ativas/{empresaId}`  | Avaliações ativas      | ✅   | ANY              |
| POST   | `/api/avaliacoes/{id}/responder`      | Responder avaliação    | ✅   | ANY              |
| PUT    | `/api/avaliacoes/{id}`                | Atualizar avaliação    | ✅   | PSICOLOGO        |
| DELETE | `/api/avaliacoes/{id}`                | Deletar avaliação      | ✅   | PSICOLOGO        |
| GET    | `/api/avaliacoes/{id}/relatorio`      | Relatório confidencial | ✅   | PSICOLOGO, ADMIN |

### Denúncias Éticas

| Método | Endpoint                             | Descrição          | Auth | Role  |
| ------ | ------------------------------------ | ------------------ | ---- | ----- |
| POST   | `/api/denuncias`                     | Criar denúncia     | ✅   | ANY   |
| GET    | `/api/denuncias/{id}`                | Buscar denúncia    | ✅   | ADMIN |
| GET    | `/api/denuncias/empresa/{empresaId}` | Listar denúncias   | ✅   | ADMIN |
| PUT    | `/api/denuncias/{id}`                | Atualizar denúncia | ✅   | ADMIN |
| PUT    | `/api/denuncias/{id}/status`         | Alterar status     | ✅   | ADMIN |

### Dashboard RH

| Método | Endpoint                                          | Descrição            | Auth | Role      |
| ------ | ------------------------------------------------- | -------------------- | ---- | --------- |
| GET    | `/api/dashboard/{empresaId}`                      | Dashboard completo   | ✅   | RH, ADMIN |
| GET    | `/api/dashboard/{empresaId}/humor-medio`          | Humor médio          | ✅   | RH, ADMIN |
| GET    | `/api/dashboard/{empresaId}/consultas-frequencia` | Frequência consultas | ✅   | RH, ADMIN |
| GET    | `/api/dashboard/{empresaId}/atividades-aderencia` | Aderência atividades | ✅   | RH, ADMIN |
| GET    | `/api/dashboard/{empresaId}/setores-estressados`  | Setores com estresse | ✅   | RH, ADMIN |

### IA / Assistente

| Método | Endpoint                      | Descrição               | Auth | Role      |
| ------ | ----------------------------- | ----------------------- | ---- | --------- |
| POST   | `/api/ai/chat`                | Chat com assistente     | ✅   | ANY       |
| POST   | `/api/ai/analisar-sentimento` | Análise de sentimento   | ✅   | ANY       |
| POST   | `/api/ai/sugerir-atividades`  | Sugestões de atividades | ✅   | ANY       |
| POST   | `/api/ai/insights-dashboard`  | Insights estratégicos   | ✅   | RH, ADMIN |

### Views (Thymeleaf)

| Método | Endpoint                | Descrição            | Auth           |
| ------ | ----------------------- | -------------------- | -------------- |
| GET    | `/`                     | Página inicial       | ❌             |
| GET    | `/login`                | Login                | ❌             |
| GET    | `/cadastro/empresa`     | Cadastro empresa     | ❌             |
| GET    | `/cadastro/funcionario` | Cadastro funcionário | ❌             |
| GET    | `/cadastro/psicologo`   | Cadastro psicólogo   | ❌             |
| GET    | `/cadastro/rh`          | Cadastro RH          | ❌             |
| GET    | `/dashboard`            | Dashboard principal  | ✅             |
| GET    | `/dashboard-rh`         | Dashboard RH         | ✅ (RH, ADMIN) |
| GET    | `/apoio-psicologico`    | Apoio psicológico    | ✅             |

---

## 🔐 Autenticação e Segurança

### JWT (JSON Web Tokens)

WorkWell utiliza JWT para autenticação stateless:

1. **Login**: Cliente envia credenciais (`POST /api/auth/login`)
2. **Token**: Servidor valida e retorna JWT
3. **Requisições**: Cliente envia JWT no header `Authorization: Bearer <token>`
4. **Validação**: Filtro valida token em cada requisição

### Estrutura do JWT

```json
{
  "sub": "usuario-id",
  "email": "usuario@email.com",
  "role": "FUNCIONARIO",
  "empresaId": "empresa-id",
  "iat": 1234567890,
  "exp": 1234571490
}
```

### Configuração de Segurança

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Endpoints públicos
    - /api/auth/**
    - /api/empresas (POST)
    - /api/usuarios (POST)
    - /, /login, /cadastro/**
    - /static/**, /css/**, /js/**, /img/**

    // Endpoints protegidos
    - /api/** (requer autenticação)
    - /dashboard/** (requer autenticação)

    // Role-based access
    - /dashboard-rh/** (RH, ADMIN)
    - /api/denuncias/** (ADMIN)
    - /api/avaliacoes (POST, PUT, DELETE) (PSICOLOGO)
}
```

### Proteção contra Ataques

- ✅ **CSRF**: Proteção habilitada para formulários
- ✅ **XSS**: Sanitização de inputs
- ✅ **SQL Injection**: Uso de Prepared Statements (JPA)
- ✅ **Senhas**: Hash BCrypt (custo 12)
- ✅ **Rate Limiting**: Configurável via proxy/gateway
- ✅ **CORS**: Configurado conforme necessidade

---

## 🌍 Internacionalização

WorkWell suporta **3 idiomas**:

- 🇧🇷 **Português (Brasil)** - `pt-BR` (padrão)
- 🇺🇸 **English** - `en-US`
- 🇪🇸 **Español** - `es-ES`

### Arquivos de Mensagens

- `messages_pt_BR.properties` (442 chaves)
- `messages_en_US.properties`
- `messages_es_ES.properties`

### Uso nas Views

```html
<h1 th:text="#{home.title}">Título</h1>
<button th:text="#{app.save}">Salvar</button>
```

### Alternar Idioma

```javascript
// Via API
POST /api/locale/change?lang=en_US
POST /api/locale/change?lang=es_ES
POST /api/locale/change?lang=pt_BR
```

### Configuração

```java
@Configuration
public class LocaleConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("pt", "BR"));
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
}
```

---

## 💾 Sistema de Cache

WorkWell utiliza **Caffeine** para cache em memória de alta performance.

### Caches Configurados

| Nome               | TTL   | Max Size | Uso                         |
| ------------------ | ----- | -------- | --------------------------- |
| `dashboard`        | 5 min | 200      | Dados do dashboard completo |
| `enquetesAtivas`   | 5 min | 200      | Enquetes ativas             |
| `atividadesAtivas` | 5 min | 200      | Atividades ativas           |
| `avaliacoesAtivas` | 5 min | 200      | Avaliações ativas           |
| `empresaToken`     | 5 min | 200      | Empresas por token          |
| `estatisticas`     | 5 min | 200      | Estatísticas RH             |
| `agendaDia`        | 5 min | 200      | Agenda do dia               |
| `insightsAI`       | 5 min | 200      | Insights de IA              |

### Configuração

```properties
spring.cache.type=caffeine
spring.cache.cache-names=dashboard,enquetesAtivas,atividadesAtivas,...
spring.cache.caffeine.spec=maximumSize=200,expireAfterWrite=5m
spring.cache.caffeine.record-stats=true
```

### Uso nos Serviços

```java
@Service
public class DashboardRhService {

    @Cacheable(value = "dashboard", key = "#empresaId")
    public DashboardDto getDashboard(String empresaId) {
        // Consulta pesada ao banco
    }

    @CacheEvict(value = "dashboard", key = "#empresaId")
    public void invalidarCache(String empresaId) {
        // Invalida cache quando dados mudam
    }
}
```

### Monitoramento

Via Actuator:

```
GET /actuator/caches
```

---

## 📨 Mensageria e Notificações

### Arquitetura

WorkWell usa **RabbitMQ** para processamento assíncrono de emails:

```
[Controller] → [EmailNotificationService] → [RabbitMQ Queue] → [EmailMessageListener] → [EmailService] → [SMTP]
```

### Filas Configuradas

| Fila                   | Exchange                  | Routing Key | Uso             |
| ---------------------- | ------------------------- | ----------- | --------------- |
| `workwell.email.queue` | `workwell.email.exchange` | `email.#`   | Todos os emails |

### Tipos de Email

1. **Boas-vindas Empresa** (`email/boas-vindas-empresa.html`)
2. **Agendamento de Consulta** (`email/agendamento-consulta.html`)
3. **Confirmação de Consulta** (`email/confirmacao-consulta.html`)
4. **Cancelamento de Consulta** (`email/cancelamento-consulta.html`)
5. **Conclusão de Consulta** (`email/conclusao-consulta.html`)

### Configuração RabbitMQ

```java
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue emailQueue() {
        return new Queue("workwell.email.queue", true);
    }

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange("workwell.email.exchange");
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange emailExchange) {
        return BindingBuilder.bind(emailQueue)
                .to(emailExchange)
                .with("email.#");
    }
}
```

### Publicação de Mensagens

```java
@Service
public class EmailNotificationService {

    @Autowired
    private EmailMessagePublisher publisher;

    public void enviarBoasVindas(Empresa empresa) {
        EmailMessage message = EmailMessage.builder()
                .to(empresa.getEmail())
                .subject("Bem-vindo ao WorkWell!")
                .templateName("boas-vindas-empresa")
                .context(Map.of("empresa", empresa))
                .build();

        publisher.publishEmailMessage(message);
    }
}
```

### Retry Pattern

Configurado para retentar envios com falha:

```properties
spring.rabbitmq.listener.simple.retry.enabled=true
spring.rabbitmq.listener.simple.retry.initial-interval=2000
spring.rabbitmq.listener.simple.retry.max-attempts=3
spring.rabbitmq.listener.simple.retry.multiplier=2
```

---

## 🤖 Integração com IA

WorkWell integra **OpenAI GPT-4o-mini** via **Spring AI** para:

### 1. Chat Assistente

```java
POST /api/ai/chat
{
    "message": "Como posso melhorar meu bem-estar no trabalho?",
    "usuarioId": "uuid"
}
```

**Response**:

```json
{
  "response": "Aqui estão algumas sugestões personalizadas para você...",
  "timestamp": "2025-11-21T10:30:00"
}
```

### 2. Análise de Sentimentos

```java
POST /api/ai/analisar-sentimento
{
    "usuarioId": "uuid"
}
```

**Response**:

```json
{
  "sentimento": "POSITIVO",
  "score": 0.75,
  "resumo": "Baseado nos seus registros, você está em um estado emocional positivo...",
  "pontosChave": [
    "Humor consistentemente bom",
    "Participação ativa em atividades"
  ],
  "recomendacoes": [
    "Continue mantendo o ritmo",
    "Experimente novas atividades sociais"
  ]
}
```

### 3. Sugestões de Atividades

```java
POST /api/ai/sugerir-atividades
{
    "usuarioId": "uuid"
}
```

**Response**:

```json
{
  "atividadesSugeridas": [
    {
      "tipo": "MEDITACAO_GUIADA",
      "titulo": "Sessão de Mindfulness",
      "justificativa": "Considerando seu nível de estresse recente...",
      "prioridade": "ALTA"
    }
  ]
}
```

### 4. Insights Estratégicos (Dashboard RH)

```java
POST /api/ai/insights-dashboard
{
    "empresaId": "uuid"
}
```

**Response**:

```json
{
  "resumoExecutivo": "A empresa apresenta um clima organizacional satisfatório...",
  "pontosCriticos": [
    "Setor de TI com alto índice de estresse",
    "Baixa aderência a atividades físicas"
  ],
  "pontosPositivos": [
    "Excelente engajamento em consultas psicológicas",
    "Humor geral acima da média"
  ],
  "recomendacoes": [
    "Implementar atividades anti-burnout no setor de TI",
    "Criar campanha de incentivo a atividades físicas"
  ],
  "tendencia": "ESTAVEL_POSITIVA"
}
```

### Configuração

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.7
```

### Custos

Modelo **gpt-4o-mini**:

- Input: $0.15 / 1M tokens
- Output: $0.60 / 1M tokens

**Estimativa**: ~$5-10/mês para 1000 usuários ativos

---

## 🗃️ Migrações de Banco de Dados

WorkWell usa **Flyway** para versionamento do schema do banco de dados.

### Histórico de Migrações

| Versão | Arquivo                                       | Descrição                   |
| ------ | --------------------------------------------- | --------------------------- |
| V1     | `V1__create_empresas_usuarios.sql`            | Empresas e usuários         |
| V2     | `V2__create_consultas_psicologicas.sql`       | Consultas psicológicas      |
| V3     | `V3__add_link_call_consultas.sql`             | Campo link_call             |
| V4     | `V4__create_dashboard_rh_tables.sql`          | Humor, enquetes, atividades |
| V5     | `V5__add_vai_participar_to_participacoes.sql` | Campo vai_participar        |
| V6     | `V6__add_opcoes_resposta_to_enquetes.sql`     | Campo opcoes_resposta       |
| V7     | `V7__create_avaliacoes_profundas.sql`         | Avaliações profundas        |
| V8     | `V8__create_denuncias_eticas.sql`             | Denúncias éticas            |

### Executar Migrações

Migrações são executadas automaticamente no startup da aplicação:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=false
```

### Criar Nova Migração

1. Crie um arquivo em `src/main/resources/db/migration/`
2. Nome: `V{versao}__{descricao}.sql` (ex: `V9__add_campo_xyz.sql`)
3. Escreva o SQL
4. Reinicie a aplicação

**Exemplo**:

```sql
-- V9__add_telefone_usuarios.sql
ALTER TABLE usuarios ADD telefone VARCHAR2(20);
```

### Rollback

Flyway Community não suporta rollback automático. Para reverter:

1. Crie uma nova migração que desfaça as mudanças
2. Ou use `spring.flyway.clean-disabled=false` e limpe o banco (⚠️ CUIDADO)

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/workwell/WorkWell/
├── service/
│   ├── AuthServiceTest.java
│   ├── EmpresaServiceTest.java
│   └── ...
└── WorkWellApplicationTests.java
```

### Executar Testes

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=AuthServiceTest

# Com cobertura (JaCoCo)
mvn clean test jacoco:report
```

### Exemplo de Teste

```java
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void deveAutenticarUsuarioComCredenciaisValidas() {
        // Given
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("usuario@teste.com");
        request.setSenha("senha123");

        // When
        LoginResponseDto response = authService.login(request);

        // Then
        assertNotNull(response.getToken());
        assertEquals("usuario@teste.com", response.getEmail());
    }

    @Test
    void deveLancarExcecaoComCredenciaisInvalidas() {
        // Given
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("usuario@teste.com");
        request.setSenha("senhaErrada");

        // When & Then
        assertThrows(UnauthorizedException.class, () -> {
            authService.login(request);
        });
    }
}
```

---

## 🚢 Deploy

### Opção 1: Deploy em VM/VPS (AWS, Azure, GCP)

#### 1. Preparar o JAR

```bash
mvn clean package -DskipTests
```

#### 2. Transferir para o servidor

```bash
scp target/WorkWell-0.0.1-SNAPSHOT.jar user@servidor:/opt/workwell/
```

#### 3. Configurar variáveis de ambiente

```bash
# /etc/systemd/system/workwell.service
[Unit]
Description=WorkWell Application
After=network.target

[Service]
User=workwell
WorkingDirectory=/opt/workwell
ExecStart=/usr/bin/java -jar WorkWell-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
Environment="JAVA_OPTS=-Xmx512m -Xms256m"
Environment="DB_URL=jdbc:oracle:thin:@host:1521/service"
Environment="DB_USERNAME=usuario"
Environment="DB_PASSWORD=senha"
Environment="JWT_SECRET=seu-secret-forte"
Environment="OPENAI_API_KEY=sua-chave"

[Install]
WantedBy=multi-user.target
```

#### 4. Iniciar o serviço

```bash
sudo systemctl daemon-reload
sudo systemctl enable workwell
sudo systemctl start workwell
sudo systemctl status workwell
```

### Opção 2: Docker

#### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/WorkWell-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

#### Construir e executar

```bash
# Build
docker build -t workwell:latest .

# Run
docker run -d \
  --name workwell \
  -p 8081:8081 \
  -e DB_URL=jdbc:oracle:thin:@host:1521/service \
  -e DB_USERNAME=usuario \
  -e DB_PASSWORD=senha \
  -e JWT_SECRET=seu-secret \
  -e OPENAI_API_KEY=sua-chave \
  workwell:latest
```

### Opção 3: Docker Compose

```yaml
version: "3.8"

services:
  workwell:
    build: .
    ports:
      - "8081:8081"
    environment:
      - DB_URL=jdbc:oracle:thin:@oracle:1521/orcl
      - DB_USERNAME=workwell
      - DB_PASSWORD=senha123
      - JWT_SECRET=seu-secret-forte-aqui
      - OPENAI_API_KEY=sk-proj-sua-chave
      - RABBITMQ_HOST=rabbitmq
      - RABBITMQ_USERNAME=guest
      - RABBITMQ_PASSWORD=guest
    depends_on:
      - rabbitmq
    restart: always

  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      - RABBITMQ_DEFAULT_USER=guest
      - RABBITMQ_DEFAULT_PASS=guest
    restart: always
```

Executar:

```bash
docker-compose up -d
```

### Opção 4: Heroku

```bash
# Login
heroku login

# Criar app
heroku create workwell-app

# Adicionar buildpack
heroku buildpacks:set heroku/java

# Deploy
git push heroku main

# Configurar variáveis
heroku config:set DB_URL=jdbc:oracle:thin:@...
heroku config:set DB_USERNAME=...
heroku config:set JWT_SECRET=...
```

### Opção 5: Kubernetes

#### deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: workwell
spec:
  replicas: 3
  selector:
    matchLabels:
      app: workwell
  template:
    metadata:
      labels:
        app: workwell
    spec:
      containers:
        - name: workwell
          image: workwell:latest
          ports:
            - containerPort: 8081
          env:
            - name: DB_URL
              valueFrom:
                secretKeyRef:
                  name: workwell-secrets
                  key: db-url
            - name: JWT_SECRET
              valueFrom:
                secretKeyRef:
                  name: workwell-secrets
                  key: jwt-secret
          resources:
            requests:
              memory: "256Mi"
              cpu: "250m"
            limits:
              memory: "512Mi"
              cpu: "500m"
---
apiVersion: v1
kind: Service
metadata:
  name: workwell-service
spec:
  type: LoadBalancer
  ports:
    - port: 80
      targetPort: 8081
  selector:
    app: workwell
```

### Checklist de Deploy

- [ ] Configurar variáveis de ambiente de produção
- [ ] Usar secrets para credenciais sensíveis
- [ ] Configurar SSL/TLS (HTTPS)
- [ ] Configurar firewall e security groups
- [ ] Configurar backup do banco de dados
- [ ] Configurar monitoramento (Prometheus, Grafana, DataDog)
- [ ] Configurar logs centralizados (ELK, Splunk)
- [ ] Testar rollback
- [ ] Documentar runbook de operações
- [ ] Configurar health checks e liveness probes

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga os passos:

### 1. Fork o Projeto

```bash
# Clone seu fork
git clone https://github.com/seu-usuario/workwell.git
cd workwell
```

### 2. Crie uma Branch

```bash
git checkout -b feature/minha-feature
# ou
git checkout -b fix/meu-bug
```

### 3. Commit suas Mudanças

Siga o padrão [Conventional Commits](https://www.conventionalcommits.org/):

```bash
git commit -m "feat: adiciona endpoint de relatórios"
git commit -m "fix: corrige bug no cálculo de humor médio"
git commit -m "docs: atualiza README com instruções de deploy"
```

**Tipos**:

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação, ponto-e-vírgula, etc
- `refactor`: Refatoração de código
- `test`: Adição de testes
- `chore`: Tarefas de build, configurações

### 4. Push e Pull Request

```bash
git push origin feature/minha-feature
```

Abra um Pull Request na interface do GitHub com:

- Descrição clara do que foi feito
- Screenshots (se aplicável)
- Referência a issues relacionadas

### Padrões de Código

- **Java**: Seguir [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **Nomenclatura**: camelCase para métodos/variáveis, PascalCase para classes
- **Javadoc**: Documentar métodos públicos
- **Testes**: Escrever testes para novas funcionalidades

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License**.

```
MIT License

Copyright (c) 2025 WorkWell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

<div align="center">

### ⭐ Se você gostou do projeto, deixe uma estrela! ⭐


[⬆ Voltar ao topo](#workwell-)

</div>
