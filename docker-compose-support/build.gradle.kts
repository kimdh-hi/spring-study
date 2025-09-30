plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("plugin.jpa") version "1.9.25"
}

group = "com.study"
version = "0.0.1-SNAPSHOT"
description = "docker-compose-support"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
//  developmentOnly("org.springframework.boot:spring-boot-docker-compose")
  testAndDevelopmentOnly("org.springframework.boot:spring-boot-docker-compose")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
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
