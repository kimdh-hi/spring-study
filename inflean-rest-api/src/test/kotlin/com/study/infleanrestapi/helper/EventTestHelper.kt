package com.study.infleanrestapi.helper

import com.study.infleanrestapi.domain.Event
import com.study.infleanrestapi.domain.EventStatus
import com.study.infleanrestapi.vo.EventVO
import java.time.LocalDateTime
import kotlin.math.max

object EventTestHelper {

    const val NAME = "name"
    const val DESCRIPTION = "description"

    fun createEvent(): Event {
        return Event.newInstance(
            name = NAME, description = DESCRIPTION,
            beginEventDateTime = LocalDateTime.now(), closeEnrollmentDateTime = LocalDateTime.now().plusDays(1),
            beginEnrollmentDateTime = LocalDateTime.now(), endEventDateTime = LocalDateTime.now().plusMonths(1),
            location = "location", basePrice = 100, maxPrice = 1000, limitOfEnrollment = 100, eventStatus = EventStatus.BEGAN_ENROLLMENT,
            offline = false, free = false
        )
    }

    fun createEventVO(name: String = NAME, description: String = DESCRIPTION, basePrice: Int = 100, maxPrice: Int = 1000): EventVO {
        return EventVO(
            name = name, description = description,
            beginEventDateTime = LocalDateTime.now(), closeEnrollmentDateTime = LocalDateTime.now().plusDays(1),
            beginEnrollmentDateTime = LocalDateTime.now(), endEventDateTime = LocalDateTime.now().plusMonths(1),
            location = "location", basePrice = basePrice, maxPrice = maxPrice, limitOfEnrollment = 100)
    }
}