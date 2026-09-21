package com.toy.springrawwebsocket.ui.api

import com.toy.springrawwebsocket.application.chat.ChatMessageQueryService
import com.toy.springrawwebsocket.ui.api.constants.X_USER_ID
import com.toy.springrawwebsocket.ui.api.dto.ChatMessageResponse
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
  ): List<ChatMessageResponse> =
    messageQueryService.findRoomMessages(roomId, userId, afterId, limit)?.map { ChatMessageResponse.from(it) }
      ?: throw ResponseStatusException(HttpStatus.FORBIDDEN)
}
