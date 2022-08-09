FROM openjdk:11-jre-slim

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PORT=23221 \
    PROFILES="default" \
    JAR_NAME="task.jar"

ADD /target/*.jar /$JAR_NAME

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /$JAR_NAME --spring.profiles.active=$PROFILES"]

EXPOSE $PORT