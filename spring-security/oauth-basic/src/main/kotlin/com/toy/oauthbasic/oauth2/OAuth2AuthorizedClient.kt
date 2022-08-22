package com.toy.oauthbasic.oauth2

import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "OAUTH2_AUTHORIZED_CLIENT")
@IdClass(OAuth2AuthorizedClientPK::class)
class OAuth2AuthorizedClient (
  @Id
  val clientRegistrationId: String,
  @Id
  val principalName: String,

  val accessTokenType: String,
  val accessTokenScopes: String,
  @Lob
  val accessTokenValue: String,
  val accessTokenIssuedAt: LocalDateTime,
  val accessTokenExpiresAt: LocalDateTime,

  @Lob
  val refreshTokenValue: String,
  val refreshTokenIssuedAt: LocalDateTime,

  val createdAt: LocalDateTime
)

@Embeddable
class OAuth2AuthorizedClientPK(
  var clientRegistrationId: String,
  var principalName: String,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -2443308088367950594L
  }
}