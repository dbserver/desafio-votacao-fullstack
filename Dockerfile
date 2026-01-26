# =========================
# STAGE 1 — BUILD
# =========================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copia apenas o projeto polls
COPY polls/pom.xml ./polls/pom.xml
COPY polls/mvnw ./polls/mvnw
COPY polls/.mvn ./polls/.mvn

WORKDIR /app/polls
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copia o código
COPY polls/src ./src

# Build do JAR
RUN ./mvnw clean package -DskipTests


# =========================
# STAGE 2 — RUNTIME
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia somente o JAR final
COPY --from=build /app/polls/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
