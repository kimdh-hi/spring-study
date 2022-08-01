package com.toy.springcore.`01-bean-lifecycle`

import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

class NetworkClient3 {

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

  @PostConstruct
  fun init() {
    log.info("@PostConstruct...")
    connect()
    call("connection message ...")
  }

  @PreDestroy
  fun close() {
    log.info("@PreDestroy...")
    disconnect()
  }
}