package com.toy.springcoreadvancedaop.proxy.`pure-proxy`

import org.slf4j.LoggerFactory

/*
프록시 패턴을 이용해서 필드를 통한 캐시 적용
 */
class CacheProxy(
  private val subject: Subject
): Subject {
  private val log = LoggerFactory.getLogger(javaClass)
  var cacheValue: String? = null

  override fun operation(): String {
    log.info("Proxy object called...")
    return cacheValue ?: run {
      this.cacheValue = subject.operation()
      this.cacheValue!!
    }
  }
}

fun main() {
  val cacheProxy = CacheProxy(RealSubject())
  val client = ProxyClient(cacheProxy)
  client.execute()
  client.execute()
  client.execute()
}