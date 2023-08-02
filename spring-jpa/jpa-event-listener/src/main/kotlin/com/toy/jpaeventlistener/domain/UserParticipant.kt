package com.toy.jpaeventlistener.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository

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