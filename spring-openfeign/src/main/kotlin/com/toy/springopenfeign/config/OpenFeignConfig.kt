package com.toy.springopenfeign.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["com.toy.springopenfeign"]) // main 클래스가 아닌 곳에서 설정시 basePackages 설정필요
class OpenFeignConfig {
}