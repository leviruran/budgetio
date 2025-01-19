# Use the official OpenJDK 17 image as the base image for building the app
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /app

# Install Maven
RUN apt-get update \
    && apt-get install -y --no-install-recommends apt-utils \
    && apt-get install -y maven \
    && rm -rf /var/lib/apt/lists/*

# Copy the Maven configuration file
COPY pom.xml .

# Download dependencies to avoid copying the whole source code unnecessarily
RUN mvn dependency:go-offline

# Now copy the actual source code of the application
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Start a new stage for the runtime image
FROM openjdk:17-jdk-slim as runtime

# Set the working directory
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
