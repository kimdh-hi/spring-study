package com.toy.oauthbasic.domain

import com.toy.oauthbasic.oauth2.Idp
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_user_oauth2_login")
class UserOAuth2Login(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @ManyToOne(optional = false, cascade = [CascadeType.REMOVE])
  @JoinColumn(name = "user_id")
  val user: User,

  @Enumerated(EnumType.STRING)
  val idp: Idp
) {
  companion object {
    fun of(user: User, idp: Idp): UserOAuth2Login {
      return UserOAuth2Login(user = user, idp = idp)
    }
  }
}