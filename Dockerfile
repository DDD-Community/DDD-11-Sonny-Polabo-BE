FROM openjdk:21
ARG JAR_FILE=./build/libs/*SNAPSHOT.jar
ARG PASSWORD
COPY ${JAR_FILE} polabo.jar
ENV JASYPT_ENCRYPTOR_PASSWORD=${PASSWORD}
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${ENVIRONMENT_VALUE} -jar /polabo.jar.jar", "-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}"]