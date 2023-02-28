package com.toy.jpacoroutine.controller

import com.toy.jpacoroutine.facade.EntityFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entities")
class EntityController(
  private val entityFacade: EntityFacade
) {

  @GetMapping("/coroutine")
  suspend fun findAllCoroutine() = entityFacade.findAllCoroutine()

  @GetMapping
  suspend fun findAll() = entityFacade.findAll()
}