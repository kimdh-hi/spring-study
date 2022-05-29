plugins {
    application
    id("org.jetbrains.kotlin.jvm") version "1.5.31" apply false
}

subprojects {

    group = "com.toy.gradle"
    version = "1.0"

    apply(plugin = "application")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    }
}

project(":web") {
    dependencies {
        // web 모듈 빌드시 생성된 war 파일에 services 모듈 jar 파일을 포함
        implementation(project(":services"))
    }
}