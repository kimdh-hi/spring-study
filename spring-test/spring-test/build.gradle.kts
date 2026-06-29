import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
  kotlin("jvm") version "2.3.21"
  kotlin("plugin.spring") version "2.3.21"
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  testImplementation("org.wiremock:wiremock-standalone:3.13.1")
  testImplementation("com.marcinziolo:kotlin-wiremock:2.1.1")
  testImplementation("com.ninja-squad:springmockk:4.0.2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.15")
  testImplementation("com.navercorp.fixturemonkey:fixture-monkey-datafaker:1.1.15")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
  testLogging {
    showStandardStreams = true
    showCauses = true
    showExceptions = true
    showStackTraces = true
    exceptionFormat = TestExceptionFormat.FULL
  }
}
