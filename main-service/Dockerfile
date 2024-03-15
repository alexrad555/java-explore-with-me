FROM amazoncorretto:11
COPY target/explore-with-me-main-service-0.0.1-SNAPSHOT.jar app/explore-with-me-main-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app/explore-with-me-main-service.jar"]
