package com.toy.springfcm.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Component

@Component
class FcmSender {

  fun send(request: FcmSendRequest) {
    val message = Message.builder()
      .putData("title", request.title)
      .putData("body", request.body)
      .setToken(request.token)
      .build()

    FirebaseMessaging.getInstance().send(message)
  }
}

data class FcmSendRequest(
  val title: String,
  val body: String,
  val token: String
)