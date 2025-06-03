# Stage 1: Build the application
FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Create a non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the jar file
COPY --from=builder /app/target/*.jar app.jar

# Change ownership of the jar file
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
