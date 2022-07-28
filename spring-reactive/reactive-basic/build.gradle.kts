plugins {
    kotlin("jvm") version "1.7.10"
}

group = "com.toy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.projectreactor:reactor-core:3.4.21")
    implementation("com.github.javafaker:javafaker:1.0.2")

    testImplementation("io.projectreactor:reactor-test:3.4.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

}