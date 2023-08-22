package com.lecture.rsocket

import com.lecture.rsocket.service.SocketAcceptorImpl
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.TcpServerTransport

fun main() {
  val socketServer = RSocketServer.create(SocketAcceptorImpl())
  val closeableChannel = socketServer.bindNow(TcpServerTransport.create(6565))

  closeableChannel.onClose().block()
}