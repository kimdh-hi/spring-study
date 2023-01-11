package com.lecture.inflearnspringsecurityoauth2.controller

import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class IndexController(
  private val clientRegistrationRepository: ClientRegistrationRepository
) {

  @GetMapping("/")
  fun index(
    model: Model,
    authentication: Authentication?,
    @AuthenticationPrincipal oAuth2User: OAuth2User?
  ): String {

    (authentication as? OAuth2AuthenticationToken)?.let {
      oAuth2User?.attributes?.let { attributes ->
        var name = attributes["name"]

        if(it.authorizedClientRegistrationId == "naver") {
          val response = attributes["response"] as Map<*, *>
          name = response["name"].toString()
        }
        model.addAttribute("user", name)
      }
    }


    return "index"
  }
}