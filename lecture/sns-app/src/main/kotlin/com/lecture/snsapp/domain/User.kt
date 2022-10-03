package com.lecture.snsapp.domain

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "users")
@SQLDelete(sql = "update users set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is NULL")
class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  @Column(name = "username")
  val username: String,

  @Column(name = "password")
  var password: String,

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  var role: UserRole? = null,

  @Column(name = "register_at")
  var registerAt: Timestamp? = null,

  @Column(name = "updated_at")
  var updatedAt: Timestamp? = null,

  @Column(name = "deleted_at")
  var deletedAt: Timestamp? = null,
) {
  companion object {
    fun of(username: String, password: String) = User(
      username = username,
      password = password
    )
  }

  @PrePersist
  fun prePersist() {
    registerAt = Timestamp.from(Instant.now())
  }

  @PreUpdate
  fun preUpdate() {
    updatedAt = Timestamp.from(Instant.now())
  }
}