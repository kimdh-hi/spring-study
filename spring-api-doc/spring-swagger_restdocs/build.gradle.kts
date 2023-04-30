import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.0.6"
  id("io.spring.dependency-management") version "1.1.0"
  id("org.asciidoctor.convert") version "2.2.1"
  kotlin("jvm") version "1.7.22"
  kotlin("plugin.spring") version "1.7.22"
  kotlin("plugin.jpa") version "1.7.22"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("com.h2database:h2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
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

tasks.test {
  outputs.dir(snippetsDir)
}

tasks.asciidoctor {
  inputs.dir(snippetsDir)
  dependsOn(test)
}
