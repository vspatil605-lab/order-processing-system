# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Working directory inside container
WORKDIR /app

# Copy your JAR from target folder
COPY target/order-processing-system-0.0.1-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
