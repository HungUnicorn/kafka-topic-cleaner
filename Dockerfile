FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package


FROM adoptopenjdk:11-jre-hotspot

ENV USER kafka-topic-cleaner
RUN useradd -m ${USER}

ARG APP_PATH=/opt/kafka-topic-cleaner

RUN mkdir -p $APP_PATH
WORKDIR $APP_PATH

ENV JAVA_TOOL_OPTIONS="-XX:+UseG1GC -Duser.timezone=UTC -Xmx256m -Xms256m -XX:MaxMetaspaceSize=128m"

COPY src/main/resources/ $APP_PATH/
COPY --from=build /home/app/target/kafka-topic-cleaner-*.jar $APP_PATH/kafka-topic-cleaner.jar

USER ${USER}

CMD ["java", "-jar", "kafka-topic-cleaner.jar"]
