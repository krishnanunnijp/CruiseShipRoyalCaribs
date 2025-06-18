# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

# Copy only ShipProxy and sharedlib (or entire root if needed)
COPY sharedlib /build/sharedlib
COPY ShipProxy /build/ShipProxy

# Build the sharedlib first
RUN cd sharedlib && mvn install -DskipTests

# Then build ShipProxy
WORKDIR /build/ShipProxy
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/ShipProxy/target/ShipProxy-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
