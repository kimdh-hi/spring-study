package com.toy.springmvc.common

import org.springframework.web.multipart.MultipartFile
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [MultipartFileValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class MultipartFileConstraint (
    val message: String = "file cannot be null.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class MultipartFileValidator: ConstraintValidator<MultipartFileConstraint, MultipartFile> {
    override fun isValid(multipartFile: MultipartFile?, context: ConstraintValidatorContext?): Boolean {
        return multipartFile != null
    }
}
