package com.study.infleanrestapi.vo

import java.io.Serializable
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class EventVO (
    @field:NotBlank
    val name: String? = null,
    @field:NotEmpty
    val description: String? = null,
    @field:NotNull
    val beginEnrollmentDateTime: LocalDateTime? = null,
    @field:NotNull
    val closeEnrollmentDateTime: LocalDateTime? = null,
    @field:NotNull
    val beginEventDateTime: LocalDateTime? = null,
    @field:NotNull
    val endEventDateTime: LocalDateTime? = null,
    val location: String? = null,
    @field:Min(0)
    val basePrice: Int? = null,
    @field:Min(0)
    val maxPrice: Int? = null,
    @field:Min(0)
    val limitOfEnrollment: Int? = null,
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 4013865171043834201L
    }
}