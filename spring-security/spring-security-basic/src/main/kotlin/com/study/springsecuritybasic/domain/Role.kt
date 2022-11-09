package com.study.springsecuritybasic.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.util.ProxyUtils
import java.io.Serial
import javax.persistence.*

@Table
@Entity(name = "mt_role")
class Role(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String,

  @Column(nullable = false)
  var name: String? = null,

  @ElementCollection(targetClass = Authority::class, fetch = FetchType.EAGER)
  @JoinTable(name = "tu_role_authorities", joinColumns = [JoinColumn(name = "role_id")])
  @Column(name = "authority", nullable = false)
  @Enumerated(EnumType.STRING)
  val authorities: Set<Authority> = mutableSetOf()

) {

  override fun equals(other: Any?): Boolean {
    other ?: return false
    if (this === other) return true
    if (javaClass != ProxyUtils.getUserClass(other)) return false

    other as Role
    return id == other.id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }

  override fun toString(): String {
    return "Role(id=$id, name=$name, authorities=$authorities)"
  }

  companion object {
    @Serial
    private const val serialVersionUID: Long = -4659417788919551430L
  }
}