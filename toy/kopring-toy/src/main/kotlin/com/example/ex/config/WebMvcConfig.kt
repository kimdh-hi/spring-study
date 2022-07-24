package com.example.ex.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.SessionLocaleResolver

@Configuration
class WebMvcConfig: WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(SearchTypeConverter())
    }

    @Bean
    fun localeResolver() = SessionLocaleResolver()

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.addBasenames("message.messages")
        messageSource.setUseCodeAsDefaultMessage(true)
        return messageSource
    }
}