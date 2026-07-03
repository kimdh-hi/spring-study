package com.study.springmodulith

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class ModularityTests {

  private val modules = ApplicationModules.of(SpringModulithApplication::class.java)

  @Test
  fun verifiesModularStructure() {
    modules.verify()
  }

  @Test
  fun writesDocumentation() {
    Documenter(modules).writeDocumentation()
  }
}
