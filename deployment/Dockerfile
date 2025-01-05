FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY applications/app-service/build/libs/reactive-workshop.jar reactive-workshop.jar

ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar reactive-workshop.jar" ]
