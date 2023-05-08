package com.toy.jpaemoji.controller

import com.toy.jpaemoji.dto.EmojiDto
import com.toy.jpaemoji.service.EmojiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/emoji")
class EmojiController(
  private val emojiService: EmojiService
) {

  @PostMapping
  fun save(@RequestBody dto: EmojiDto): ResponseEntity<EmojiDto> {
    val responseDto = emojiService.save(dto)
    return ResponseEntity.ok(responseDto)
  }

  @GetMapping
  fun getAll(): ResponseEntity<List<EmojiDto>> {
    val responseDto = emojiService.getAll()
    return ResponseEntity.ok(responseDto)
  }
}