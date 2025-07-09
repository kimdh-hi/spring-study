package com.study.openfeign.controller

import com.study.openfeign.service.FakeApiService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fake")
class FakeApiController(
  private val fakeApiService: FakeApiService,
) {

  @GetMapping
  fun getTodos() = fakeApiService.getTodo()

  @GetMapping("/default")
  fun getTodosByDefault() = fakeApiService.getTodoByDefault()

}
