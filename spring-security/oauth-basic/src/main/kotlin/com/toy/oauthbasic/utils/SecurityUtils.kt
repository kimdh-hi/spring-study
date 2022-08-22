package com.toy.oauthbasic.utils

import com.toy.oauthbasic.oauth2.JwtPrincipal
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder

class SecurityUtils {

  companion object {
    fun getPrincipal(): JwtPrincipal {
      val authentication = SecurityContextHolder.getContext().authentication ?: throw AccessDeniedException(
        "authentication not exists.."
      )

      return if (authentication.principal is JwtPrincipal) {
        authentication.principal as JwtPrincipal
      } else {
        throw AccessDeniedException("Not properly authenticated..")
      }
    }
  }
}