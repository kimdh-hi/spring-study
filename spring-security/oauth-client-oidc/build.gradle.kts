import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  id("org.springframework.boot") version "2.7.2"
  id("io.spring.dependency-management") version "1.0.12.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  kotlin("plugin.jpa") version "1.6.21"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
  implementation("org.apache.commons:commons-lang3")
  implementation("commons-io:commons-io:2.11.0")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

  implementation("com.auth0:java-jwt:4.0.0")
  implementation("io.jsonwebtoken:jjwt:0.9.1")

  implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  runtimeOnly("com.h2database:h2")
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

fun loadSettingsToProperties(): MutableMap<String, String> {
  val settingsProperty =  "settings/secrets.properties"

  val settingsProperties = mutableMapOf<String, String>()
  loadProperties(settingsProperty).forEach { entry ->
    settingsProperties[entry.key as String] = entry.value as String
  }

  return settingsProperties
}

tasks.processResources {
  val properties = loadSettingsToProperties()

  filesMatching("**/application.yml") {
    expand(properties)
  }
}