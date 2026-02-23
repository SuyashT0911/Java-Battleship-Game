FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY . /app

RUN javac BattleshipServer.java

EXPOSE 8080

CMD ["java", "BattleshipServer"]
