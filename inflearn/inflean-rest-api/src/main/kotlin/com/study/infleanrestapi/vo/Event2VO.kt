package com.study.infleanrestapi.vo

import java.io.Serializable
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class Event2VO (
    @NotEmpty
    val name: String? = null,
    @NotEmpty
    val description: String? = null,
    @NotNull
    val beginEnrollmentDateTime: LocalDateTime? = null,
    @NotNull
    val closeEnrollmentDateTime: LocalDateTime? = null,
    @NotNull
    val beginEventDateTime: LocalDateTime? = null,
    @NotNull
    val endEventDateTime: LocalDateTime? = null,
    val location: String? = null,
    @Min(0)
    val basePrice: Int? = null,
    @Min(0)
    val maxPrice: Int? = null,
    @Min(0)
    val limitOfEnrollment: Int? = null,
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 4013865171043834201L
    }
}