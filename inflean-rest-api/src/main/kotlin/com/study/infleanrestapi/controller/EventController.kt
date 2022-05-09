package com.study.infleanrestapi.controller

import com.study.infleanrestapi.config.EventValidator
import com.study.infleanrestapi.domain.Event
import com.study.infleanrestapi.repository.EventRepository
import com.study.infleanrestapi.vo.EventVO
import org.modelmapper.ModelMapper
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult

import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/events")
@RestController
class EventController(
    private val eventRepository: EventRepository,
    private val modelMapper: ModelMapper, private val eventValidator: EventValidator) {

    @PostMapping
    fun create(@RequestBody @Valid eventVO: EventVO, bindingResult: BindingResult) : ResponseEntity<Event> {
        eventValidator.validate(eventVO, bindingResult)
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }
        val event = modelMapper.map(eventVO, Event::class.java)
        val savedEvent = eventRepository.save(event)
        val uri = linkTo(EventController::class.java).slash(savedEvent.id).toUri()

        return ResponseEntity.created(uri).body(savedEvent)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long) {

    }
}