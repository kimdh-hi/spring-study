package com.toy.jpadatetime.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDateTime

@Table(name = "tb_user")
@Entity
@EntityListeners(AuditingEntityListener::class)
class User(
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  var id: String? = null,

  var name: String,
) {
  @Column(name = "created_at", updatable = false)
  @CreatedDate
  lateinit var createdAt: Instant

  @Column(name = "created_date", updatable = false)
  @CreatedDate
  lateinit var createdDate: LocalDateTime

  override fun toString(): String {
    return "User(id=$id, name='$name', createdAt=$createdAt, createdDate=$createdDate)"
  }
}