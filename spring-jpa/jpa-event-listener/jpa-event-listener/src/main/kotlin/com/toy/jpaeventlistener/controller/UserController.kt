package com.toy.jpaeventlistener.controller

import com.toy.jpaeventlistener.domain.User
import com.toy.jpaeventlistener.service.UserSaveRequestVO
import com.toy.jpaeventlistener.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
  private val userService: UserService
) {

  @PostMapping
  fun save(@RequestBody requestVO: UserSaveRequestVO): User {
    return userService.save(requestVO)
  }

  @PostMapping("/save-event")
  fun saveByEvent(@RequestBody requestVO: UserSaveRequestVO) {
    return userService.saveEventSend(requestVO)
  }
}