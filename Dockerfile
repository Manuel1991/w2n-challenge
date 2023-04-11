FROM maven:3.8.5-openjdk-17-slim

WORKDIR /app

COPY . .

ENV PORT=${PORT}
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

RUN mvn dependency:go-offline
RUN mvn clean package

CMD ["java", "-jar", "target/w2n_challenge-0.0.1-SNAPSHOT.jar"]