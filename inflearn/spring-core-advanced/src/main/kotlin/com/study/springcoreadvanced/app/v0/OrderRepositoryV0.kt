package com.study.springcoreadvanced.app.v0

import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryV0 {

  fun save(id: String) {
    if (id == "ex")
      throw RuntimeException("error...")
    Thread.sleep(1000L)
  }
}