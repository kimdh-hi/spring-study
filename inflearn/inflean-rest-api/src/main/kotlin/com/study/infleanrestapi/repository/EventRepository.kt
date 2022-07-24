package com.study.infleanrestapi.repository

import com.study.infleanrestapi.domain.Event
import org.springframework.data.repository.CrudRepository

interface EventRepository: CrudRepository<Event, Long> {
}