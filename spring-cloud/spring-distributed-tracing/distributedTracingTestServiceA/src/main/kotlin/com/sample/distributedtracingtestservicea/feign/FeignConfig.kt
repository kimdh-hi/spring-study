package com.sample.distributedtracingtestservicea.feign

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.sample.distributedtracingtestservicea.feign")
class FeignConfig {
}