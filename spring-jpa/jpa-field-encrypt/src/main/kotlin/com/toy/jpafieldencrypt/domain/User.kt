package com.toy.jpafieldencrypt.domain

import com.toy.jpafieldencrypt.utils.DatabaseFieldAesEncryptor
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Table(name = "tb_user")
@Entity
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Convert(converter = DatabaseFieldAesEncryptor::class)
  var name: String? = null,
) {
  override fun toString(): String {
    return "User(id=$id, name=$name)"
  }
}