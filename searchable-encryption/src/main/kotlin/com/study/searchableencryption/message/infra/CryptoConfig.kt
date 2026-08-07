package com.study.searchableencryption.message.infra

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

@Configuration
class CryptoConfig {

  @Bean
  fun textEncryptor(
    @Value("\${app.crypto.data-key}") dataKey: String,
    @Value("\${app.crypto.salt}") salt: String,
  ): TextEncryptor = Encryptors.delux(dataKey, salt)
}
