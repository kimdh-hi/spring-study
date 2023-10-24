import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.1.5"
  id("io.spring.dependency-management") version "1.1.3"
  kotlin("jvm") version "1.8.22"
  kotlin("plugin.spring") version "1.8.22"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "2022.0.4"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-amqp")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springframework.cloud:spring-cloud-starter-config")
  implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.amqp:spring-rabbit-test")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
