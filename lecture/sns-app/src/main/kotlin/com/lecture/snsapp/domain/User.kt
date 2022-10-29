package com.lecture.snsapp.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tb_user")
//@SQLDelete(sql = "update users set deleted_at = now() where id = ?")
//@Where(clause = "deleted_at is NULL")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String = "",

  @Column(name = "username")
  private val username: String,

  @Column(name = "password")
  private var password: String,

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  var role: UserRole? = null,

  @Column(name = "register_at")
  var registerAt: Timestamp? = null,

  @Column(name = "updated_at")
  var updatedAt: Timestamp? = null,

  @Column(name = "deleted_at")
  var deletedAt: Timestamp? = null,
): UserDetails {

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

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(role?.name))

  override fun getPassword(): String = password

  override fun getUsername(): String = username

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true

  override fun isCredentialsNonExpired(): Boolean = true

  override fun isEnabled(): Boolean = true
  override fun toString(): String {
    return "User(id=$id, username='$username', password='$password', role=$role, registerAt=$registerAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"
  }


}