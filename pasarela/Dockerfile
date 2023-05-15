FROM maven:3.8.5-jdk-11 AS builder

WORKDIR /usuarios-pasarela/
COPY usuarios-pasarela/pom.xml .
RUN mvn -e -B dependency:resolve
COPY usuarios-pasarela/src ./src
RUN mvn install

WORKDIR /pasarela/
COPY pasarela/pom.xml .
RUN mvn -e -B dependency:resolve
COPY pasarela/src ./src
RUN mvn package

FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=builder /pasarela/target/*.jar app.jar

EXPOSE 8090
CMD ["java", "-jar", "app.jar"]
