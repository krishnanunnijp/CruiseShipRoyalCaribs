# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Step 1: Copy and build sharedlib
COPY sharedlib /app/sharedlib
RUN mvn -f /app/sharedlib/pom.xml install -DskipTests

# Step 2: Copy and build ShoreProxy (which depends on sharedlib)
COPY ShoreProxy /app/ShoreProxy
RUN mvn -f /app/ShoreProxy/pom.xml clean package -DskipTests

# ----------- Runtime Stage -----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/ShoreProxy/target/ShoreProxy-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
