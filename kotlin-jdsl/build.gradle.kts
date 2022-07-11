import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.1"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  kotlin("plugin.jpa") version "1.6.21"
  kotlin("plugin.noarg") version "1.6.21"
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.Embeddable")
  annotation("javax.persistence.MappedSuperclass")
}

noArg {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.Embeddable")
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
//  implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-core:2.0.4.RELEASE")
//  implementation("com.linecorp.kotlin-jdsl:hibernate-kotlin-jdsl:2.0.4.RELEASE")
//  implementation("org.hibernate:hibernate-core:2.0.4.RELEASE")

  val kotlinJdslVersion = "2.0.4.RELEASE"
  implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:${kotlinJdslVersion}") {
    exclude(group = "com.linecorp.kotlin-jdsl", module = "hibernate-kotlin-jdsl")
  }
  implementation("com.linecorp.kotlin-jdsl:eclipselink-kotlin-jdsl:${kotlinJdslVersion}")
//  dependencies {
//    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:${kotlinJdslVersion}") {
//      exclude(group = "com.linecorp.kotlin-jdsl", module = "hibernate-kotlin-jdsl")
//    }
//    implementation("com.linecorp.kotlin-jdsl:eclipselink-kotlin-jdsl:${kotlinJdslVersion}")
//  }

  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
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
