plugins {
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("jvm") version "2.4.10"
  kotlin("plugin.spring") version "2.4.10"
  kotlin("plugin.jpa") version "2.4.10"
  kotlin("plugin.noarg") version "2.4.10"
  kotlin("plugin.serialization") version "2.4.10"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

kotlin {
  jvmToolchain(21)
  compilerOptions {
    freeCompilerArgs.add("-Xjsr305=strict")
  }
}

repositories {
  mavenCentral()
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.Embeddable")
  annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
  annotation("jakarta.persistence.Entity")
  annotation("com.toy.springcacheex.common.NoArg")
  invokeInitializers = true
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.session:spring-session-data-redis")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
  implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("com.h2database:h2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
