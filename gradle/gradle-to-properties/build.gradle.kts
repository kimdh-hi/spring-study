import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
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
    val settingsProperty = project.properties["settingsPropertiesPath"] ?: "settings/settings.properties"

    /**
     * ext.set : ext에 custom property 등록
     */
    val settingsProperties = mutableMapOf<String, String>()
    loadProperties(settingsProperty as String).forEach { entry ->
        settingsProperties[entry.key as String] = entry.value as String
    }

    return settingsProperties
}

//fun loadSettingsToProperties() {
//    val settingsProperty = project.properties["settingsPropertiesPath"] ?: "settings/settings.properties"
//
//    /**
//     * ext.set : ext에 custom property 등록
//     */
//    loadProperties(settingsProperty as String).forEach { entry ->
//        ext.set(entry.key as String, entry.value)
//    }
//}

fun printAllProjectProperties() {
    /**
     * ext 에 등록된 property 에 접근시 project.properties 를 사용한다.
     */
    project.properties.forEach { entry ->
        run {
            println("key: ${entry.key} | value: ${entry.value}")
        }
    }
}

tasks.processResources {
    val properties = loadSettingsToProperties()
//    loadSettingsToProperties()

    filesMatching("**/application.yml") {
        expand(properties)
//        expand(project.properties)
    }
}