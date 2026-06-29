plugins {
  kotlin("jvm") version "2.3.21"
  kotlin("plugin.spring") version "2.3.21"
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("plugin.jpa") version "2.3.21"
}

group = "com.study"
version = "0.0.1-SNAPSHOT"
description = "spring-monitoring"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(25)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  // web + jpa + kotlin
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  // --- Observability (LGTM+P) ---
  // Actuator: /actuator/prometheus, /actuator/health
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  // METRICS: Prometheus scrape endpoint (Prometheus -> Mimir)
  implementation("io.micrometer:micrometer-registry-prometheus")
  // TRACES: Micrometer Tracing -> OpenTelemetry bridge + OTLP exporter (-> Tempo)
  implementation("io.micrometer:micrometer-tracing-bridge-otel")
  implementation("io.opentelemetry:opentelemetry-exporter-otlp")
  // LOGS: ship directly to Loki (not in Spring Boot BOM -> explicit version)
  implementation("com.github.loki4j:loki-logback-appender:2.0.3")
  // PROFILES: continuous profiling agent (async-profiler based) -> Pyroscope
  implementation("io.pyroscope:agent:2.5.1")

  runtimeOnly("com.mysql:mysql-connector-j")

  testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
