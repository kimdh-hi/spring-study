package com.toy.springstomp.ui.api

import com.toy.springstomp.application.chat.RoomUserService
import com.toy.springstomp.ui.api.constants.X_USER_ID
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RoomUserController(
  private val roomUserService: RoomUserService,
) {
  @PutMapping("/api/rooms/{roomId}/me")
  fun join(@PathVariable roomId: String, @RequestHeader(X_USER_ID) userId: String): Map<String, Boolean> =
    mapOf("joined" to roomUserService.join(roomId, userId))

  @DeleteMapping("/api/rooms/{roomId}/me")
  fun leave(@PathVariable roomId: String, @RequestHeader(X_USER_ID) userId: String): Map<String, Boolean> =
    mapOf("left" to roomUserService.leave(roomId, userId))

  @GetMapping("/api/me/rooms")
  fun findRooms(@RequestHeader(X_USER_ID) userId: String): List<String> = roomUserService.findRoomIds(userId)
}
