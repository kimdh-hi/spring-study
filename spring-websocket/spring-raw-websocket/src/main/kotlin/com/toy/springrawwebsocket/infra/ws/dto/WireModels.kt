package com.toy.springrawwebsocket.infra.ws.dto

import com.toy.springrawwebsocket.infra.ws.constants.FrameTypes

data class InboundFrame(
  val type: String,
  val roomId: String,
  val text: String?,
)

data class MessageFrame(
  val id: Long,
  val roomId: String,
  val senderUserId: String,
  val senderDeviceId: String,
  val text: String,
  val at: Long,
  val type: String = FrameTypes.MESSAGE,
)
