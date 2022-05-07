package com.study.infleanrestapi.controller

import com.study.infleanrestapi.domain.Event
import com.study.infleanrestapi.repository.EventRepository
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/events")
@RestController
class EventController(private val eventRepository: EventRepository) {

    @PostMapping
    fun create(@RequestBody event: Event) : ResponseEntity<Event> {
        val savedEvent = eventRepository.save(event)
        val uri = linkTo(EventController::class.java).slash(savedEvent.id).toUri()

        return ResponseEntity.created(uri).body(savedEvent)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long) {

    }
}