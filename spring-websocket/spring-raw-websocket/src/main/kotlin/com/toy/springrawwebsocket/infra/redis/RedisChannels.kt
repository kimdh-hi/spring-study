package com.toy.springrawwebsocket.infra.redis

private const val ROOM_CHANNEL_PREFIX = "ws:fanout:room:"

object RedisChannels {
  const val DUPLICATE_SESSION = "ws:fanout:duplicate-session"
  const val ROOM_USER = "ws:fanout:room-user"

  fun room(roomId: String): String = ROOM_CHANNEL_PREFIX + roomId

  fun roomIdOf(channel: String): String? = channel.removePrefix(ROOM_CHANNEL_PREFIX).takeIf { it != channel }
}
