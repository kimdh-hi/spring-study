package com.toy.springcoreadvancedaop.trace.hellotrace

import com.toy.springcoreadvancedaop.trace.TraceId
import com.toy.springcoreadvancedaop.trace.TraceStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class HelloTraceV2 {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    private const val START_PREFIX = "-->"
    private const val COMPLETE_PREFIX = "<--"
    private const val EX_PREFIX = "<X-"
  }

  fun begin(message: String): TraceStatus {
    val traceId = TraceId()
    val startTimeMs = System.currentTimeMillis()
    log.info(
      "[{}] {}{}", traceId.id, addSpace(
        START_PREFIX,
        traceId.level
      ), message
    )
    return TraceStatus(traceId, startTimeMs, message)
  }

  // v2 traceId sync 추가
  // 각 레이어에서 traceId가 유지되도록
  fun beginSync(beforeTraceId: TraceId, message: String): TraceStatus {
    val nextTraceId = beforeTraceId.createNextId()
    val startTimeMs = System.currentTimeMillis()
    log.info(
      "[{}] {}{}", nextTraceId.id, addSpace(
        START_PREFIX,
        nextTraceId.level
      ), message
    )
    return TraceStatus(nextTraceId, startTimeMs, message)
  }

  fun end(status: TraceStatus) {
    complete(status, null)
  }

  fun exception(status: TraceStatus, e: Exception?) {
    complete(status, e)
  }

  private fun complete(status: TraceStatus, e: java.lang.Exception?) {
    val stopTimeMs = System.currentTimeMillis()
    val resultTimeMs = stopTimeMs - status.startTimeMs
    val (id, level) = status.traceId
    if (e == null) {
      log.info(
        "[{}] {}{} time={}ms", id,
        addSpace(COMPLETE_PREFIX, level), status.message,
        resultTimeMs
      )
    } else {
      log.info(
        "[{}] {}{} time={}ms ex={}", id,
        addSpace(EX_PREFIX, level), status.message, resultTimeMs,
        e.toString()
      )
    }
  }

  private fun addSpace(prefix: String, level: Int): String {
    val sb = StringBuilder()
    for (i in 0 until level) {
      sb.append(if (i == level - 1) "|$prefix" else "|   ")
    }
    return sb.toString()
  }
}