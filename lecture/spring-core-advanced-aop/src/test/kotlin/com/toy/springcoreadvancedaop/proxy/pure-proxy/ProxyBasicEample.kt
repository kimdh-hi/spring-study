package com.toy.springcoreadvancedaop.proxy.`pure-proxy`

import org.slf4j.LoggerFactory

interface Subject {
  fun operation(): String
}

class RealSubject: Subject {
  private val log = LoggerFactory.getLogger(javaClass)
  override fun operation(): String {
    log.info("RealSubject called...")
    Thread.sleep(500L)
    return "ok"
  }
}

class ProxyClient(
  private val subject: Subject
) {
  fun execute() {
    subject.operation()
  }
}

fun main() {
  val client = ProxyClient(RealSubject())
  client.execute()
  client.execute()
  client.execute()
}