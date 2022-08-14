package com.toy.springcoreadvancedaop.trace.logtrace

import com.toy.springcoreadvancedaop.trace.TraceId
import com.toy.springcoreadvancedaop.trace.TraceStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FieldSyncLogTrace: LogTrace {

  private val log = LoggerFactory.getLogger(javaClass)

  // 필드방식 동기화
  // 동시성 문제...
  // 현재 FieldSyncLogTrace 는 컴포넌트 스캔 대상이므로 싱클톤 객체임
  // 모든 쓰레드가 한 개 인스턴스만을 참조하는 것.
  // 한 개 인스턴스의 한 개 필드에 여러 쓰레드가 접근하므로 동시성 이슈가 생기는 것.
  private var traceIdHolder: TraceId? = null

  companion object {
    private const val START_PREFIX = "-->"
    private const val COMPLETE_PREFIX = "<--"
    private const val EX_PREFIX = "<X-"
  }

  override fun begin(message: String): TraceStatus {
    syncTraceId()
    val traceId = traceIdHolder!!
    val startTimeMs = System.currentTimeMillis()
    log.info("[{}] {}{}", traceId.id, addSpace(START_PREFIX, traceId.level), message)
    return TraceStatus(traceId, startTimeMs, message)
  }

  override fun end(status: TraceStatus) = complete(status, null)

  override fun exception(status: TraceStatus, e: Exception) = complete(status, e)

  private fun complete(status: TraceStatus, e: Exception?) {
    val stopTimeMs = System.currentTimeMillis()
    val resultTimeMs = stopTimeMs - status.startTimeMs
    val (id, level) = status.traceId
    if (e == null) {
      log.info("[{}] {}{} time={}ms", id, addSpace(COMPLETE_PREFIX, level), status.message, resultTimeMs)
    } else {
      log.info("[{}] {}{} time={}ms ex={}", id, addSpace(EX_PREFIX, level), status.message, resultTimeMs, e.toString())
    }

    releaseTraceId()
  }

  private fun addSpace(prefix: String, level: Int): String {
    val sb = StringBuilder()
    for (i in 0 until level) {
      sb.append(if (i == level - 1) "|$prefix" else "|   ")
    }
    return sb.toString()
  }

  private fun syncTraceId() {
    this.traceIdHolder?.let {
      traceIdHolder = it.createNextId()
    } ?: run {
      traceIdHolder = TraceId()
    }
  }

  private fun releaseTraceId() {
    this.traceIdHolder?.let {
      if (it.isFirstLevel()) {
        traceIdHolder = null
      } else {
        traceIdHolder = it.createPreviousId()
      }
    } ?: throw RuntimeException("failed to release ...")
  }
}