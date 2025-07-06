# Use official Maven image with Java 17
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Final image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy JAR from builder stage
COPY --from=build /app/target/*.jar app.jar

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]