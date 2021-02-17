FROM openjdk:8-jdk-alpine

COPY target/georgApi-thorntail.jar /usr/src/myapp/
WORKDIR /usr/src/myapp


ENTRYPOINT [""]
EXPOSE 8080
CMD ["java", "-jar", "georgApi-thorntail.jar", "-Sinitdata"]
