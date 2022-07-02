plugins {
    kotlin("jvm") version "1.6.21"
}

group = "com.toy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
9791189909147
dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")

    implementation(kotlin("stdlib"))
}