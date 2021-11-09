FROM openjdk:11
ADD /build/libs/gndi-spring-backend-0.0.1-SNAPSHOT.jar gndi-spring-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gndi-spring-backend.jar"]
