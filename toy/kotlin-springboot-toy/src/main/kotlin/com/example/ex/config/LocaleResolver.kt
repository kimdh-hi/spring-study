package com.example.ex.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*

@Configuration
class LocaleResolver {

    @Bean
    fun customLocaleResolver(): LocaleResolver {
        val resolver = AcceptHeaderLocaleResolver()
        resolver.defaultLocale = Locale.KOREA
        return resolver
    }
}