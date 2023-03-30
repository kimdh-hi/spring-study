import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.0.5"
  id("io.spring.dependency-management") version "1.1.0"
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
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.mariadb:r2dbc-mariadb:1.1.4")
  implementation("org.mariadb.jdbc:mariadb-java-client")

  val coroutineVersion = "1.6.4"
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:${coroutineVersion}")

  implementation("io.netty:netty-resolver-dns-native-macos:4.1.84.Final:osx-aarch_64")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
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
