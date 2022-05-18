#docker
FROM public.ecr.aws/docker/library/openjdk:11-oracle
LABEL maintainer="audit-authentication-main.net"
ADD target/authentication-0.0.1-SNAPSHOT.jar audit-authentication-main.jar
ENTRYPOINT ["java","-jar","audit-authentication-main.jar"]


