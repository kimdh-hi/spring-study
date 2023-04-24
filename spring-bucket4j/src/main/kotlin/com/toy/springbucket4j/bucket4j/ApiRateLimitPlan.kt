package com.toy.springbucket4j.bucket4j

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Refill
import java.time.Duration

enum class ApiRateLimitPlan {

  FREE {
    // 5분-10회 호출 가능
    fun getLimit(): Bandwidth
      = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(5)))
  },

  BASIC {
    // 5분-30회 호출 가능
    fun getLimit(): Bandwidth
      = Bandwidth.classic(30, Refill.intervally(30, Duration.ofMinutes(5)))
  },

  // 1분-50회 호출 가능
  ENTERPRISE {
    fun getLimit(): Bandwidth
      = Bandwidth.classic(50, Refill.intervally(50, Duration.ofMinutes(1)))
  }
}