import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val restdocsApiSpecVersion = "0.16.2"

plugins {
  id("org.springframework.boot") version "3.0.6"
  id("io.spring.dependency-management") version "1.1.0"
  id("com.epages.restdocs-api-spec") version "0.16.2"
  id("org.hidetake.swagger.generator") version "2.18.2"
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

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
  testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
  testImplementation("com.epages:restdocs-api-spec-mockmvc:$restdocsApiSpecVersion")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
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

tasks.withType(GenerateSwaggerUI::class.java) {
  dependsOn("openapi3")
  delete { file("src/main/resources/static/docs/") }
  copy {
    println("open-api.json copy...")
    from("build/api-spec/")
    into("src/main/resources/static/docs/")
  }
}

tasks.bootJar {
  dependsOn(":openapi3")
}

openapi3 {
  setServer("http://localhost:8080")
  title = "swagger doc"
  description = "Spring REST Docs with SwaggerUI."
  version = "0.0.1"
  format = "json"
  outputFileNamePrefix = "swagger"
}