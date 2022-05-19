package com.study.jwt.util

import com.study.jwt.auth.JwtPrincipal
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {
    fun getCurrentPrincipal(): JwtPrincipal {
        val authentication = SecurityContextHolder.getContext().authentication ?: throw AccessDeniedException("principal is not founded...")
        return if (authentication.principal is JwtPrincipal) authentication.principal as JwtPrincipal
            else throw AccessDeniedException("principal is not valid...")
    }
}