package com.toy.springopenfeign

import com.toy.springopenfeign.config.OpenFeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(url = "\${open-feign.my-fake-url}/exception", name = "myFakeApiFeign", configuration = [OpenFeignConfig::class])
interface MyFakeExceptionApiFeign {

  @GetMapping
  fun exception(): String
}