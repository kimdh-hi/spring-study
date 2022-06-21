package com.toy.springthymeleaf.controller

import com.toy.springthymeleaf.common.MessageSourceResolver
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TestViewController(
  private val messageSourceResolver: MessageSourceResolver
) {

  @GetMapping
  fun home(model: Model): String {
    val message = messageSourceResolver.getMessage("key1", "daehyun Kim")
    model.addAttribute("message", message)

    return "home"
  }
}