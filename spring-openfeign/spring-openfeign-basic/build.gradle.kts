import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.4.3"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

java {
  extra["springCloudVersion"] = "2024.0.0"
}

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "2024.0.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

//  implementation("io.github.openfeign:feign-hc5")
//  implementation("io.github.openfeign:feign-okhttp")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
