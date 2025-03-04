package com.toy.springtest.base

import com.ninjasquad.springmockk.MockkBean
import com.toy.springtest.TestComponent
import com.toy.springtest.service.Test2Service
import com.toy.springtest.service.TestService
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractTest {

  @MockkBean
  lateinit var testComponent: TestComponent

  @Autowired
  lateinit var testService: TestService

  @Autowired
  lateinit var test2Service: Test2Service
}
