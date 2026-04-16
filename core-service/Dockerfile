# Stage 1: Create a minimized Java runtime with jlink
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /opt

RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jdk

# Stage 2: Run the application with the minimized runtime
FROM alpine:latest

COPY --from=build /opt/jdk /opt/jdk

ENV JAVA_HOME=/opt/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY target/core-service-1.0.0.jar core-service-1.0.0.jar

ENTRYPOINT ["java","-Duser.timezone=Asia/Ho_Chi_Minh","-jar","core-service-1.0.0.jar"]
