package com.toy.springcacheex.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "tb_user")
@Entity
class User (
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -6445825922879909934L
  }
}