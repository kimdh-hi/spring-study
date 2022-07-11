package com.toy.kotlinjdsl.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.io.Serializable
import javax.persistence.*

@Entity(name = "tb_user")
@Table
class User(
  @Id
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @GeneratedValue(generator = "uuid")
  var id: String? = null,
  var name: String,
  var username: String,
  var password: String,
  @ManyToOne(optional = false)
  var company: Company,
  @ManyToOne(optional = false)
  var role: Role
): Serializable {
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

  override fun toString(): String {
    return "User(id=$id, name='$name', username='$username', password='$password', company=$company)"
  }
}