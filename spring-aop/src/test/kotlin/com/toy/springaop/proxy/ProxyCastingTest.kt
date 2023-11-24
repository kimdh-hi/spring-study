package com.toy.springaop.proxy

import com.toy.springaop.service.MemberService
import com.toy.springaop.service.MemberServiceImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.aop.framework.ProxyFactory

class ProxyCastingTest {

  @Test
  fun jdkProxy() {
    val target = MemberServiceImpl()
    val proxyFactory = ProxyFactory(target)

    // jdk 동적 프록시 (default=false)
    // interface 기반 proxy 생성
    proxyFactory.isProxyTargetClass = false

    val memberServiceProxy = proxyFactory.proxy as MemberService

    // jdk 동적 프록시에 의해 생성된 proxy 는 구체 타입으로 케스팅 불가능
    assertThrows<ClassCastException> { memberServiceProxy as MemberServiceImpl }
  }

  @Test
  fun cglibProxy() {
    val target = MemberServiceImpl()
    val proxyFactory = ProxyFactory(target)

    // cglib 프록시
    // 구체 클래스 기반 proxy 생성
    proxyFactory.isProxyTargetClass = true

    val memberServiceProxy = proxyFactory.proxy as MemberService
    
    assertDoesNotThrow { memberServiceProxy as MemberServiceImpl }
  }
}