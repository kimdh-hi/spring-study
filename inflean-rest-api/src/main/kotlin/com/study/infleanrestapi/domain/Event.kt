package com.study.infleanrestapi.domain

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Event (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var description: String,
    var beginEnrollmentDateTime: LocalDateTime,
    var closeEnrolmentDateTime: LocalDateTime,
    var beginEventDateTime: LocalDateTime,
    var endEventDateTime: LocalDateTime,
    var location: String,
    var basePrice: Int,
    var maxPrice: Int,
    var limitOfEnrollment: Int,

)