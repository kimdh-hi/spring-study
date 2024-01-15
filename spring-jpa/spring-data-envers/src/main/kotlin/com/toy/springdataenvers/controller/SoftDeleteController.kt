package com.toy.springdataenvers.controller

import com.toy.springdataenvers.service.SoftDeleteService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test/soft-delete")
class SoftDeleteController(
  private val softDeleteService: SoftDeleteService
) {

  @PostMapping
  fun save(): String {
    val entity = softDeleteService.save()
    return entity.id
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String) {
    softDeleteService.delete(id)
  }
}