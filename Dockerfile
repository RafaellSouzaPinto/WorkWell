# Multi-stage build para otimizar o tamanho da imagem
# Estágio 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas os arquivos de dependências primeiro (para cache)
COPY pom.xml .

# Baixa as dependências (isso será cacheado se o pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copia o resto do código
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Estágio 2: Imagem de runtime
FROM eclipse-temurin:17-jre-alpine

# Instala timezone data para suportar America/Sao_Paulo
RUN apk add --no-cache tzdata

# Define variáveis de ambiente
ENV TZ=America/Sao_Paulo
ENV SPRING_PROFILES_ACTIVE=prod

# Cria usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR da aplicação do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Muda o proprietário do arquivo
RUN chown spring:spring app.jar

# Muda para o usuário não-root
USER spring:spring

# Expõe a porta da aplicação
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-XX:+UseContainerSupport", \
  "-jar", \
  "app.jar"]

