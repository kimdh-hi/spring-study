import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.4"
  id("io.spring.dependency-management") version "1.0.14.RELEASE"
  id("nu.studer.jooq") version "7.1.1"
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
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  runtimeOnly("com.h2database:h2")
  jooqGenerator("com.h2database:h2")
  jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
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

jooq {
  version.set("3.16.4")
  edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

  configurations {
    create("main") {
      generateSchemaSourceOnCompilation.set(true)

      jooqConfiguration.apply {
        jdbc.apply {
          driver = "org.h2.Driver"
          url = "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE;"
          user = "sa"
        }

        generator.apply {
          name = "org.jooq.codegen.KotlinGenerator"

          database.apply {
            name = "org.jooq.meta.h2.H2Database"
            inputSchema = "public"
          }
          generate.apply {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = true
          }
          target.apply {
            packageName = "com.tr.domain.generated"
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}
