package com.toy.jpaeventlistener.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class UserParticipant(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var participated: Boolean,

  @Column(name = "user_id", nullable = false)
  val userId: String
) {

  companion object {
    fun of(userId: String) = UserParticipant(participated = true, userId = userId)
  }
}

interface UserParticipantRepository: JpaRepository<UserParticipant, String> {
  fun findByUserId(userId: String): UserParticipant?
}