package com.toy.jpacoroutine.controller

import com.toy.jpacoroutine.facade.EntityFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entities")
class EntityController(
  private val entityFacade: EntityFacade
) {

  @GetMapping("/coroutine/{keyword}")
  suspend fun findAllCoroutine(@PathVariable keyword: String) = entityFacade.findAllCoroutine(keyword)

  @GetMapping("/{keyword}")
  suspend fun findAll(@PathVariable keyword: String) = entityFacade.findAll(keyword)
}