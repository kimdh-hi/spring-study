plugins {
  kotlin("jvm") version "2.3.21" apply false
  kotlin("plugin.spring") version "2.3.21" apply false
  kotlin("plugin.jpa") version "2.3.21" apply false
  id("org.springframework.boot") version "4.1.0" apply false
  id("io.spring.dependency-management") version "1.1.7" apply false
}

subprojects {
  group = "com.study"
  version = "0.0.1-SNAPSHOT"

  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
  apply(plugin = "org.springframework.boot")
  apply(plugin = "io.spring.dependency-management")

  configure<JavaPluginExtension> {
    toolchain {
      languageVersion = JavaLanguageVersion.of(25)
    }
  }

  repositories {
    mavenCentral()
  }

  dependencies {
    "implementation"("org.springframework.boot:spring-boot-starter-data-jpa")
    "implementation"("org.springframework.boot:spring-boot-starter-webmvc")
    "implementation"("tools.jackson.module:jackson-module-kotlin")
    "implementation"("org.jetbrains.kotlin:kotlin-reflect")
    "runtimeOnly"("com.mysql:mysql-connector-j")
    "testImplementation"("org.springframework.boot:spring-boot-starter-data-jpa-test")
    "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit5")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}
