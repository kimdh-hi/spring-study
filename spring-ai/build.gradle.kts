plugins {
  kotlin("jvm") version "2.3.21"
  kotlin("plugin.spring") version "2.3.21"
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.study"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

extra["springAiVersion"] = "2.0.0-RC1"

dependencies {
  implementation("org.springframework.ai:spring-ai-starter-model-openai")
  implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
  implementation("org.springframework.ai:spring-ai-vector-store-advisor")
//  implementation("org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc")
//  implementation("org.springframework.ai:spring-ai-starter-model-chat-memory-repository-cassandra")
  implementation("org.springframework.ai:spring-ai-pdf-document-reader")
  implementation("org.springframework.ai:spring-ai-rag")

  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("io.netty:netty-resolver-dns-native-macos:4.2.7.Final:osx-aarch_64")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
