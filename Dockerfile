FROM hub.c.163.com/library/java:8-alpine

MAINTAINER WangWei wang_wei608@163.com

ADD server/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]