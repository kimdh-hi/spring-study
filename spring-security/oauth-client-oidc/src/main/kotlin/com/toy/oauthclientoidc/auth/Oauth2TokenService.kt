package com.toy.oauthclientoidc.auth

import com.toy.oauthclientoidc.utils.ParamUtils
import com.toy.oauthclientoidc.utils.WebClientUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class Oauth2TokenService(
  private val clientRegistrationRepository: ClientRegistrationRepository,
  @Qualifier("OauthWebClient") private val webClient: WebClient
) {

  private val log = LoggerFactory.getLogger(javaClass)

  private val webClientUtils: WebClientUtils = WebClientUtils(webClient)

  fun getToken(registrationId: String, code: String): Mono<Oauth2TokenResponseVO> {
    val clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId)

    val requestVO = Oauth2TokenRequestVO(
      code = code,
      clientId = clientRegistration.clientId,
      clientSecret = clientRegistration.clientSecret,
      redirectUri = "http://localhost:8080/oauth2/google",
      grantType = AuthorizationGrantType.AUTHORIZATION_CODE.value
    )

    val params = ParamUtils.convert(requestVO)
    log.info("params: {}", params)

    return webClientUtils.post(
      uri = "https://accounts.google.com/o/oauth2/token",
      body = params,
      returnType = Oauth2TokenResponseVO::class
    )
  }
}