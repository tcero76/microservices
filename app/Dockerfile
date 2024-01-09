FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY ${DEPENDENCY}/libs/app.jar /app/lib/app.jar
ENTRYPOINT ["java","-jar","/app/lib/app.jar"]