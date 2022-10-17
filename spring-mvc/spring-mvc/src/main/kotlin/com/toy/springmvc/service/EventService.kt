package com.toy.springmvc.service

import com.toy.springmvc.domain.Event
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventService {

  fun getEvents(): List<Event> {
    val event1  = Event(
      name = "spring mvc1",
      limitOfEnrollment = 5,
      startDate = LocalDateTime.of(2022, 10, 1, 1, 1),
      endDate = LocalDateTime.of(2022, 11, 1, 1, 1),
    )

    val event2  = Event(
      name = "spring mvc2",
      limitOfEnrollment = 5,
      startDate = LocalDateTime.of(2022, 10, 1, 1, 1),
      endDate = LocalDateTime.of(2022, 11, 1, 1, 1),
    )
    return listOf(event1, event2)
  }
}