package com.toy.emailauthentication.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_email_authentication")
class EmailAuthentication (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  var user: User
) {
  companion object {
    fun newInstance(user: User): EmailAuthentication {
      return EmailAuthentication(user = user)
    }
  }
}