package com.study.searchableencryption.message.ui

import com.study.searchableencryption.message.application.ChatMessageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rooms/{roomId}/messages")
class ChatMessageController(
  private val chatMessageService: ChatMessageService,
) {

  @PostMapping
  fun send(@PathVariable roomId: Long, @RequestBody request: SendMessageRequest): ChatMessageResponse =
    ChatMessageResponse.from(chatMessageService.send(roomId, request.senderId, request.content))

  @GetMapping("/search")
  fun search(@PathVariable roomId: Long, @RequestParam q: String): List<ChatMessageResponse> =
    chatMessageService.search(roomId, q).map(ChatMessageResponse::from)

  @GetMapping("/search/full-scan")
  fun searchByFullScan(@PathVariable roomId: Long, @RequestParam q: String): List<ChatMessageResponse> =
    chatMessageService.searchByFullScan(roomId, q).map(ChatMessageResponse::from)

  @GetMapping("/search/like")
  fun searchByLikeOnCipher(@PathVariable roomId: Long, @RequestParam q: String): List<ChatMessageResponse> =
    chatMessageService.searchByLikeOnCipher(roomId, q).map(ChatMessageResponse::from)
}
