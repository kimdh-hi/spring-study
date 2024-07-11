plugins {
    kotlin("jvm")
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jooq:jooq-codegen:3.19.8")
    runtimeOnly("com.mysql:mysql-connector-j:8.2.0")
}

kotlin {
    jvmToolchain(17)
}