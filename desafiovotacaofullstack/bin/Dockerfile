FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY . .

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw
RUN ./mvnw dependency:resolve
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/desafiovotacaofullstack-0.0.1-SNAPSHOT.jar"]


