package com.toy.springcacheex.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/session-clustering")
class SessionClusteringTestController {

  private val log = LoggerFactory.getLogger(javaClass)

  val sessionMap = mutableMapOf<String, String>()

  @GetMapping("/login")
  fun login(httpSession: HttpSession, @RequestParam username: String): String {
    log.info("httpSession.id: {}", httpSession.id)
//    sessionMap[httpSession.id] = username

    // redis 를 통한 세션 클러스터링
    // spring-session-data-redis 의존성
    // spring.session.storage-type=redis 설정
    httpSession.setAttribute("name", username)
    return "ok"
  }

  @GetMapping("/me")
  fun me(httpSession: HttpSession): String? {
    log.info("httpSession.id: {}", httpSession.id)
//    log.info("me session: {}", httpSession.id)
    return httpSession.getAttribute("name") as String
  }
}