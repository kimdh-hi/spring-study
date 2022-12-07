package com.toy.springcacheex.controller

import com.toy.springcacheex.service.CacheTestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class CacheTestController(
  private val cacheTestService: CacheTestService
) {

  @GetMapping("/{spaceId}/{spaceChannelId}")
  fun test(@PathVariable spaceId: String, @PathVariable spaceChannelId: String): String {
    return cacheTestService.getSpaceParticipantCount(spaceId, spaceChannelId)
  }
}