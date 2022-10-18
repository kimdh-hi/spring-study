package com.toy.springmvc.controller

import com.toy.springmvc.domain.Member
import com.toy.springmvc.domain.Person
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message-converter")
class MessageConverterController {

  /**
   * MessageConverter 는 보통 contentType 을 기준으로 결정된다.
   *
   * 기본 제공되는 컨버터 외 컨버터를 등록하고 싶다면 외부 의존성을 통해 추가하자.
   * WebMvcConfigurer 를 통해 컨버터를 등록할 수 있지만 기존 기본 컨버터의 설정이 무시된다.
   *
   * 컨버터 관련 의존성을 추가하는 경우 WebMvcConfigurationSupport 를 통해 컨버터로 등록된다.
   */

  @GetMapping("/message")
  fun message(@RequestBody message: String) = message

  @GetMapping("/json-message")
  fun jsonMessage(@RequestBody person: Person) = person

  @GetMapping("/xml-message")
  fun xmlMessage(@RequestBody person: Person) = person
}