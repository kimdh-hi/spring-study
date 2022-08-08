package com.toy.proxyex

import com.toy.proxyex.config.AppV1Config
import com.toy.proxyex.config.AppV2Config
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(AppV1Config::class, AppV2Config::class)
@SpringBootApplication(scanBasePackages = ["com.toy.proxyex.app"])
class ProxyExApplication

fun main(args: Array<String>) {
  runApplication<ProxyExApplication>(*args)
}
