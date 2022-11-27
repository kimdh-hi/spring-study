package com.toy.springintegrationdemo.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class TestService {

  fun logic() {
    val random = Random().nextInt(1, 5)
    if(random != 10)
      throw RuntimeException("error...")
  }
}