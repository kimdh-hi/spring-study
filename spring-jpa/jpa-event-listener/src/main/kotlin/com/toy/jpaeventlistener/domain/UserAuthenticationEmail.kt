package com.toy.jpaeventlistener.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_user_authentication_email")
class UserAuthenticationEmail(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  val user: User
)

interface UserAuthenticationEmailRepository: JpaRepository<UserAuthenticationEmail, String>