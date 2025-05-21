# Etapa 1: build usando Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Cria a pasta de trabalho e copia apenas a pasta "back"
WORKDIR /app
COPY back ./back

# Compila o projeto Spring Boot dentro da pasta back
RUN mvn -f back/pom.xml clean package -DskipTests

# Etapa 2: imagem final com JDK apenas (sem Maven)
FROM eclipse-temurin:17-jdk

# Cria a pasta de trabalho no container final
WORKDIR /app

# Copia o .jar gerado da etapa de build
COPY --from=build /app/back/target/*.jar app.jar

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
