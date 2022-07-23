package com.toy.productinfo.domain

import com.toy.productinfo.domain.enum.UserRole
import com.toy.productinfo.domain.enum.UserStatus
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
class User (
  id: String? = null,
  name: String,
  username: String,
  password: String,
  role: UserRole,
  status: UserStatus
): AbstractDateTraceEntity() {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id = id
    protected set

  var name = name
    protected set

  var username = username
    protected set

  var password = password
    protected set

  @Enumerated(EnumType.STRING)
  var role = role
    protected set

  @Enumerated(EnumType.STRING)
  var status = status
    protected set

  companion object {
    fun newInstance(name: String, username: String, password: String,
                    role: UserRole, status: UserStatus = UserStatus.ENABLED): User {
      return User(name = name, username = username, password = password, role = role, status = status)
    }
  }
}