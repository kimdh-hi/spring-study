package com.toy.proxyex.trace.logtrace

import com.toy.proxyex.trace.TraceId
import com.toy.proxyex.trace.TraceStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ThreadLocalLogTrace: LogTrace {

  private val log = LoggerFactory.getLogger(javaClass)

  private var traceIdHolder: ThreadLocal<TraceId?> = ThreadLocal()

  companion object {
    private const val START_PREFIX = "-->"
    private const val COMPLETE_PREFIX = "<--"
    private const val EX_PREFIX = "<X-"
  }

  override fun begin(message: String): TraceStatus {
    syncTraceId()
    val traceId = traceIdHolder.get()!!
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
    this.traceIdHolder.get()?.let {
      traceIdHolder.set(it.createNextId())
    } ?: run {
      traceIdHolder.set(TraceId())
    }
  }

  private fun releaseTraceId() {
    this.traceIdHolder.get()?.let {
      if (it.isFirstLevel()) {
        traceIdHolder.set(null)
      } else {
        traceIdHolder.set(it.createPreviousId())
      }
    } ?: throw RuntimeException("failed to release ...")
  }
}