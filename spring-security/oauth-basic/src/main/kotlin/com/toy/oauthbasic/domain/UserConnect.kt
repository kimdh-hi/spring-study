package com.toy.oauthbasic.domain

import com.toy.oauthbasic.oauth2.Idp
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_user_connect")
class UserConnect(
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
    fun of(user: User, idp: Idp): UserConnect {
      return UserConnect(user = user, idp = idp)
    }
  }
}