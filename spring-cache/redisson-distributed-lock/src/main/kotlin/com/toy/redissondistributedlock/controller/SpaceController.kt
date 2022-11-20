package com.toy.redissondistributedlock.controller

import com.toy.redissondistributedlock.service.SpaceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/spaces")
class SpaceController(
  private val spaceService: SpaceService
) {

  @PostMapping("/{spaceId}/participate")
  fun participate(@PathVariable spaceId: String): ResponseEntity<Unit> {
    spaceService.participateWithLock(spaceId)
    return ResponseEntity.ok().build()
  }
}