# ── Stage 1: build ───────────────────────────────────────────────────────────
FROM --platform=linux/amd64 ubuntu:22.04 AS builder

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-17-jdk maven ca-certificates \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /build

# Cache dependency resolution separately from source compilation
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode dependency:go-offline -q

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode package -DskipTests -q

# ── Stage 2: runtime ─────────────────────────────────────────────────────────
FROM --platform=linux/amd64 ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-17-jre-headless ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Run as a non-root user for security
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser
USER appuser

WORKDIR /app

COPY --from=builder /build/target/fix-binary-converter-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
