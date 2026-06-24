plugins {
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.spring") version "2.4.0"
    id("com.google.protobuf") version "0.9.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.grpc:spring-grpc-dependencies:1.0.3")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-grpc-server")
    implementation("org.springframework.boot:spring-boot-starter-grpc-client")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.grpc:grpc-kotlin-stub:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    add("protobuf", "com.example:proto-contracts:0.0.1-SNAPSHOT")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${dependencyManagement.importedProperties["protobuf-java.version"]}"
    }
    plugins {
        val grpc = findByName("grpc") ?: create("grpc")
        grpc.artifact = "io.grpc:protoc-gen-grpc-java:${dependencyManagement.importedProperties["grpc.version"]}"
        val grpckt = findByName("grpckt") ?: create("grpckt")
        grpckt.artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                if (findByName("grpc") == null) create("grpc")
                if (findByName("grpckt") == null) create("grpckt")
            }
        }
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("build/generated/sources/proto/main/java")
            kotlin.srcDir("build/generated/sources/proto/main/grpc")
            kotlin.srcDir("build/generated/sources/proto/main/grpckt")
        }
    }
}
