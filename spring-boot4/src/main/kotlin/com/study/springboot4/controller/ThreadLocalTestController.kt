package com.study.springboot4.controller

import com.study.springboot4.context.RequestContextHolder
import com.study.springboot4.httpclients.TestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/test/threadlocal")
class ThreadLocalTestController(
  private val testClient: TestClient
) {
  private val log = LoggerFactory.getLogger(ThreadLocalTestController::class.java)

  @GetMapping
  fun testThreadLocalPropagation(): Map<String, Any?> {
    // 컨트롤러에서 ThreadLocal과 SecurityContext 값 확인
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser
    )

    // HTTP 클라이언트 호출 (RestClientLoggingInterceptor에서 ThreadLocal 값 확인)
    val response = testClient.get(1)

    log.info(
      "[Controller After HTTP Call] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      RequestContextHolder.getRequestId(),
      RequestContextHolder.getUserInfo(),
      SecurityContextHolder.getContext().authentication?.name
    )

    return mapOf(
      "threadId" to Thread.currentThread().threadId(),
      "isVirtualThread" to Thread.currentThread().isVirtual,
      "requestId" to requestId,
      "userInfo" to userInfo,
      "securityUser" to securityUser,
      "httpResponse" to response
    )
  }

  @GetMapping("/multiple")
  fun testMultipleHttpCalls(): Map<String, Any?> {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller Multiple] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser
    )

    // 여러 번 HTTP 호출
    val responses = (1..3).map { id ->
      log.info("[Before HTTP Call #$id] RequestId=${RequestContextHolder.getRequestId()}, SecurityUser=${SecurityContextHolder.getContext().authentication?.name}")
      val response = testClient.get(id.toLong())
      log.info("[After HTTP Call #$id] RequestId=${RequestContextHolder.getRequestId()}, SecurityUser=${SecurityContextHolder.getContext().authentication?.name}")
      response
    }

    return mapOf(
      "threadId" to Thread.currentThread().threadId(),
      "isVirtualThread" to Thread.currentThread().isVirtual,
      "requestId" to requestId,
      "userInfo" to userInfo,
      "securityUser" to securityUser,
      "httpResponses" to responses
    )
  }

  @GetMapping("/async")
  fun testAsyncThreadLocalPropagation(): Map<String, Any?> = runBlocking {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller Async] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser
    )

    // 코루틴으로 비동기 호출
    val deferred1 = async(Dispatchers.IO) {
      log.info(
        "[Async Task 1] ThreadId={}, VirtualThread={}, RequestId={}, SecurityUser={}",
        Thread.currentThread().threadId(),
        Thread.currentThread().isVirtual,
        RequestContextHolder.getRequestId(),
        SecurityContextHolder.getContext().authentication?.name
      )
      testClient.get(1)
    }

    val deferred2 = async(Dispatchers.IO) {
      log.info(
        "[Async Task 2] ThreadId={}, VirtualThread={}, RequestId={}, SecurityUser={}",
        Thread.currentThread().threadId(),
        Thread.currentThread().isVirtual,
        RequestContextHolder.getRequestId(),
        SecurityContextHolder.getContext().authentication?.name
      )
      testClient.get(2)
    }

    mapOf(
      "threadId" to Thread.currentThread().threadId(),
      "isVirtualThread" to Thread.currentThread().isVirtual,
      "requestId" to requestId,
      "userInfo" to userInfo,
      "securityUser" to securityUser,
      "response1" to deferred1.await(),
      "response2" to deferred2.await(),
      "note" to "비동기 작업에서는 ThreadLocal이 전파되지 않을 수 있습니다"
    )
  }

  @PostMapping
  fun testThreadLocalWithPost(@RequestBody request: TestRequest): Map<String, Any?> {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller POST] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}, RequestBody={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser,
      request
    )

    val response = testClient.get(1)

    return mapOf(
      "threadId" to Thread.currentThread().threadId(),
      "isVirtualThread" to Thread.currentThread().isVirtual,
      "requestId" to requestId,
      "userInfo" to userInfo,
      "securityUser" to securityUser,
      "requestBody" to request,
      "httpResponse" to response
    )
  }

  @GetMapping("/delay")
  fun testThreadLocalWithDelay(@RequestParam(defaultValue = "1000") delayMs: Long): Map<String, Any?> {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller Delay] Start - ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser
    )

    // 지연 시간 추가 (Virtual Thread가 블로킹되는 동안 다른 작업 처리 가능)
    Thread.sleep(delayMs)

    log.info(
      "[Controller Delay] After Sleep - ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      RequestContextHolder.getRequestId(),
      RequestContextHolder.getUserInfo(),
      SecurityContextHolder.getContext().authentication?.name
    )

    val response = testClient.get(1)

    return mapOf(
      "threadId" to Thread.currentThread().threadId(),
      "isVirtualThread" to Thread.currentThread().isVirtual,
      "requestId" to requestId,
      "userInfo" to userInfo,
      "securityUser" to securityUser,
      "delayMs" to delayMs,
      "httpResponse" to response
    )
  }

  @GetMapping("/error")
  fun testThreadLocalWithError(): Map<String, Any?> {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller Error] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser
    )

    // 의도적으로 에러 발생
    throw RuntimeException("테스트 에러 - ThreadLocal이 제대로 정리되는지 확인")
  }

  @GetMapping("/info")
  fun getThreadLocalInfo(): Map<String, Any?> {
    val requestId = RequestContextHolder.getRequestId()
    val userInfo = RequestContextHolder.getUserInfo()
    val securityUser = SecurityContextHolder.getContext().authentication?.name

    log.info(
      "[Controller Info] ThreadId={}, VirtualThread={}, RequestId={}, UserInfo={}, SecurityUser={}",
      Thread.currentThread().threadId(),
      Thread.currentThread().isVirtual,
      requestId,
      userInfo,
      securityUser
    )

    return mapOf(
      "threadId" to Thread.currentThread().threadId(),
      "isVirtualThread" to Thread.currentThread().isVirtual,
      "threadName" to Thread.currentThread().name,
      "requestId" to requestId,
      "userInfo" to userInfo,
      "securityUser" to securityUser,
      "securityAuthorities" to SecurityContextHolder.getContext().authentication?.authorities?.map { it.authority }
    )
  }

  data class TestRequest(
    val name: String,
    val message: String
  )
}
