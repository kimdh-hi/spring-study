package com.toy.proxyex.app.v2

class OrderRepositoryV2 {

  fun save(itemId: String) {
    if(itemId == "ex")
      throw IllegalStateException("ex...")
    Thread.sleep(1000)
  }
}