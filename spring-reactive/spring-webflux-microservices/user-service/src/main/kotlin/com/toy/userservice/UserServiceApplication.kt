package com.toy.userservice

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.Resource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

@SpringBootApplication
class UserServiceApplication

fun main(args: Array<String>) {
  runApplication<UserServiceApplication>(*args)
}

@Component
class SampleDataSetup(
  @Value("classpath:h2/init.sql") private val initSql: Resource,
  private val entityTemplate: R2dbcEntityTemplate
): CommandLineRunner {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun run(vararg args: String?) {
    val initSqlQuery = StreamUtils.copyToString(initSql.inputStream, StandardCharsets.UTF_8)
    log.info("sample data init ..\n{}", initSqlQuery)
    entityTemplate
      .databaseClient
      .sql(initSqlQuery)
      .then()
      .subscribe()
  }
}
