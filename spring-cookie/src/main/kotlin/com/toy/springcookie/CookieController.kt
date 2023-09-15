package com.toy.springcookie

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cookie")
class CookieController {

  @GetMapping
  fun sendCookie(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
    if (request.cookies != null) {
      request.cookies.forEach {
        println(it.value)
      }
    }

    val cookie = Cookie("cookieTestName1", "cookieTestValue1")
    cookie.domain = "example.com"
    response.addCookie(cookie)

    return ResponseEntity.ok("ok")
  }
}