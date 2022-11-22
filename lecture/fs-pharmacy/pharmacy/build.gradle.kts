import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.5"
  id("io.spring.dependency-management") version "1.0.15.RELEASE"
  id("groovy")
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  kotlin("plugin.jpa") version "1.6.21"
}

group = "com.lecture"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  //spock
  testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
  testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")

  // 런타임에 클래스 기반 spock mock 을 만들기 위
  testImplementation("net.bytebuddy:byte-buddy:1.12.10")

  //testcontainers
  testImplementation("org.testcontainers:spock:1.17.1")
  testImplementation("org.testcontainers:mariadb:1.17.1")
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

tasks.bootJar {
  archiveFileName.set("app.jar")
}