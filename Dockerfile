FROM maven:3.8.5-openjdk-17-slim

WORKDIR /app

COPY . .
#COPY ./target ./target

RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/w2n_challenge-0.0.1-SNAPSHOT.jar"]