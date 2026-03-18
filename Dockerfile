# ── Stage 1: build ───────────────────────────────────────────────────────────
FROM maven:3.9-amazoncorretto-17 AS builder

WORKDIR /build

# Cache dependency resolution separately from source compilation
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode dependency:go-offline -q

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode package -DskipTests -q

# ── Stage 2: runtime ─────────────────────────────────────────────────────────
FROM amazoncorretto:17-alpine

# Run as a non-root user for security
RUN addgroup -S appgroup && adduser -S -G appgroup appuser
USER appuser

WORKDIR /app

COPY --from=builder /build/target/fix-binary-converter-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
