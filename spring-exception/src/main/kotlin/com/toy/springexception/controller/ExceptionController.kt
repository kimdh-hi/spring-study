package com.toy.springexception.controller

import com.toy.springexception.exception.ErrorCodes
import com.toy.springexception.exception.KnownException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exceptions")
class ExceptionController {

  @GetMapping("/unknown")
  fun getUnknownException() {
    throw IllegalArgumentException("unknown exception...")
  }

  @GetMapping("/known")
  fun getKnownException() {
    throw KnownException(ErrorCodes.NOT_FOUND, "data not found.")
  }
}
