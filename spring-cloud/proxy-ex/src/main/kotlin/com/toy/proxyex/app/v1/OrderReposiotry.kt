package com.toy.proxyex.app.v1

interface OrderReposiotryV1 {
  fun save(itemId: String)
}

class OrderRepositoryV1Impl: OrderReposiotryV1 {

  override fun save(itemId: String) {
    if(itemId == "ex")
      throw IllegalStateException("ex...")
    Thread.sleep(1000)
  }
}