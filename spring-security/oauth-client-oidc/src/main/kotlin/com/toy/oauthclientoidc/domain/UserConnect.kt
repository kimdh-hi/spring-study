package com.toy.oauthclientoidc.domain

import com.toy.oauthclientoidc.common.Idp
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_user_connect")
class UserConnect (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(length = 50)
  var id: String? = null,

  @Enumerated(EnumType.STRING)
  val idp: Idp,

  @ManyToOne(optional = false, cascade = [CascadeType.REMOVE])
  @JoinColumn(name = "user_id")
  val user: User,

  val idToken: String,
  val accessToken: String
) {
  companion object {
    fun newUserConnect(idp: Idp, user: User, idToken: String, accessToken: String): UserConnect {
      return UserConnect(
        idp = idp, user = user, idToken = idToken, accessToken = accessToken
      )
    }
  }
}