package com.toy.springaop.proxy

import com.toy.springaop.service.MemberService
import com.toy.springaop.service.MemberServiceImpl
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

//@SpringBootTest(properties = ["spring.aop.proxy-target-class=false"]) // jdk 동적 프록시 적용-구체클래스 di 시 예외
@SpringBootTest(properties = ["spring.aop.proxy-target-class=true"]) // cglib 프록시 적용
@Import(ProxyDIAspect::class)
class ProxyDIAspectTest @Autowired constructor(
  private val memberService: MemberService,
  private val memberServiceImpl: MemberServiceImpl
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun test() {
    log.info("memberService class: {}", memberService::class)
    log.info("memberServiceImpl class: {}", memberServiceImpl::class)
  }
}