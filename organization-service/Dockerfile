#Official Docker Image for OpenJDK
FROM eclipse-temurin:17

#Author email metadata
LABEL mentainer="jorge.sansone.@gmail.com"

#When start Docker container this app directory will be created
WORKDIR /app

#copy app .jar to Docker app folder
COPY target/organization-service-0.0.1-SNAPSHOT.jar /app/organization-service.jar

#Parameters to run the java command
ENTRYPOINT ["java", "-jar", "organization-service.jar"]
