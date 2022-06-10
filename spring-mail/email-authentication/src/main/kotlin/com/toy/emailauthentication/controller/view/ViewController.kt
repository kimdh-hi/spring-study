package com.toy.emailauthentication.controller.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ViewController {

  @GetMapping("/password-reset/{id}")
  fun passwordResetForm(@PathVariable id: String, model: Model): String {
    model.addAttribute("authId", id)

    return "passwordResetForm"
  }
}