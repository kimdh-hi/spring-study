import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.1.0"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.8.21"
  kotlin("plugin.spring") version "1.8.21"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-batch")
  implementation("com.navercorp.spring:spring-boot-starter-batch-plus-kotlin:1.0.0")

  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  runtimeOnly("com.h2database:h2")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.batch:spring-batch-test")
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
