import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

plugins {
  kotlin("jvm") version "2.3.21"
  kotlin("plugin.spring") version "2.3.21"
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("plugin.jpa") version "2.3.21"
  kotlin("kapt") version "2.3.21"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.Embeddable")
  annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
  annotation("jakarta.persistence.Entity")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}


private val queryDslVersion = "7.0"
dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  //openfeign/querydsl
  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
//  ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:$queryDslVersion")
  kapt("io.github.openfeign.querydsl:querydsl-apt:$queryDslVersion:jpa")

  //querydsl/querydsl
//  implementation("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
//  implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
//  kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

  runtimeOnly("com.h2database:h2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    showStandardStreams = true
    exceptionFormat = FULL
  }
}
