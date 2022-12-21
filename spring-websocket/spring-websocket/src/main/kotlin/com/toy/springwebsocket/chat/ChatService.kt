package com.toy.springwebsocket.chat

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.UUID

@Service
class ChatService(
  private val objectMapper: ObjectMapper,
) {

  val chatRooms = hashMapOf<String, ChatRoom>()

  fun findAllRoom(): List<ChatRoomResponseVO> {
    return chatRooms.values
      .map {
        ChatRoomResponseVO(it.roomId, it.name, it.sessions.map { session -> session.id }.toList())
      } .toList()
  }

  fun findRoomById(id: String) = chatRooms[id]

  fun createRoom(name: String): ChatRoom {
    val roomId = UUID.randomUUID().toString()
    val chatRoom = ChatRoom(roomId = roomId, name)
    chatRooms[roomId] = chatRoom
    return chatRoom
  }

  fun <T> sendMessage(session: WebSocketSession, message: T) {
    session.sendMessage(TextMessage(objectMapper.writeValueAsString(message)))
  }
}