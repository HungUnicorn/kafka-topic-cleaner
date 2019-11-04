FROM adoptopenjdk:11-jre-hotspot

ENV USER kafka-topic-cleaner
RUN useradd -m ${USER}

ARG APP_PATH=/opt/kafka-topic-cleaner

RUN mkdir -p $APP_PATH
WORKDIR $APP_PATH

ENV JAVA_TOOL_OPTIONS="-XX:+UseG1GC -Duser.timezone=UTC -Xmx256m -Xms256m -XX:MaxMetaspaceSize=128m"

COPY src/main/resources/ $APP_PATH/
COPY target/kafka-topic-cleaner-*.jar $APP_PATH/kafka-topic-cleaner.jar

USER ${USER}

CMD ["java", "-jar", "kafka-topic-cleaner.jar"]
