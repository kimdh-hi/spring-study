package com.study.springcoreadvanced.trace.logtrace

import com.study.springcoreadvanced.trace.TraceId
import com.study.springcoreadvanced.trace.TraceStatus
import com.study.springcoreadvanced.trace.hellotrace.HelloTraceV2
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FieldLogTrace: LogTrace {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    private const val START_PREFIX = "-->"
    private const val COMPLETE_PREFIX = "<--"
    private const val EX_PREFIX = "<X-"
  }

  override fun begin(message: String): TraceStatus {
    val traceId = TraceId()
    val startTimeMs = System.currentTimeMillis()
    log.info("[{}] {}{}", traceId.id, addSpace(START_PREFIX, traceId.level), message)
    return TraceStatus(traceId, startTimeMs, message)
  }

  override fun end(status: TraceStatus) {
    TODO("Not yet implemented")
  }

  override fun exception(status: TraceStatus, e: Exception) {
    TODO("Not yet implemented")
  }

  private fun addSpace(prefix: String, level: Int): String {
    val sb = StringBuilder()
    for (i in 0 until level) {
      sb.append(if (i == level - 1) "|$prefix" else "|   ")
    }
    return sb.toString()
  }
}