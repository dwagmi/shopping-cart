FROM maven:3.8.6-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean
#RUN mvn -f /home/app/pom.xml compile
#RUN mvn -f /home/app/pom.xml test
RUN mvn -f /home/app/pom.xml package -DskipTests

FROM eclipse-temurin:17
COPY --from=build /home/app/target/demo-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]