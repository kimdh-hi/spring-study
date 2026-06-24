plugins {
    `java-library`
    `maven-publish`
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

sourceSets {
    main {
        resources.srcDir("src/main/proto")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

fun Project.isCommandAvailable(command: String): Boolean {
    val result = providers.exec {
        commandLine("sh", "-c", "command -v $command >/dev/null 2>&1")
        isIgnoreExitValue = true
    }.result.get()
    return result.exitValue == 0
}

tasks.register<Exec>("bufLint") {
    group = "verification"
    description = "Lint protobuf contracts with Buf STANDARD rules."
    workingDir = projectDir
    commandLine("buf", "lint")
    doFirst {
        if (!isCommandAvailable("buf")) {
            throw GradleException("Buf CLI is required for bufLint. Install it from https://buf.build/docs/installation/")
        }
    }
}

val bufBreakingAgainst = providers.gradleProperty("bufBreakingAgainst")

tasks.register("bufBreaking") {
    group = "verification"
    description = "Detect protobuf breaking changes with Buf. Provide -PbufBreakingAgainst=<source>."
    onlyIf("bufBreakingAgainst Gradle property is provided") {
        bufBreakingAgainst.isPresent
    }
    doLast {
        if (!isCommandAvailable("buf")) {
            throw GradleException("Buf CLI is required for bufBreaking. Install it from https://buf.build/docs/installation/")
        }
        providers.exec {
            workingDir = projectDir
            commandLine("buf", "breaking", "--against", bufBreakingAgainst.get())
        }.result.get()
    }
}

tasks.register("protoCheck") {
    group = "verification"
    description = "Run protobuf contract validation."
    dependsOn("bufLint", "bufBreaking")
}
