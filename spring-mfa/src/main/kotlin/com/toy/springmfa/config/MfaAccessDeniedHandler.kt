package com.toy.springmfa.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.authorization.FactorAuthorizationDecision
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.access.AccessDeniedHandler
import tools.jackson.databind.json.JsonMapper

class MfaAccessDeniedHandler(
  private val jsonMapper: JsonMapper,
) : AccessDeniedHandler {

  private val delegate = BearerTokenAccessDeniedHandler()

  override fun handle(request: HttpServletRequest, response: HttpServletResponse, exception: AccessDeniedException) {
    val decision = (exception as? AuthorizationDeniedException)?.authorizationResult as? FactorAuthorizationDecision
    if (decision == null) {
      delegate.handle(request, response, exception)
      return
    }
    val problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "additional authentication factor required").apply {
      setProperty(
        "requiredFactors",
        decision.factorErrors.map {
          mapOf(
            "authority" to it.requiredFactor.authority,
            "validDuration" to it.requiredFactor.validDuration?.toString(),
            "reason" to if (it.isExpired) "expired" else "missing",
          )
        },
      )
    }
    response.status = HttpStatus.UNAUTHORIZED.value()
    response.setHeader(
      HttpHeaders.WWW_AUTHENTICATE,
      """Bearer error="insufficient_user_authentication", error_description="additional authentication factor required"""",
    )
    response.contentType = MediaType.APPLICATION_PROBLEM_JSON_VALUE
    jsonMapper.writeValue(response.outputStream, problem)
  }
}
