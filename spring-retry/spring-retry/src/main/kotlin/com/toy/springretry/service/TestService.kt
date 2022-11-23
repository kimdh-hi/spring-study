package com.toy.springretry.service

import com.toy.springretry.exceptions.CustomException
import com.toy.springretry.utils.RetryUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class TestService(
  private val retryUtils: RetryUtils
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun exceptionTest(): String {
    retryUtils.run(5, 3000, listOf(CustomException::class.java)) {
      log.info("try...")
      throw CustomException()
    }
  }

}