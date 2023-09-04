package com.toy.springmvc.controller

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import jakarta.validation.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import kotlin.reflect.KClass

@RestController
@RequestMapping("/api/validation")
class ValidationController {
  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping
  fun test(@RequestBody @Valid requestVO: ValidationDataVO, result: BindingResult): ValidationDataVO {
    if(result.hasErrors()) {
      println(result.allErrors.forEach { println(it) })
      throw ParameterException()
    }
    return requestVO
  }
  
  @PostMapping("/test")
  fun test2(@RequestBody @Valid requestVO: ValidationData2VO, result: BindingResult): ValidationData2VO {
    if(result.hasErrors()) {
      println(result.allErrors.forEach { println(it) })
      throw ParameterException()
    }
    return requestVO
  }

  @PostMapping("/json-enum")
  fun test3(@RequestBody @Valid requestVO: ValidationData3VO, result: BindingResult): ValidationData3VO {
    if(result.hasErrors()) {
      println(result.allErrors.forEach { println(it) })
      throw ParameterException()
    }
    return requestVO
  }

  @PostMapping("/form-enum")
  fun test4(@ModelAttribute @Valid requestVO: ValidationData3VO, result: BindingResult): ValidationData3VO {
    if(result.hasErrors()) {
      println(result.allErrors.forEach { println(it) })
      throw ParameterException()
    }
    return requestVO
  }
}

data class ValidationDataVO(
  @NameListConstraint
  val names: List<String>,

  @field:NotBlank
  val nickname: String,

  @field:Email
  @field:NotBlank
  val email: String,
)

data class ValidationData2VO(
  @field:Email
  @field:NotBlank
  val email: String
)

data class ValidationData3VO(
  val validationTestEnum: ValidationTestEnum
)

enum class ValidationTestEnum {
  AAA, BBB
}

/**
 * form -> converter 동작
 * json -> converter 동작안함
 */
@Component
class ValidationTestEnumConverter: Converter<String, ValidationTestEnum> {

  override fun convert(source: String): ValidationTestEnum? {
    return try {
      ValidationTestEnum.valueOf(source)
    } catch (e: Exception) {
      throw e
    }
  }
}

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
      if(StringUtils.isBlank(it))
        return false
    }
    return true
  }
}