package com.toy.springwebsocket.chat

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
  private val chatService: ChatService
) {

  @PostMapping
  fun createRoom(@RequestParam name: String): ChatRoom {
    return chatService.createRoom(name)
  }

  @GetMapping
  fun findAllRoom() = chatService.findAllRoom()
}