package com.toy.springwebfluxredis.weather.controller

import com.toy.springwebfluxredis.weather.service.WeatherService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/weather")
class WeatherController(
  private val weatherService: WeatherService
) {

  @GetMapping("/{zip}")
  fun getWeather(@PathVariable zip: Int) = Mono.fromSupplier { weatherService.getInfo(zip) }
}