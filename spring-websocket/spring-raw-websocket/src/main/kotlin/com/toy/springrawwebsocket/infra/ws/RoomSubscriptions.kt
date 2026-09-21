package com.toy.springrawwebsocket.infra.ws

import com.toy.springrawwebsocket.domain.chat.repository.RoomUserRepository
import com.toy.springrawwebsocket.infra.redis.RoomChannelRegistrar
import org.springframework.stereotype.Component

@Component
class RoomSubscriptions(
  private val localSessionRegistry: SessionRegistry,
  private val roomChannelRegistrar: RoomChannelRegistrar,
  private val roomUserRepository: RoomUserRepository,
) {
  fun attach(entry: SessionEntry) {
    roomUserRepository.findRoomIdsByUserId(entry.userId).forEach { subscribe(entry, it) }
  }

  fun detach(sessionId: String) {
    val entry = localSessionRegistry.remove(sessionId) ?: return
    entry.subscribedRooms().forEach { unsubscribe(entry, it) }
  }

  fun applyRoomUserChangeLocally(userId: String, roomId: String, joined: Boolean) {
    localSessionRegistry.findEntriesByUserId(userId).forEach { entry ->
      when {
        joined -> subscribe(entry, roomId)
        else -> unsubscribe(entry, roomId)
      }
    }
  }

  private fun subscribe(entry: SessionEntry, roomId: String) {
    if (localSessionRegistry.subscribe(entry, roomId)) roomChannelRegistrar.listen(roomId)
  }

  private fun unsubscribe(entry: SessionEntry, roomId: String) {
    if (localSessionRegistry.unsubscribe(entry, roomId)) roomChannelRegistrar.stopListening(roomId)
  }
}
