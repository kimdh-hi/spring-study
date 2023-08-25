package com.toy.springcacheex.domain

import com.toy.springcacheex.domain.enums.UserStatus
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "tb_user")
@EntityListeners(AuditingEntityListener::class)
class User (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,

  @CreatedDate
  var createdDate: LocalDateTime?,

  @Enumerated(EnumType.STRING)
  val status: UserStatus
) {

  override fun toString(): String {
    return "User(id=$id, username='$username', createdDate=$createdDate)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as User
    return id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }
}