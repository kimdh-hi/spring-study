package com.example.ex.filter

import org.apache.commons.lang.StringUtils
import org.apache.tomcat.jni.Local
import org.slf4j.LoggerFactory
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.PatternMatchUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.support.RequestContextUtils
import org.springframework.web.util.WebUtils
import java.util.Locale
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LocaleFilter: OncePerRequestFilter() {

    private val LOG = LoggerFactory.getLogger(javaClass)

    companion object {
        private val LOCALE = "locale"
        private val SESSION_LOCALE_KEY = "org.apache.struts2.action.LOCALE";
    }

    val SKIP_URI = arrayOf(
        "/static/**"
    )

    val DEFAULT_LOCALE = "ko"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        if (checkSkipUrI(request)) {
            chain.doFilter(request, response)
            return
        }

        var localeParam = request.getParameter(LOCALE)
        if (StringUtils.isBlank(localeParam)) {
            localeParam = getLocaleFromSessionOrDefaultLocale(request)
        }

        val locale = convertStringToLocale(localeParam)

        setLocale(request, locale)

        chain.doFilter(request, response)
    }

    private fun setLocale(request: HttpServletRequest, locale: Locale) {
        WebUtils.setSessionAttribute(request, SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale)

        val session = request.session
        session.setAttribute(SESSION_LOCALE_KEY, locale)

        LocaleContextHolder.setLocale(locale)
    }

    private fun getLocaleFromSessionOrDefaultLocale(request: HttpServletRequest): String {
        val locale = request.session.getAttribute(SESSION_LOCALE_KEY)
        return locale?.toString() ?: DEFAULT_LOCALE
    }

    private fun convertStringToLocale(locale: String): Locale {
        val delimiterIndex = locale.indexOf("-")
        if (delimiterIndex == -1) {
            return Locale(locale)
        } else {
            val language = locale.substring(0, delimiterIndex)
            val country = locale.substring(delimiterIndex+1)
            return Locale(language, country)
        }
    }
    
    private fun checkSkipUrI(request: HttpServletRequest): Boolean {
        return PatternMatchUtils.simpleMatch(SKIP_URI, request.requestURI)
    }
}