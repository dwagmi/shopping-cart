# Uses pre-built jar in target dir
FROM eclipse-temurin:17
ADD target/*.jar app.jar
EXPOSE 8080
CMD java -jar app.jar
