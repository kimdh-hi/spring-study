import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("plugin.noarg") version "1.9.25"
	kotlin("kapt") version "1.9.25"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
	annotation("jakarta.persistence.Entity")
	annotation("com.toy.springquerydsl.common.NoArg")
	invokeInitializers = true
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	//querydsl
	implementation("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")
	implementation("com.querydsl:querydsl-jpa:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")
	kapt("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
