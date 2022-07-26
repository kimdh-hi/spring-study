package com.study.springcoreadvanced.v0.app

import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV0 {

  fun save(id: String) {
    if (id == "ex")
      throw RuntimeException("error...")
    Thread.sleep(1000L)
  }
}