import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("jvm")
  kotlin("plugin.spring")
  kotlin("kapt")
  kotlin("plugin.jpa")
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

noArg {
  annotation("jakarta.persistence.Entity")
  annotation("com.toy.springboot3.common.NoArg")
  invokeInitializers = true
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

private val querydslVersion = dependencyManagement.importedProperties["querydsl.version"]

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.mariadb.jdbc:mariadb-java-client")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
//  implementation("org.springframework.boot:spring-boot-starter-security")

  implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")  // querydsl-jpa
  implementation("com.querydsl:querydsl-apt:$querydslVersion:jakarta")
  kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  runtimeOnly("com.h2database:h2")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
//  testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
    languageVersion = "1.9"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

fun loadProperties() {
  val path = "settings/settings.properties"

  loadProperties(path).forEach { key, value ->
    println(">> $key: $value")
    ext[key.toString()] = value
  }
}

project.afterEvaluate {
  println(" >> afterEvaluate")
  loadProperties()
}

tasks.processResources {
  filesMatching("**/application.yml") {
    expand(project.properties)
  }
}
