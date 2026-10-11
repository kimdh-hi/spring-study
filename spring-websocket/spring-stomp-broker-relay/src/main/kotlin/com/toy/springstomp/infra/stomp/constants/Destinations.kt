package com.toy.springstomp.infra.stomp.constants

private const val ROOM_TOPIC_PREFIX = "/topic/rooms."

object Destinations {
  const val ROOM_SEND = "/rooms/{roomId}"

  fun roomTopic(roomId: String): String = ROOM_TOPIC_PREFIX + roomId

  fun roomIdOf(destination: String): String? =
    destination.removePrefix(ROOM_TOPIC_PREFIX).takeIf { it != destination && it.isNotBlank() }
}
