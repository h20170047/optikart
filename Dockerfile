FROM openjdk:8
EXPOSE 8080
ADD build/libs/optikart.jar optikart.jar
ENTRYPOINT ["java", "-jar", "/optikart.jar"]