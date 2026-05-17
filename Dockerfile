FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copy only the POM first
COPY pom.xml .
# Download dependencies (this caches them in the Docker layer)
RUN mvn dependency:go-offline
# Now copy the source code
COPY src src
# Build the application
RUN mvn package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]