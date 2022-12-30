package com.lecture.inflearnspringsecurityoauth2.config

import org.slf4j.LoggerFactory
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer

class CustomSecurityConfigurer: AbstractHttpConfigurer<CustomSecurityConfigurer, HttpSecurity>() {

  private var isSecured: Boolean = true

  private val log = LoggerFactory.getLogger(javaClass)

  /**
   * init -> configure 순서로 설정
   * AbstractConfiguredSecurityBuilder.doBuild 참고
   */
  override fun init(builder: HttpSecurity?) {
    super.init(builder)
    log.info("init...")
  }

  override fun configure(builder: HttpSecurity?) {
    super.configure(builder)
    log.info("configure...")

    if(isSecured) {
      log.info("isSecured...")
    } else {
      log.info("isNonSecured...")
    }
  }

  fun setSecured(isSecured: Boolean): CustomSecurityConfigurer {
    this.isSecured = isSecured
    return this
  }
}