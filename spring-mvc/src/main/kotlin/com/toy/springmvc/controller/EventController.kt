package com.toy.springmvc.controller

import com.toy.springmvc.domain.Event
import com.toy.springmvc.service.EventService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import jakarta.validation.Valid

@Controller
class EventController(
  private val eventService: EventService
) {

  @InitBinder("event") // event model 에만 적용
  fun eventInitBinder(webDataBinder: WebDataBinder) {
    webDataBinder.setDisallowedFields("id") // 파라미터 필드에서 제외 (파라미터 블랙리스트 방식)
    webDataBinder.addValidators(EventCustomValidator())
  }

  @PostMapping("/events/binder")
  @ResponseBody
  fun initBinderTest(@ModelAttribute event: Event): Event {
    return event
  }

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
   * 단, jakarta.validation.constraints 의 검증 어노테이션을 사용하려면 @Valid 혹은 @Validated 를 사용해야 한다.
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

class EventCustomValidator: Validator {
  override fun supports(clazz: Class<*>): Boolean {
    return Event::class.java.isAssignableFrom(clazz)
  }

  override fun validate(target: Any, errors: Errors) {
    val event = target as Event
    if (event.name == "invalid-event")
      errors.rejectValue("name", "wrongValue", "invalid-event-name")
  }
}