package com.study.openfeigncircuitbreaker.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.study.openfeigncircuitbreaker")
class OpenFeignConfig
