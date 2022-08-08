package com.toy.proxyex.app.v3

import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV3 {

  fun save(itemId: String) {
    if(itemId == "ex")
      throw IllegalStateException("ex...")
    Thread.sleep(1000)
  }
}