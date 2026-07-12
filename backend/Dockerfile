FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget tesseract-ocr tesseract-ocr-eng tesseract-ocr-fra tesseract-ocr-ara \
    && rm -rf /var/lib/apt/lists/*
RUN useradd --system --uid 10001 yazidcv
WORKDIR /app
COPY --from=build /build/target/backend-*.jar app.jar
RUN mkdir -p /app/data/uploads && chown -R yazidcv:yazidcv /app
USER yazidcv
EXPOSE 8080
HEALTHCHECK --interval=20s --timeout=3s --start-period=30s --retries=3 CMD wget -qO- http://localhost:8080/actuator/health/readiness || exit 1
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","app.jar"]
