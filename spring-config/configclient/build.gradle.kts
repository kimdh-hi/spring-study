plugins {
  kotlin("jvm") version "2.4.10"
  kotlin("plugin.spring") version "2.4.10"
  id("org.springframework.boot") version "4.1.1"
  id("io.spring.dependency-management") version "1.1.7"
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

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.3")
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.cloud:spring-cloud-starter-config")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
