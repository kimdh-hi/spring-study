import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.spring") version "2.4.20"
    kotlin("plugin.jpa") version "2.4.20"
    id("org.springframework.boot") version "4.1.1"
    id("com.epages.restdocs-api-spec") version "0.20.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

val swaggerUiVersion = "5.32.15"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    testImplementation(platform(SpringBootPlugin.BOM_COORDINATES))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.webjars:swagger-ui:$swaggerUiVersion")
    runtimeOnly("org.webjars:webjars-locator-lite")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-restdocs")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.20.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openapi3 {
    title = "User API"
    description = "User CRUD API generated from Spring REST Docs"
    version = "1.0.0"
    format = "json"
    // Relative server URL so Swagger UI calls whichever origin serves it (local, dev, prod)
    setServer("/")
}

val updateApiSpec by tasks.registering(Copy::class) {
    group = "documentation"
    description = "Copies the generated OpenAPI spec into src/main/resources/static/docs"
    dependsOn("openapi3")
    from(layout.buildDirectory.file("api-spec/openapi3.json"))
    into("src/main/resources/static/docs")
    doLast {
        val specFile = file("src/main/resources/static/docs/openapi3.json")
        @Suppress("UNCHECKED_CAST")
        val spec = JsonSlurper().parse(specFile) as MutableMap<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val components = spec.getOrPut("components") { mutableMapOf<String, Any?>() } as MutableMap<String, Any?>
        components["securitySchemes"] = mapOf(
            "ApiKey" to mapOf("type" to "apiKey", "in" to "header", "name" to "X-API-KEY"),
        )
        spec["security"] = listOf(mapOf("ApiKey" to emptyList<String>()))
        specFile.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(spec)) + "\n")
    }
}
