package com.toy.oauthbasic.oauth2

import com.toy.oauthbasic.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

data class OAuth2UserPrincipal (
  val oAuth2Token: OAuth2Token,
  val oidcIdToken: OidcIdToken? = null, // oidc
  val _claims: MutableMap<String, Any> = mutableMapOf(), // oidc
  val _attributes: MutableMap<String, Any> = mutableMapOf(),
  val user: User
): OAuth2User, OidcUser {

  companion object {
    fun createOidcUserPrincipal(user: User, oidcUser: OidcUser, oAuth2Token: OAuth2Token, oidcIdToken: OidcIdToken): OAuth2UserPrincipal {
      return OAuth2UserPrincipal(
        user = user,
        oAuth2Token = oAuth2Token,
        oidcIdToken = oidcIdToken,
        _claims = oidcUser.claims,
        _attributes = oidcUser.attributes
      )
    }

    fun createOauth2UserPrincipal(user: User, oAuth2User: OAuth2User, oauth2Token: OAuth2Token): OAuth2UserPrincipal {
      return OAuth2UserPrincipal(
        user = user,
        oAuth2Token = oauth2Token,
        _attributes = oAuth2User.attributes
      )
    }
  }

  override fun getName(): String = user.username

  override fun getAttributes(): MutableMap<String, Any> = _claims

  override fun getClaims(): MutableMap<String, Any> = _attributes

  override fun getIdToken(): OidcIdToken? = oidcIdToken

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return mutableListOf()
  }

  override fun getUserInfo(): OidcUserInfo? = null

}