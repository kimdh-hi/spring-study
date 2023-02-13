package com.toy.springcloudconfigtest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "custom")
class CustomProperties {

  lateinit var test: String

  lateinit var enc: String

  lateinit var enc2: String

}