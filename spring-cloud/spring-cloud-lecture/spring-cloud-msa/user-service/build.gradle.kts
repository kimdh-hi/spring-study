import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.7"
  id("io.spring.dependency-management") version "1.0.15.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  kotlin("plugin.noarg") version "1.6.21"
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.Embeddable")
  annotation("javax.persistence.MappedSuperclass")
}

noArg {
  annotation("javax.persistence.Entity")
}


group = "com.lecture"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "2021.0.5"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.cloud:spring-cloud-starter-config")
  implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
  implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
  implementation("io.jsonwebtoken:jjwt:0.9.1")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
  runtimeOnly("com.h2database:h2")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
