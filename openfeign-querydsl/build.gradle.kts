plugins {
  kotlin("jvm") version "2.3.0"
  kotlin("plugin.spring") version "2.3.0"
  kotlin("plugin.jpa") version "2.3.0"
  id("org.springframework.boot") version "4.0.2"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.devtools.ksp") version "2.3.5"
//  kotlin("kapt") version "2.3.0"
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

private val queryDslVersion = "7.1"
dependencies {
  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
  ksp("io.github.openfeign.querydsl:querydsl-ksp-codegen:$queryDslVersion")
//  implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDslVersion")
//  kapt("io.github.openfeign.querydsl:querydsl-apt:$queryDslVersion:jpa")

  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("com.mysql:mysql-connector-j")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
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
