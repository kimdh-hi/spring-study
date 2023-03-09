import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  id("org.springframework.boot") version "3.0.4"
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
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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

    println("$key: [$value]")
    ext.set(key, value)
  }
}

project.afterEvaluate {
  loadSettingsProperties()
}

tasks.processResources {
  filesMatching("application.yml") {
    expand(project.properties)
  }
}
