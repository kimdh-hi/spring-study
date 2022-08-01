package com.toy.springcore.`01-bean-lifecycle`

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean

class NetworkClient1: InitializingBean, DisposableBean {

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

  override fun afterPropertiesSet() {
    log.info("afterPropertiesSet...")
    connect()
    call("connection message ...")
  }

  override fun destroy() {
    log.info("destory...")
    disconnect()
  }
}