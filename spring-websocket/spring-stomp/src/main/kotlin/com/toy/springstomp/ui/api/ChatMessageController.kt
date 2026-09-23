package com.toy.springstomp.ui.api

import com.toy.springstomp.application.chat.ChatMessageQueryService
import com.toy.springstomp.infra.stomp.dto.MessageFrame
import com.toy.springstomp.ui.api.constants.X_USER_ID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class ChatMessageController(
  private val messageQueryService: ChatMessageQueryService,
) {
  @GetMapping("/api/rooms/{roomId}/messages")
  fun findRoomMessages(
    @PathVariable roomId: String,
    @RequestHeader(X_USER_ID) userId: String,
    @RequestParam(defaultValue = "0") afterId: Long,
    @RequestParam(defaultValue = "100") limit: Int,
  ): List<MessageFrame> =
    messageQueryService.findRoomMessages(roomId, userId, afterId, limit)?.map { MessageFrame.from(it) }
      ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
}
