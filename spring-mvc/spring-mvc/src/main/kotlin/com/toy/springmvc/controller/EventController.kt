package com.toy.springmvc.controller

import com.toy.springmvc.domain.Event
import com.toy.springmvc.service.EventService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import javax.validation.Valid

@Controller
class EventController(
  private val eventService: EventService
) {

  @GetMapping("/events/form")
  fun form(model: Model): String {
    model.addAttribute("event", Event())
    return "events/form"
  }

  @PostMapping("/events")
  @ResponseBody
  fun save(
    @RequestParam name: String,
    @RequestParam limitOfEnrollment: Int
  ): Event {
    return Event(name = name, limitOfEnrollment = limitOfEnrollment)
  }

  /**
   * @Valid 를 쓰지 않아도 bindingResult 에 reject 된 결과가 바인딩된다.
   * 단, javax.validation.constraints 의 검증 어노테이션을 사용하려면 @Valid 혹은 @Validated 를 사용해야 한다.
   *
   * @Validated validation-group 사용가능
   */
  @PostMapping("/events/model-attributes")
  @ResponseBody
  fun saveByModelAttributes(
    @ModelAttribute @Validated(Event.NameValidate::class, Event.LimitValidate::class) event: Event,
    bindingResult: BindingResult
  ): Event {
    if(bindingResult.hasErrors()) {
      bindingResult.allErrors.forEach {
        println("fieldErrors: $it")
      }
    }
    return event
  }

  @GetMapping("/events")
  fun events(model: Model): String {
    model.addAttribute("events", eventService.getEvents())
    return "events/list"
  }
}