package com.study.infleanrestapi.helper

import com.study.infleanrestapi.domain.Event
import com.study.infleanrestapi.domain.EventStatus
import java.time.LocalDateTime

object EventTestHelper {

    fun createEvent(): Event {
        return Event.newInstance(
            name = "name", description = "description",
            beginEventDateTime = LocalDateTime.now(), closeEnrollmentDateTime = LocalDateTime.now().plusDays(1),
            beginEnrollmentDateTime = LocalDateTime.now(), endEventDateTime = LocalDateTime.now().plusMonths(1),
            location = "location", basePrice = 100, maxPrice = 1000, limitOfEnrollment = 100, eventStatus = EventStatus.BEGAN_ENROLLMENT
        )
    }
}