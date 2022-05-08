package com.study.infleanrestapi.helper

import com.study.infleanrestapi.domain.Event
import com.study.infleanrestapi.domain.EventStatus
import com.study.infleanrestapi.vo.EventVO
import java.time.LocalDateTime

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

    fun createEventVO(): EventVO {
        return EventVO(
            name = NAME, description = DESCRIPTION,
            beginEventDateTime = LocalDateTime.now(), closeEnrollmentDateTime = LocalDateTime.now().plusDays(1),
            beginEnrollmentDateTime = LocalDateTime.now(), endEventDateTime = LocalDateTime.now().plusMonths(1),
            location = "location", basePrice = 100, maxPrice = 1000, limitOfEnrollment = 100)
    }
}