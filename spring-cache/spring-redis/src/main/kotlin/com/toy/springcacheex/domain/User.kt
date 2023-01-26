package com.toy.springcacheex.domain

import com.toy.springcacheex.common.LocalDateTimeSerializer
import com.toy.springcacheex.domain.enums.UserStatus
import kotlinx.serialization.Serializable
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_user")
@EntityListeners(AuditingEntityListener::class)
@Serializable
class User (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,

  @CreatedDate
  @Serializable(LocalDateTimeSerializer::class)
  var createdDate: LocalDateTime?,

  @Enumerated(EnumType.STRING)
  val status: UserStatus
)
{

  override fun toString(): String {
    return "User(id=$id, username='$username', createdDate=$createdDate)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as User

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }
}