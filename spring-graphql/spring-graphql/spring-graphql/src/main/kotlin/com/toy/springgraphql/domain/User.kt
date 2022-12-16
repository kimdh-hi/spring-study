package com.toy.springgraphql.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_user")
class User(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  var username: String,

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  val group: Group
) {
  companion object {
    fun of(name: String, username: String, group: Group) = User(
      name = name,
      username = username,
      group = group
    )
  }
}