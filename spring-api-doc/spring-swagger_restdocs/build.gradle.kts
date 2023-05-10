import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

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

swaggerSources {
  create("sample") {
    setInputFile(file("${project.buildDir}/api-spec/openapi3.yaml"))
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("com.h2database:h2")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
  swaggerUI("org.webjars:swagger-ui:4.11.1")
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

tasks.withType<GenerateSwaggerUI> {
  dependsOn("openapi3")
}

tasks.register<Copy>("copySwaggerUI") {
  dependsOn("generateSwaggerUISample")
  val generateSwaggerUISampleTask = tasks.named("generateSwaggerUISample", GenerateSwaggerUI::class).get()
  from(generateSwaggerUISampleTask.outputDir)
  into("${project.buildDir}/resources/main/static/docs")
}
tasks.withType<BootJar> {
  dependsOn("copySwaggerUI")
}

openapi3 {
  setServer("http://localhost:8080")
  title = "restdocs-swagger API Documentation"
  description = "Spring REST Docs with SwaggerUI."
  version = "0.0.1"
  format = "yaml"
}