#FROM openjdk:25-oraclelinux8
FROM khipu/openjdk21-alpine

COPY target/paul-api.jar /apapun/paul-api.jar
CMD ["java","-jar","/apapun/paul-api.jar"]