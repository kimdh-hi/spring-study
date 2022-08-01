package com.toy.springcore.`01-bean-lifecycle`

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean

class NetworkClient2 {

  private val log = LoggerFactory.getLogger(javaClass)
  var url: String? = null

  init {
    log.info("constructor called. url={}", url)
    connect()
    log.info("connection message ...")
  }

  fun connect() = log.info("connect to {} ...", url)
  fun call(message: String) = log.info("url: {}, message: {}", url, message)
  fun disconnect() = log.info("disconnect to {} ...", url)

  fun init() {
    log.info("initMode.init...")
    connect()
    call("connection message ...")
  }

  fun close() {
    log.info("destroyMode.close...")
    disconnect()
  }
}