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

  @Test
  fun anyMatch() {
    pointcut.expression = "execution(* *(..))"
    Assertions.assertThat(pointcut.matches(testMethod, MemberServiceImpl::class.java)).isTrue()
  }

  // 패턴매칭시
  // * 해당 패키지만 매칭
  // ** 해당 패키지 이하 모든 패키지
  @Test
  fun `packagePatternMatch`() {
    pointcut.expression = "execution(* com.toy.springaop.service..*.*(..))"
    Assertions.assertThat(pointcut.matches(testMethod, MemberServiceImpl::class.java)).isTrue()
  }

  @Test
  fun `subPackagePatternMatch`() {
    pointcut.expression = "execution(* com.toy.springaop..*.*(..))"
    Assertions.assertThat(pointcut.matches(testMethod, MemberServiceImpl::class.java)).isTrue()
  }

  @Test
  fun `packagePatterMismatch`() {
    pointcut.expression = "execution(* com.toy.springaop.*.*(..))"
    Assertions.assertThat(pointcut.matches(testMethod, MemberServiceImpl::class.java)).isFalse()
  }

  // execution 과 거의 동일
  // execution 에서 typeMatching 만 지원
  // *, .. 등 사용 가능
  // 주의, execution 과 다르게 typeMatching 시 부모타입을 지정할 수 없음. 정확하게 해당 타입으로 명시해줘야 함
  @Test
  fun `within`() {
    pointcut.expression = "within(com.toy.springaop.service.MemberServiceImpl)"
    Assertions.assertThat(pointcut.matches(testMethod, MemberServiceImpl::class.java)).isTrue()
  }
}