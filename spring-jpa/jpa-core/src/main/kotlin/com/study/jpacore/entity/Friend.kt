package com.study.jpacore.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator
import org.hibernate.envers.Audited

@Entity
@Audited
class Friend(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @ManyToOne(optional = false)
  @JoinColumn(name = "from_user_id")
  val fromUser: User,

  @ManyToOne(optional = false)
  @JoinColumn(name = "to_user_id")
  val toUser: User
) {
  companion object {
    fun of(fromUser: User, toUser: User): Friend {
      return Friend(fromUser = fromUser, toUser = toUser)
    }
  }
}
