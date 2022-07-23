package com.toy.reactivejdsl.domain

import com.toy.reactivejdsl.common.PasswordUtils
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serial
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(
  @Id
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  var id: String? = null,
  var name: String,
  var username: String,
  var password: String,

  @ManyToOne(optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  var company: Company,

  @ManyToOne(optional = false)
  var role: Role
): AbstractByTraceEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -8520117387419801999L

    fun newUser(name: String, username:String, password: String, company: Company, role: Role): User
      = User(
        name = name,
        username = username, password = password,
        company = company,
        role = role
      )
  }

  fun checkPassword(password: String) = PasswordUtils.matches(password, this.password)

  override fun toString(): String {
    return "User(id=$id, name='$name', username='$username', password='$password', company=$company)"
  }

  override fun getPk(): Any? = id

  override fun getType() = id
}