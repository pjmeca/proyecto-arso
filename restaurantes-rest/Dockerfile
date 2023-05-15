FROM maven:3.8.5-jdk-11 AS builder

WORKDIR /restaurantes/
COPY restaurantes/pom.xml .
RUN mvn -e -B dependency:resolve
COPY restaurantes/src ./src
RUN mvn install

WORKDIR /restaurantes-rest/
COPY restaurantes-rest/pom.xml .
RUN mvn -e -B dependency:resolve
COPY restaurantes-rest/src ./src
RUN mvn package

FROM tomcat:9.0.58-jdk11
WORKDIR /usr/local/tomcat/webapps/
COPY --from=builder /restaurantes-rest/target/restaurantes-rest.war ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
