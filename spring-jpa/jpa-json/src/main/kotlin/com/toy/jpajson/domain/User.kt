package com.toy.jpajson.domain

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Lob
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type

@Entity(name = "tb_user")
//@TypeDef(name = "json", typeClass = JsonType.class) // boot 2.x
class User(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val name: String,

  /*
  @Type(type = "json") // boot 2.x

  columnDefinition = "longtext" // mariadb 최대 4GB text
   */
  @Type(JsonType::class)
  @Column(name = "avatarV1", columnDefinition = "longtext")
  val avatarV1: Map<String, Any>,

  @Lob
  @Column(name = "avatarV2")
  val avatarV2: String
) {
  override fun toString(): String {
    return "User(id=$id, name='$name', avatarV1=$avatarV1, avatarV2=$avatarV2)"
  }
}