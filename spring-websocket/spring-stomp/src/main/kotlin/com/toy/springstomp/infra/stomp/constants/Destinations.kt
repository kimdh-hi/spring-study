package com.toy.springstomp.infra.stomp.constants

private const val ROOMS_SEGMENT = "/rooms/"
private const val ROOM_TOPIC_PREFIX = "/topic$ROOMS_SEGMENT"

object Destinations {
  const val ROOM_SEND_MAPPING = "$ROOMS_SEGMENT{roomId}"

  fun roomTopic(roomId: String): String = ROOM_TOPIC_PREFIX + roomId

  fun roomIdOf(destination: String): String? =
    destination.removePrefix(ROOM_TOPIC_PREFIX).takeIf { it != destination && it.isNotBlank() }
}
