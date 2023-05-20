package com.toy.springfcm.controller

import com.toy.springfcm.fcm.FcmSendRequest
import com.toy.springfcm.fcm.FcmSender
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FcmController(
  private val fcmSender: FcmSender
) {

  @PostMapping("/fcm/send")
  fun fcmSend(@RequestBody request: FcmSendRequest) = fcmSender.send(request)
}