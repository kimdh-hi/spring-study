package com.toy.springstomp.infra.redis

object RedisChannels {
  const val DUPLICATE_SESSION = "stomp:fanout:duplicate-session"
  const val ROOM_MESSAGE = "stomp:fanout:room-message"
}
