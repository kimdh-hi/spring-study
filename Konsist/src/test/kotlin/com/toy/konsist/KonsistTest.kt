package com.toy.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withAllAnnotationsOf
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

class KonsistTest {

    @Test
    fun `Spring Boot application class name ends with Application`() {
        Konsist.scopeFromProduction()
            .classes()
            .withAllAnnotationsOf(SpringBootApplication::class)
            .assertTrue { it.hasNameEndingWith("Application") }
    }

    @Test
    fun `REST controllers have Controller suffix and reside in controller package`() {
        Konsist.scopeFromProduction()
            .classes()
            .withAllAnnotationsOf(RestController::class)
            .assertTrue { it.hasNameEndingWith("Controller") && it.resideInPackage("..controller") }
    }

    @Test
    fun `services have Service suffix and reside in service package`() {
        Konsist.scopeFromProduction()
            .classes()
            .withAllAnnotationsOf(Service::class)
            .assertTrue { it.hasNameEndingWith("Service") && it.resideInPackage("..service") }
    }

    @Test
    fun `controllers depend on services`() {
        Konsist.scopeFromProduction().assertArchitecture {
            val controller = Layer("Controller", "..controller..")
            val service = Layer("Service", "..service..")

            controller.dependsOn(service, strict = true)
        }
    }

    @Test
    fun `package names match directory paths`() {
        Konsist.scopeFromProject()
            .packages
            .assertTrue { it.hasMatchingPath }
    }

    @Test
    fun `wildcard imports are not allowed`() {
        Konsist.scopeFromProject()
            .imports
            .assertFalse { it.isWildcard }
    }

    @Test
    fun `java util logging is not allowed`() {
        Konsist.scopeFromProject()
            .files
            .assertFalse { it.hasImport { import -> import.name == "java.util.logging.." } }
    }
}
