plugins {
  kotlin("jvm") version "2.3.21"
  kotlin("plugin.spring") version "2.3.21"
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.cloud.tools.jib") version "3.5.4"
}

group = "com.study"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(25)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
}

jib {
  from {
    image = "eclipse-temurin:25-jre"
  }
  to {
    image = "spring-jib:latest"
    tags = setOf(version.toString())
  }
  container {
    ports = listOf("8080")
    user = "1000:1000"
    jvmFlags = listOf("-XX:MaxRAMPercentage=75")
    creationTime = "USE_CURRENT_TIMESTAMP"
  }
}
