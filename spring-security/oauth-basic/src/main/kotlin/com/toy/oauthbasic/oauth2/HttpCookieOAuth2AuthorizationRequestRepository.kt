package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.utils.CookieUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
    return CookieUtils.get(request, "oauth2_auth_request")?.let {
      CookieUtils.deserialize(it, OAuth2AuthorizationRequest::class.java)
    }
  }

  override fun saveAuthorizationRequest(
    authRequest: OAuth2AuthorizationRequest?,
    request: HttpServletRequest,
    response: HttpServletResponse
  ) {
    if (authRequest == null) return
    CookieUtils.add(
      response,
      "oauth2_auth_request",
      CookieUtils.serialize(authRequest),
      COOKIE_EXPIRE_SECONDS
    )
    val redirectUriAfterLogin = request.getParameter("redirect_uri")
    if (StringUtils.hasText(redirectUriAfterLogin)) CookieUtils.add(
      response,
      "redirect_uri",
      redirectUriAfterLogin,
      COOKIE_EXPIRE_SECONDS
    )
  }

  override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
    return loadAuthorizationRequest(request)
  }

  fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
    CookieUtils.delete(request, response, "oauth2_auth_request")
    CookieUtils.delete(request, response, "redirect_uri")
  }

  companion object {
    private const val COOKIE_EXPIRE_SECONDS = 180
  }
}