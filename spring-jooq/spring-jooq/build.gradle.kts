val jooqVersion = "3.19.5"

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("nu.studer.jooq") version "9.0"
}

group = "com.toy"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq") {
        exclude("org.jooq", "jooq") //boot 3.3x jooq 최신버전 사용 (exclude 불필요)
    }
    implementation("org.jooq:jooq:$jooqVersion")
    jooqGenerator("com.mysql:mysql-connector-j")
    jooqGenerator("org.jooq:jooq")
    jooqGenerator("org.jooq:jooq-meta")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val dbUser = System.getProperty("db-user") ?: "root"
val dbPassword = System.getProperty("db-password") ?: "passwd"

jooq {
    configurations {
        create("sakilaDB") { // task > jooq.generate[NAME]Jooq
            generateSchemaSourceOnCompilation.set(false) // 기본적으로 스키마 소스 생성을 비활성화합니다
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3306/sakila"
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator" // 코틀린 제너레이터 명시
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "sakila"
                    }
                    generate.apply {
                        isDaos = true
                        isRecords = true
                        isFluentSetters = true
                        isJavaTimeTypes = true
                        isDeprecated = false
                    }
                    target.apply {
                        directory = "src/generated"
                    }
                }
            }
        }
    }
}

sourceSets {
    main {
        kotlin {
            srcDirs(listOf("src/main/kotlin", "src/generated"))
        }
    }
}