package com.toy.springmvc.controller

import org.slf4j.LoggerFactory
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.*
import kotlin.reflect.KClass

@RestController
@RequestMapping("/enum-validation")
class EnumValidationController {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("/form")
  fun form(
    @ModelAttribute @Valid enumValidationVO: EnumValidationVO,
    bindingResult: BindingResult
  ): EnumValidationVO {
    if(bindingResult.hasErrors()) {
      bindingResult.allErrors.forEach {
        log.error(it.defaultMessage)
      }
    }

    return enumValidationVO
  }

  @PostMapping("/json")
  fun json(
    @RequestBody @Valid enumValidationVO: EnumValidationVO,
    bindingResult: BindingResult
  ): EnumValidationVO {
    if(bindingResult.hasErrors()) {
      bindingResult.allErrors.forEach {
        log.error(it.defaultMessage)
      }
    }

    return enumValidationVO
  }
}


enum class EnumValidation {
  AAA, BBB
}
data class EnumValidationVO(
  @field:ValueOfEnum(enumClass = EnumValidation::class)
  val enum: String
)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValueOfEnumValidator::class])
annotation class ValueOfEnum(
  val enumClass: KClass<out Enum<*>>,
  val message: String = "must be any of enum {enumClass}",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, CharSequence> {
  private lateinit var acceptedValues: List<String>
  override fun initialize(annotation: ValueOfEnum) {
    acceptedValues = annotation.enumClass.java.enumConstants
      .map { it.name }
  }

  override fun isValid(value: CharSequence, context: ConstraintValidatorContext): Boolean {
    return acceptedValues.contains(value.toString())
  }
}