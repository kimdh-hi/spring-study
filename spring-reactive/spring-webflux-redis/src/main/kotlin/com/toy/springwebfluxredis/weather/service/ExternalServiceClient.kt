package com.toy.springwebfluxredis.weather.service

import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

@Service
class ExternalServiceClient {

  @CachePut(value = ["weather"], key = "#zip")
  fun getWeatherInfo(zip: Int): Int {
    return ThreadLocalRandom.current().nextInt(60, 100)
  }
}