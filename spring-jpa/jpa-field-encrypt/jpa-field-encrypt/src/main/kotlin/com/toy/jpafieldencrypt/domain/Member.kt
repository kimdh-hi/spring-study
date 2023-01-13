package com.toy.jpafieldencrypt.domain

import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Table(name = "tb_member")
@Entity
class Member(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Column
  @ColumnTransformer(
    write = "HEX(AES_ENCRYPT(?, secret-key))",
    read = "CAST(AES_DECRYPT(UNHEX(name), secret-key) AS CHAR)",
  )
  var name: String? = null,
) {
  override fun toString(): String {
    return "User(id=$id, name=$name)"
  }
}