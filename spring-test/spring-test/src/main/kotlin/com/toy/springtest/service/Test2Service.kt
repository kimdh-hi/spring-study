package com.toy.springtest.service

import com.toy.springtest.TestComponent
import org.springframework.stereotype.Service

@Service
class Test2Service(
  private val testComponent: TestComponent,
) {

  fun logic() = testComponent.logic()
}

