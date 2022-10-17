package com.toy.springmvc.controller

import com.toy.springmvc.service.EventService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class EventController(
  private val eventService: EventService
) {

  @GetMapping("/events")
  fun events(model: Model): String {
    model.addAttribute("events", eventService.getEvents())
    return "events/list"
  }

}