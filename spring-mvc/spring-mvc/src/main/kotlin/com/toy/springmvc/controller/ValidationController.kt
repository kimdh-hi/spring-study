package com.toy.springmvc.controller

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.*
import javax.validation.constraints.NotBlank
import kotlin.reflect.KClass

@RestController
@RequestMapping("/api/validation")
@Validated
class ValidationController {
  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping
  fun test(@RequestBody @Valid requestVO: ValidationDataVO, result: BindingResult): ValidationDataVO {
    if(result.hasErrors()) {
      println(result.allErrors.forEach { println(it) })
    }
    return requestVO
  }
}

data class ValidationDataVO(
  @NameListConstraint
  val names: List<String>
)

@Constraint(validatedBy = [NotBlankValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class NameListConstraint (
  val message: String = "name is cannot be blank.",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)

class NotBlankValidator: ConstraintValidator<NameListConstraint, List<String>> {
  override fun isValid(values: List<String>, context: ConstraintValidatorContext): Boolean {
    values.forEach {
      if(StringUtils.isNotBlank(it))
        return false
    }
    return true
  }
}