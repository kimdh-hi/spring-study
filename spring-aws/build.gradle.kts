import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  id("org.springframework.boot") version "3.0.5"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.7.22"
  kotlin("plugin.spring") version "1.7.22"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.cloud:spring-cloud-starter-aws")
  implementation("com.amazonaws:aws-java-sdk-s3:1.12.446")
  implementation("org.apache.commons:commons-lang3")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
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

fun loadSettingsProperties() {
  val settingsProperty = "settings/settings.properties"
  loadProperties(settingsProperty).forEach { entry ->
    val key = entry.key as String
    val value = entry.value as String
    ext.set(key, value)
  }

  ext.set("storagePath", project.rootDir.path)
}

project.afterEvaluate {
  loadSettingsProperties()
}

tasks.processResources {
  filesMatching("**/application.yml") {
    expand(project.properties)
  }
}