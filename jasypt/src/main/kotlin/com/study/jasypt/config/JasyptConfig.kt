package com.study.jasypt.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.PBEConfig
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig(val jasyptPropertyConfig: JasyptProperties) {

    @Bean
    fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        encryptor.setConfig(getPBEConfig())
        return encryptor
    }

    fun getPBEConfig(): PBEConfig {
        val config = SimpleStringPBEConfig()
        config.password = jasyptPropertyConfig.password
        config.keyObtentionIterations = 1000
        config.setPoolSize("1")
        config.providerName = "SunJCE"
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
        config.stringOutputType = "base64"
        return config
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "jasypt.encryptor")
data class JasyptProperties(val password: String)