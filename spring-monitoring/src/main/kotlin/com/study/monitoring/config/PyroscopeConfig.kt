package com.study.monitoring.config

import io.pyroscope.http.Format
import io.pyroscope.javaagent.EventType
import io.pyroscope.javaagent.PyroscopeAgent
import io.pyroscope.javaagent.config.Config
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * Starts the Pyroscope continuous-profiling agent so JVM profiles are pushed to the Pyroscope server.
 * Completes the four pillars: metrics(Mimir) / logs(Loki) / traces(Tempo) / profiles(Pyroscope).
 */
@Configuration
class PyroscopeConfig(
  @Value("\${spring.application.name}")
  private val applicationName: String,
  @Value("\${pyroscope.server-address:http://localhost:4040}")
  private val serverAddress: String,
) {

  @PostConstruct
  fun startPyroscope() {
    PyroscopeAgent.start(
      Config.Builder()
        .setApplicationName(applicationName)
        .setProfilingEvent(EventType.ITIMER)
        .setFormat(Format.JFR)
        .setServerAddress(serverAddress)
        .build()
    )
  }
}
