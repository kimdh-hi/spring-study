package com.toy.springstomp.ui.stomp

import java.security.Principal

data class ChatUser(
  val userId: String,
  val deviceId: String,
) : Principal {
  override fun getName(): String = userId
}
