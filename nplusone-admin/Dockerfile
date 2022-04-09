FROM adoptopenjdk:11-jre-hotspot
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /logs
COPY ${JAR_FILE:-build/libs/*.jar} app.jar
ENTRYPOINT exec java ${JAVA_OPTS} -jar /app.jar
EXPOSE 8080