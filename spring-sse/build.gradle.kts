plugins {
  kotlin("jvm") version "2.4.20"
  kotlin("plugin.spring") version "2.4.20"
  id("org.springframework.boot") version "4.1.1"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
description = "spring-sse"

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
  implementation("io.projectreactor:reactor-core")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}
