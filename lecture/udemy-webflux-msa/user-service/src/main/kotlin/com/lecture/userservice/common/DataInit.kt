package com.lecture.userservice.common

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.Resource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

@Component
class DataInit(
  @Value("classpath:h2/init.sql")
  private val resource: Resource,
  private val entityTemplate: R2dbcEntityTemplate
): CommandLineRunner {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun run(vararg args: String) {
    val query = StreamUtils.copyToString(resource.inputStream, StandardCharsets.UTF_8)
    log.info("data init query={}", query)

    entityTemplate.databaseClient
      .sql(query)
      .then()
      .subscribe()
  }
}