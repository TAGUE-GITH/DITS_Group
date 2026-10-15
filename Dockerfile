# Image de l'API (Render) : construite a partir du dossier backend/
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY backend/mvnw .
COPY backend/.mvn .mvn
COPY backend/pom.xml .
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline
COPY backend/src ./src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
