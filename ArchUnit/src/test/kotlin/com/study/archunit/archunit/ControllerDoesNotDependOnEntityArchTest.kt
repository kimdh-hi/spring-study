package com.study.archunit.archunit

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import jakarta.persistence.Entity
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.RestController

class ControllerDoesNotDependOnEntityArchTest {

  @Test
  fun `Controller는 Entity 를 직접 참조하지 않는다`() {
    val classes = ClassFileImporter()
      .withImportOption(ImportOption.DoNotIncludeTests())
      .importPackages("com.study.archunit")

    // 대상 클래스와 의존성 확인
    println("=== All Classes ===")
    classes.forEach { clazz ->
      if (clazz.isAnnotatedWith(RestController::class.java)) {
        println("RestController: ${clazz.name}")
        clazz.directDependenciesFromSelf.forEach { dep ->
          println("  -> depends on: ${dep.targetClass.name}")
          if (dep.targetClass.isAnnotatedWith(Entity::class.java)) {
            println("     ^^^ THIS IS AN ENTITY! ^^^")
          }
        }
      }
    }

    val rule = ArchRuleDefinition.noClasses().that()
      .resideInAPackage("..controller..")
      .and().resideInAPackage("com.study.archunit..")
      .or().areAnnotatedWith(RestController::class.java)
      .should().dependOnClassesThat()
      .areAnnotatedWith(Entity::class.java)
      .because("Controller는 Entity 클래스를 직접 참조하지 않는다.")

    rule.check(classes)
  }

}
