FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy the Maven wrapper and POM
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the wrapper executable and download dependencies offline to cache layers
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src src
RUN ./mvnw package -DskipTests

# Final runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
