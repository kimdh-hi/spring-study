package com.example.ex.controller.api

import com.example.ex.common.MessageSourceService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Locale
import javax.servlet.http.HttpServletRequest

@RequestMapping("/api/localization")
@RestController
class LocalizationApiController(private val messageSourceService: MessageSourceService) {

    private val LOG = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun localization(request: HttpServletRequest): ResponseEntity<String> {
        val message = messageSourceService.getMessage("key1")
        return ResponseEntity.ok(message)
    }

    @GetMapping("/params")
    fun localizationParams(): ResponseEntity<String> {
        val message = messageSourceService.getMessage("key3", "김대현")

        return ResponseEntity.ok(message)
    }

    @GetMapping("/params-newline")
    fun localizationParamsNewline(): ResponseEntity<String> {
        val message = messageSourceService.getMessage("key4", "김대현")

        return ResponseEntity.ok(message)
    }

    @GetMapping("/header")
    fun header(request: HttpServletRequest): ResponseEntity<Locale> {
        val acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)
        val locale = getMostPreferredLocale(acceptLanguage)
        return ResponseEntity.ok(locale)
    }

    private fun getMostPreferredLocale(acceptLanguage: String): Locale {
        val indexOfDelimiter = acceptLanguage.indexOf(",")
        return if (indexOfDelimiter == -1) {
            Locale("en", "KR") // default-language
        } else {
            val locale = acceptLanguage.substring(0, indexOfDelimiter)
            convertStringToLocale(locale)
        }
    }

    private fun convertStringToLocale(localeString: String): Locale {
        val indexOfDelimiter = localeString.indexOf("-")
        return if (indexOfDelimiter == -1) {
            Locale(localeString, "KR")
        } else {
            val language = localeString.substring(0, indexOfDelimiter)
            val country = localeString.substring(indexOfDelimiter+1)
            Locale(language, country)
        }
    }
}