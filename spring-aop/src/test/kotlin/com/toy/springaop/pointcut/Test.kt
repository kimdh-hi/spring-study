package com.toy.springaop.pointcut

import com.toy.springaop.service.MemberServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import java.lang.reflect.Method
import kotlin.reflect.full.functions

class Test {

  private val log = LoggerFactory.getLogger(javaClass)

  val pointcut = AspectJExpressionPointcut()
  lateinit var testMethod: Method

  @BeforeEach
  fun init() {
    testMethod = MemberServiceImpl::class.java.getMethod("test", String::class.java)
  }

  @Test
  fun exactMatch() {
    pointcut.expression = "execution(public String com.toy.springaop.service.MemberServiceImpl.test(String))"
    Assertions.assertThat(pointcut.matches(testMethod, MemberServiceImpl::class.java)).isTrue()
  }
}