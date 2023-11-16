import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.1.5"
  id("io.spring.dependency-management") version "1.1.3"
  id("nu.studer.jooq") version "8.2"
  kotlin("jvm") version "1.8.22"
  kotlin("plugin.spring") version "1.8.22"
}

jooq {
  configurations {
    create("main") {
      generateSchemaSourceOnCompilation.set(true)
      jooqConfiguration.apply {
        jdbc.apply {
          driver = 'com'
        }
      }

    }
  }
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  runtimeOnly("com.h2database:h2")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
