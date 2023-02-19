package com.toy.jpasecondlevelcache.controller

import com.toy.jpasecondlevelcache.service.SomeEntityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/some-entity")
class SomeEntityController(
  private val someEntityService: SomeEntityService
) {

  @GetMapping("/{id}")
  fun find(@PathVariable id: Long) = someEntityService.find(id)

}