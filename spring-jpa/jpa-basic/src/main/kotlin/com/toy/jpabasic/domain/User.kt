package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table
@Entity(name = "tb_user")
class User (

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,

  @ManyToOne(optional = false)
  @JoinColumn(name = "company_id")
  var company: Company
): Serializable {

  override fun toString(): String {
    return "User(id=$id, username='$username', company=$company)"
  }

  companion object {
    @Serial
    private const val serialVersionUID: Long = 425994759014562522L
  }
}