package com.toy.springrawwebsocket.infra.redis

import com.toy.springrawwebsocket.application.chat.RoomUserChangedEvent
import com.toy.springrawwebsocket.infra.redis.dto.RoomUserChangedMessage
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class RoomUserChangedRedisBridge(
  private val publisher: RedisPublisher,
) {
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  fun publish(event: RoomUserChangedEvent) {
    publisher.publish(RedisChannels.ROOM_USER, RoomUserChangedMessage(event.userId, event.roomId, event.joined))
  }
}
