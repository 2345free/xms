FROM harbor.itzone123.com/envimages/alpine-java-unlimited-wkhtmltopdf:1.0
MAINTAINER MrLuo <2345free@sina.com>

ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=none","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]