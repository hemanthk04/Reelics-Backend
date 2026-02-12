# =========================
# BUILD STAGE
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# =========================
# RUNTIME STAGE
# =========================
FROM eclipse-temurin:21-jre-jammy

# ---- Install system dependencies ----
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    ffmpeg \
    curl \
 && rm -rf /var/lib/apt/lists/*

# ---- Install yt-dlp (official binary) ----
RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp \
    -o /usr/local/bin/yt-dlp \
 && chmod +x /usr/local/bin/yt-dlp

# ---- Install Python Google libs (SUPPORTED HERE) ----
RUN pip3 install --no-cache-dir \
    google-api-python-client \
    google-auth \
    google-auth-oauthlib

WORKDIR /app

# ---- Copy Spring Boot app ----
COPY --from=builder /app/target/reelics-0.0.1-SNAPSHOT.jar app.jar

# ---- Copy uploader tools ----
COPY tools /tools

EXPOSE 8078

ENTRYPOINT ["java", "-jar", "app.jar"]
