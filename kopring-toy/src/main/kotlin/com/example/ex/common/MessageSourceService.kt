package com.example.ex.common

import org.springframework.context.MessageSource
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.stereotype.Service

@Service
class MessageSourceService(private val messageSource: MessageSource) {

    val messages: MessageSourceAccessor by lazy {
        MessageSourceAccessor(messageSource)
    }

    fun getMessage(code: String) = messages.getMessage(code)

    fun getMessage(code: String, vararg params: String) = messages.getMessage(code, params)

}