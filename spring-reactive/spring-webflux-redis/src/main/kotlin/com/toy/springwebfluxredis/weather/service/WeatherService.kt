package com.toy.springwebfluxredis.weather.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.stream.IntStream

@Service
class WeatherService(
  private val externalServiceClient: ExternalServiceClient
) {

  @Cacheable("weather")
  fun getInfo(zip: Int): Int {
    return 0
  }

  @Scheduled(fixedRate = 10_000)
  fun update() {
    println("update...")
    IntStream.rangeClosed(1, 5)
      .forEach { externalServiceClient.getWeatherInfo(it) }
  }
}