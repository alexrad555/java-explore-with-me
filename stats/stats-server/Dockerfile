FROM amazoncorretto:11
COPY target/explore-with-me-stats-server-0.0.1-SNAPSHOT.jar app/explore-with-me-stats-server.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app/explore-with-me-stats-server.jar"]
