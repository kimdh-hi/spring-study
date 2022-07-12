package com.toy.reactivejdsl.common

import com.toy.webfluxr2dbcpostgres.auth.JwtPrincipal
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.ReactiveSecurityContextHolder

class SecurityUtils {

  companion object {
    suspend fun getPrincipal(): JwtPrincipal {
      val securityContext = ReactiveSecurityContextHolder.getContext().awaitFirstOrDefault(null)
      if (securityContext == null || securityContext.authentication == null)
        throw AccessDeniedException("authentication not exists..")

      return if (securityContext.authentication.principal is JwtPrincipal) {
        securityContext.authentication.principal as JwtPrincipal
      } else {
        throw AccessDeniedException("Not properly authenticated..")
      }
    }
  }

}