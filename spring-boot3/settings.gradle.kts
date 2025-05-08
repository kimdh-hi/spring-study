rootProject.name = "spring-boot3"

pluginManagement {
  val springBootVersion: String by settings
  val dependencyManagementVersion: String by settings
  val kotlinVersion: String by settings

  plugins {
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version dependencyManagementVersion
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
  }
}
