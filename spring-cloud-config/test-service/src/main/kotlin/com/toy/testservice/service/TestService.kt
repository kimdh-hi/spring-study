package com.toy.testservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Service

@RefreshScope
@Service
class TestService(
    @Value("\${my.secret}")
    val secret: String
) {

    fun test(): String {
        return secret
    }
}