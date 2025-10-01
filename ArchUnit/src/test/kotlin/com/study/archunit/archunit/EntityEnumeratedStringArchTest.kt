package com.study.archunit.archunit

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

class EntityEnumeratedStringArchTest {

  @Test
  fun `jpa entity는 Enum 필드에 @Enumerated(EnumType_STRING)를 가진다`() {
    val classFileImporter = ClassFileImporter()
      .withImportOption(ImportOption.DoNotIncludeTests())
      .withImportOption { it.contains("/domain/") }
//      .withImportOption { !it.contains("/classes/java/") } // QClass 제외
//      .withImportOption { !it.contains("\$Companion") } // Companion object 제외
      .importPackages("com.study.archunit")

    val rule = ArchRuleDefinition.classes().that()
      .areAnnotatedWith(Entity::class.java)
      .should(haveEnumFieldsWithEnumeratedString())

    rule.check(classFileImporter)
  }

  private fun haveEnumFieldsWithEnumeratedString(): ArchCondition<JavaClass> {
    return object : ArchCondition<JavaClass>("have enum fields with @Enumerated(EnumType.STRING)") {
      override fun check(javaClass: JavaClass, events: ConditionEvents) {
        javaClass.allFields
          .filter { it.rawType.isEnum && it.isAnnotatedWith(Column::class.java) }
          .forEach {
            val enumeratedAnnotation = runCatching { it.getAnnotationOfType(Enumerated::class.java) }.getOrNull()
            if (enumeratedAnnotation == null || enumeratedAnnotation.value != EnumType.STRING) {
              val message = "[${javaClass.simpleName}.${it.name}] does not have @Enumerated(EnumType.STRING) annotation"
              events.add(SimpleConditionEvent.violated(it, message))
            }
          }
      }
    }
  }
}
