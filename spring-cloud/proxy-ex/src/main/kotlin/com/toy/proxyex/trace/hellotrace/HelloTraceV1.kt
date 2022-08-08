package com.toy.proxyex.trace.hellotrace

import com.toy.proxyex.trace.TraceId
import com.toy.proxyex.trace.TraceStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class HelloTraceV1 {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    private const val START_PREFIX = "-->"
    private const val COMPLETE_PREFIX = "<--"
    private const val EX_PREFIX = "<X-"
  }

  fun begin(message: String?): TraceStatus {
    val traceId = TraceId()
    val startTimeMs = System.currentTimeMillis()
    log.info(
      "[{}] {}{}", traceId.id, addSpace(
        START_PREFIX,
        traceId.level
      ), message
    )
    return TraceStatus(traceId, startTimeMs, message!!)
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